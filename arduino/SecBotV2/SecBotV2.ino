#include <SyRenSimplified.h>

#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <Servo.h>
#include <SharpIR.h>
#include <Time.h>

#define model SharpIR::GP2Y0A02YK



const int SCANNING_IR_PIN = A0;
const int SCANNING_SERVO_FEEDBACK_PIN = A8;

const int MOTOR_PIN_1 = 2;
const int MOTOR_PIN_2 = 3;
const int FRONT_SONAR_ECHO_PIN = 5;
const int FRONT_SONAR_TRIGGER_PIN = 6;
const int SCANNING_SONAR_ECHO_PIN = 8;
const int SCANNING_SONAR_TRIGGER_PIN = 9;
const int SCANNING_SERVO_PIN = 11;
const int STEERING_SERVO_PIN = 12;
const int DISTANCE_SCAN_SERVO_PIN = 12;


SharpIR IR_prox(SharpIR::GP2Y0A02YK0F, SCANNING_IR_PIN);



const String MOTOR_1 = "MOTOR_1";
const String MOTOR_2 = "MOTOR_2";
const String STEERING_SERVO = "STEERING_SERVO";
const String SCANNING_SERVO = "SCANNING_SERVO";
const String SCANNING_SONAR = "SCANNING_SONAR";
const String SCANNING_IR = "SCANNING_IR";
const String FRONT_SONAR = "FRONT_SONAR";
const String COMPASS_HEADING = "COMPASS_HEADING";
const String ACCELEROMETER_X = "ACCELEROMETER_X";
const String ACCELEROMETER_Y = "ACCELEROMETER_Y";
const String ACCELEROMETER_Z = "ACCELEROMETER_Z";
const String DEBUG = "DEBUG";

Servo steering_servo;
Servo scanning_servo;

int scanning_servo_pos = 0;    // variable to store the servo position
int scanning_servo_min;
int scanning_servo_max;
int scanning_servo_tolerance = 3;



Adafruit_LSM303DLH_Mag_Unified mag = Adafruit_LSM303DLH_Mag_Unified(12345);

SoftwareSerial SWSerial1(NOT_A_PIN, MOTOR_PIN_1);
SyRenSimplified SR1(SWSerial1);


SoftwareSerial SWSerial2(NOT_A_PIN, MOTOR_PIN_2);
SyRenSimplified SR2(SWSerial2);

long control_loop_timestamp = millis();

long front_sonar_duration;
int front_sonar_last_cm = 0;


long scanning_sonar_duration;
int scanning_sonar_last_cm = 0;

long scanning_ir_duration;

long scanning_ir_last_cm = 0;


String inputString = "";
String serial_input_string = "";


int throttle = 0;
int lastHeading = 0;
int accelerometer_x = 0;
int accelerometer_y = 0;
int accelerometer_z = 0;


void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  SWSerial1.begin(9600);
  SWSerial2.begin(9600);

  inputString.reserve(200);

  steering_servo.attach(STEERING_SERVO_PIN);
  steering_servo.write(40);
  steering_servo.detach();

  
  powerMotor(0, 0);
  powerMotor(0, 1);
  calibrateScanningServo();



  pinMode(SCANNING_IR_PIN, INPUT);
  pinMode(SCANNING_SONAR_TRIGGER_PIN, OUTPUT);
  pinMode(SCANNING_SONAR_ECHO_PIN, INPUT);

  pinMode(FRONT_SONAR_TRIGGER_PIN, OUTPUT);
  pinMode(FRONT_SONAR_ECHO_PIN, INPUT);
  mag.enableAutoRange(true);

 if (!mag.begin()) {
   /* There was a problem detecting the LSM303 ... check your connections */
   Serial.println("Ooops, no LSM303 detected ... Check your wiring!");
   while (1)
     ;
 }






}

void loop() {


  if (Serial1.available()) {
    char inChar = (char)Serial1.read();
    inputString += inChar;
    if (inChar == '\n') {
      processCommand(inputString);
      inputString = "";
      

    }
  }

  if (Serial.available()) {
    char inChar = (char)Serial.read();
    serial_input_string += inChar;
    if (inChar == '\n') {
      processCommand(serial_input_string);
      serial_input_string = "";

    }
  }

  sonarPing();
  //orientationCheck();

}

void calibrateScanningServo() {
  log("Calibrating Scanning Servo");
  scanning_servo.attach(SCANNING_SERVO_PIN);
  pinMode(SCANNING_SERVO_FEEDBACK_PIN, INPUT);

  scanning_servo.write(20);
  delay(2000);
  scanning_servo.write(20);
  delay(2000);
  scanning_servo_min = analogRead(SCANNING_SERVO_FEEDBACK_PIN);

  scanning_servo.write(160);
  delay(2000);
  scanning_servo_max = analogRead(SCANNING_SERVO_FEEDBACK_PIN);

  scanning_servo.write(90);
  delay(2000);
  scanning_servo.detach();
}


void orientationCheck() {
  //compass


  if (millis() - throttle > 500) {
    throttle = millis();
    sensors_event_t event;
    mag.getEvent(&event);

    /* Display the results (acceleration is measured in m/s^2) */

    if (accelerometer_x > accelerometer_x + 1 || event.acceleration.x < accelerometer_x - 1) {
      accelerometer_x = accelerometer_x;
      sendCommand(ACCELEROMETER_X, event.acceleration.x);
    }
    if (accelerometer_y > accelerometer_y + 1 || event.acceleration.y < accelerometer_y - 1) {
      accelerometer_y = accelerometer_y;
      sendCommand(ACCELEROMETER_Y, event.acceleration.y);
    }
    if (accelerometer_z > accelerometer_z + 1 || event.acceleration.x < accelerometer_z - 1) {
      accelerometer_z = accelerometer_z;
      sendCommand(ACCELEROMETER_Z, event.acceleration.z);
    }


    float Pi = 3.14159;

    // Calculate the angle of the vector y,x
    float heading = (atan2(event.magnetic.y, event.magnetic.x) * 180) / Pi;

    // Normalize to 0-360
    if (heading < 0)
    {
      heading = 360 + heading;
    }

    //  if (heading > lastHeading + 10 || heading < lastHeading - 10) {
    lastHeading = heading;
    sendCommand(COMPASS_HEADING, heading);


    // }

  }
}
//long front_sonar_duration, front_sonar_cm, front_sonar_timestamp;
//int front_sonar_last_cm = 0;

void sonarPing() {
  //SONAR LOOP

 
  
  if (millis() - control_loop_timestamp > 1000) {
    digitalWrite(FRONT_SONAR_TRIGGER_PIN, LOW);
    delayMicroseconds(5);
    digitalWrite(FRONT_SONAR_TRIGGER_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(FRONT_SONAR_TRIGGER_PIN, LOW);
    pinMode(FRONT_SONAR_ECHO_PIN, INPUT);
    front_sonar_duration = pulseIn(FRONT_SONAR_ECHO_PIN, HIGH);
    int front_sonar_cm = (front_sonar_duration / 2) / 29.1;
   

    if (front_sonar_cm < 1000 && front_sonar_cm != front_sonar_last_cm && front_sonar_cm != front_sonar_last_cm - 1 && front_sonar_cm != front_sonar_last_cm + 1) {
      sendCommand(FRONT_SONAR, front_sonar_cm);
      front_sonar_last_cm = front_sonar_cm;
      if (front_sonar_cm < 5) {
        powerMotor(0, 1);
        powerMotor(0, 2);
      }

    }
    delay(100);


    digitalWrite(SCANNING_SONAR_TRIGGER_PIN, LOW);
    delayMicroseconds(5);
    digitalWrite(SCANNING_SONAR_TRIGGER_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(SCANNING_SONAR_TRIGGER_PIN, LOW);
    pinMode(SCANNING_SONAR_ECHO_PIN, INPUT);
    scanning_sonar_duration = pulseIn(SCANNING_SONAR_ECHO_PIN, HIGH);
    int scanning_sonar_cm = (scanning_sonar_duration / 2) / 29.1;

 

    if (scanning_sonar_cm != scanning_sonar_last_cm && scanning_sonar_cm != scanning_sonar_last_cm - 1 && scanning_sonar_cm != scanning_sonar_last_cm + 1) {
      sendCommand(SCANNING_SONAR, scanning_sonar_cm);
      scanning_sonar_last_cm = scanning_sonar_cm;
  
    }


    int scanning_ir_cm = IR_prox.getDistance();
   

    if (scanning_ir_cm != scanning_ir_last_cm && scanning_ir_cm != scanning_ir_last_cm - 1 && scanning_ir_cm != scanning_ir_last_cm + 1) {
      sendCommand(SCANNING_IR, scanning_ir_cm);
      scanning_ir_last_cm = scanning_ir_cm;
    }

    control_loop_timestamp = millis();


  }
}

void steerServo(int newPos) {
//  log("steerServo");
  if (newPos <= 70 && newPos >= 20) {
    steering_servo.attach(STEERING_SERVO_PIN);
    delay(100);
    steering_servo.write(newPos);
    delay(100);
    steering_servo.detach();

  }



}

void scanServo(int newPos) {
  
  if (newPos <= 160 && newPos >= 20) {

   // log("scan Servo");
    scanning_servo.attach(SCANNING_SERVO_PIN);
    delay(100);
    scanning_servo.write(newPos);
    log(map( analogRead(SCANNING_SERVO_FEEDBACK_PIN), scanning_servo_min, scanning_servo_max, 20, 160));
    while (abs(map( analogRead(SCANNING_SERVO_FEEDBACK_PIN), scanning_servo_min, scanning_servo_max, 20, 160) - newPos) > scanning_servo_tolerance) {
        log(abs(map( analogRead(SCANNING_SERVO_FEEDBACK_PIN), scanning_servo_min, scanning_servo_max, 20, 160) - newPos));
    
    } ;
    scanning_servo.detach();

  }



}


//Serial Input
void processCommand(String command) {


  String cmd = getValue(command, ':', 0);
  String val = getValue(command, ':', 1);
  log("process command " + cmd);
  if (cmd == MOTOR_1) {
    powerMotor(val.toInt(), 1);
  }
  else if (cmd == MOTOR_2) {
    powerMotor(val.toInt(), 2);
  }
  else if (cmd == STEERING_SERVO) {
    log("Steering servo");
    steerServo(val.toInt());
  }
  else if (cmd == SCANNING_SERVO) {
    log("scanning servo");
    scanServo(val.toInt());
  } else if (cmd == "PING") {
    sendCommand("PONG", millis());
  }

}

void sendCommand(String device, float value) {
  
  Serial1.print("{");
  Serial1.print(device);
  Serial1.print(":");
  Serial1.print(value);
  Serial1.print(":");
  Serial1.print(now());
  Serial1.print("}");
 
  Serial.print(device);
  Serial.print(":");
  Serial.println(value);
 
}




String getValue(String data, char separator, int index)
{
  int found = 0;
  int strIndex[] = { 0, -1 };
  int maxIndex = data.length() - 1;

  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}


void powerMotor(int val, int m) {

   if (m == 1) {

     SR2.motor(0);
     SR1.motor(val);
   } else {
     SR1.motor(0);
     SR2.motor(val);
   }


}

void log(String s) {
  Serial.println("DEBUG:" + s);
}