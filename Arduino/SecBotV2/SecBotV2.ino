#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <Servo.h>
#include <SharpIR.h>    //IR sensor library

 #define model 20150 


 
const int LIDAR_PIN = A0;

const int MOTOR_PIN_1 = 2;
const int MOTOR_PIN_2 = 3;
const int SONAR_ECHO_PIN = 5;
const int SONAR_TRIGGER_PIN = 6;
const int STEERING_SERVO_PIN = 12;
const int DISTANCE_SCAN_SERVO_PIN = 13;


SharpIR IR_prox(LIDAR_PIN,model);  //define the sensor
 


const String MOTOR_1 = "MOTOR_1";
const String MOTOR_2 = "MOTOR_2";
const String STEERING_SERVO = "STEERING_SERVO";
const String FRONT_SONAR = "FRONT_SONAR";
const String COMPASS_HEADING = "COMPASS_HEADING";
const String ACCELEROMETER_X = "ACCELEROMETER_X";
const String ACCELEROMETER_Y = "ACCELEROMETER_Y";
const String ACCELEROMETER_Z = "ACCELEROMETER_Z";
const String DEBUG = "DEBUG";

Servo steering_servo;
Servo scanning_servo;



Adafruit_LSM303DLH_Mag_Unified mag = Adafruit_LSM303DLH_Mag_Unified(12345);

SoftwareSerial SWSerial1(NOT_A_PIN, MOTOR_PIN_1);
SyRenSimplified SR1(SWSerial1);


SoftwareSerial SWSerial2(NOT_A_PIN, MOTOR_PIN_2);
SyRenSimplified SR2(SWSerial2);


long duration, cm, inches, lastSonarPing;
int lastCm = 0;


String inputString = "";
bool stringComplete = false;

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

  pinMode(LIDAR_PIN, INPUT);
  pinMode(SONAR_TRIGGER_PIN, OUTPUT);
  pinMode(SONAR_ECHO_PIN, INPUT);

  mag.enableAutoRange(true);

  if (!mag.begin()) {
    /* There was a problem detecting the LSM303 ... check your connections */
    Serial.println("Ooops, no LSM303 detected ... Check your wiring!");
    while (1)
      ;
  }



  
  powerMotor(0, 1);
  powerMotor(0, 2);

}

void loop() {
  if (stringComplete) {
    inputString = "";
    stringComplete = false;
  }

  if (Serial1.available()) {
     char inChar = (char)Serial1.read();
    inputString += inChar;
    if (inChar == '\n') {
      processCommand(inputString);
      stringComplete = true;

    }
  }

  sonarPing();
  orientationCheck();


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

void sonarPing() {
  //SONAR LOOP

  if (millis() - lastSonarPing > 100) {


    digitalWrite(SONAR_TRIGGER_PIN, LOW);
    delayMicroseconds(5);
    digitalWrite(SONAR_TRIGGER_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(SONAR_TRIGGER_PIN, LOW);
    pinMode(SONAR_ECHO_PIN, INPUT);
    duration = pulseIn(SONAR_ECHO_PIN, HIGH);
    // Convert the time into a distance
    cm = (duration / 2) / 29.1;   // Divide by 29.1 or multiply by 0.0343

    int l=IR_prox.distance(); 
    Serial.println(l);
    lastSonarPing = millis();
  
    if (cm < 1000 && cm != lastCm && cm != lastCm - 1 && cm != lastCm + 1) {
      sendCommand(FRONT_SONAR, cm);
      lastCm = cm;
      if (cm < 5) {
        powerMotor(0, 1);
        powerMotor(0, 2);
      }

    }




  }
}

void steerServo(int newPos) {
  log("steerServo");
  if (newPos <= 70 && newPos >= 20) {
    steering_servo.attach(STEERING_SERVO_PIN);
    delay(100);
    steering_servo.write(newPos);
    delay(100);
    steering_servo.detach();

  }



}




//Serial Input
void processCommand(String command) {

  
  String cmd = getValue(inputString, ':', 0);
  String val = getValue(inputString, ':', 1);

  if (cmd == MOTOR_1) {
    powerMotor(val.toInt(), 1);
  }
  else if (cmd == MOTOR_2) {
    powerMotor(val.toInt(), 2);
  }
  else if (cmd == STEERING_SERVO) {
    log("Steering servo");
    steerServo(val.toInt());
  } else if (cmd == "PING") {
    sendCommand("PONG", millis());
  }

}

void sendCommand(String device, float value) {
  Serial1.print("{");
  Serial1.print(device);
  Serial1.print(":");
  Serial1.print(value);
  Serial1.print("}");
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
