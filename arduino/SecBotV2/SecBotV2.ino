#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

#include <Arduino_JSON.h>
#include <RPLidar.h>
#include <Servo.h>
#include <SharpIR.h>
#include <SoftwareSerial.h>
#include <SyRenSimplified.h>

#define model SharpIR::GP2Y0A02YK

const int SCANNING_IR_PIN = A0;
const int SCANNING_SERVO_FEEDBACK_PIN = A8;

const int MOTOR_PIN_1 = 2;
const int MOTOR_PIN_2 = 3;
const int RPLIDAR_MOTOR = 4;
const int FRONT_SONAR_ECHO_PIN = 5;
const int FRONT_SONAR_TRIGGER_PIN = 6;
const int SCANNING_SONAR_ECHO_PIN = 8;
const int SCANNING_SONAR_TRIGGER_PIN = 9;
const int SCANNING_SERVO_PIN = 11;
const int STEERING_SERVO_PIN = 12;
const int DISTANCE_SCAN_SERVO_PIN = 12;

SharpIR IR_prox(SharpIR::GP2Y0A02YK0F, SCANNING_IR_PIN);
RPLidar lidar;

const String COMPASS = "COMPASS";
const String LIDAR = "LIDAR";
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

int scanning_servo_pos = 0; // variable to store the servo position
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

String input_string = "";
String serial_sensor_input_string = "";
String serial_device_input_string = "";

int throttle = 0;
int lastHeading = 0;
int accelerometer_x = 0;
int accelerometer_y = 0;
int accelerometer_z = 0;

void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  Serial2.begin(115200);

  // bind the RPLIDAR driver to the arduino hardware serial
  lidar.begin(Serial3);

  // set pin modes
  pinMode(RPLIDAR_MOTOR, OUTPUT);

  SWSerial1.begin(9600);
  SWSerial2.begin(9600);

  input_string.reserve(200);
  serial_sensor_input_string.reserve(200);
  serial_device_input_string.reserve(200);

  steering_servo.attach(STEERING_SERVO_PIN);
  steering_servo.write(40);
  steering_servo.detach();

  // powerMotor(0, 0);
  // powerMotor(0, 1);
  // calibrateScanningServo();

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

  if (Serial2.available()) {
    char inChar = (char)Serial2.read();
    serial_device_input_string += inChar;
    if (inChar == '\n') {
  //    processCommand(serial_device_input_string);
      serial_device_input_string = "";
    }
  }

  if (Serial1.available()) {
    char inChar = (char)Serial1.read();
    serial_sensor_input_string += inChar;
    if (inChar == '\n') {
   //   processCommand(serial_sensor_input_string);
      serial_sensor_input_string = "";
    }
  }

  if (Serial.available()) {
    char inChar = (char)Serial.read();
    input_string += inChar;
    if (inChar == '\n') {
      //  processCommand(serial_input_string);
      Serial.println(input_string);
      input_string = "";
    }
  }


  readLidar();
  orientationCheck();
}

void calibrateScanningServo() {
 // log("Calibrating Scanning Servo");
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

void readSensors() {
  // SONAR LOOP


  // if (millis() - control_loop_timestamp > 100) {
  //   orientationCheck();

  //   digitalWrite(FRONT_SONAR_TRIGGER_PIN, LOW);
  //   delayMicroseconds(5);
  //   digitalWrite(FRONT_SONAR_TRIGGER_PIN, HIGH);
  //   delayMicroseconds(10);
  //   digitalWrite(FRONT_SONAR_TRIGGER_PIN, LOW);
  //   pinMode(FRONT_SONAR_ECHO_PIN, INPUT);
  //   front_sonar_duration = pulseIn(FRONT_SONAR_ECHO_PIN, HIGH);
  //   int front_sonar_cm = (front_sonar_duration / 2) / 29.1;

  //   if (front_sonar_cm < 1000 && front_sonar_cm != front_sonar_last_cm &&
  //       front_sonar_cm != front_sonar_last_cm - 1 &&
  //       front_sonar_cm != front_sonar_last_cm + 1) {
  //     sendSensorData(FRONT_SONAR, front_sonar_cm);
  //     front_sonar_last_cm = front_sonar_cm;
  //     if (front_sonar_cm < 5) {
  //       powerMotor(0, 1);
  //       powerMotor(0, 2);
  //     }
  //   }
  //   delay(100);

  //   digitalWrite(SCANNING_SONAR_TRIGGER_PIN, LOW);
  //   delayMicroseconds(5);
  //   digitalWrite(SCANNING_SONAR_TRIGGER_PIN, HIGH);
  //   delayMicroseconds(10);
  //   digitalWrite(SCANNING_SONAR_TRIGGER_PIN, LOW);
  //   pinMode(SCANNING_SONAR_ECHO_PIN, INPUT);
  //   scanning_sonar_duration = pulseIn(SCANNING_SONAR_ECHO_PIN, HIGH);
  //   int scanning_sonar_cm = (scanning_sonar_duration / 2) / 29.1;

  //   if (scanning_sonar_cm != scanning_sonar_last_cm &&
  //       scanning_sonar_cm != scanning_sonar_last_cm - 1 &&
  //       scanning_sonar_cm != scanning_sonar_last_cm + 1) {
  //     //sendSensorData(SCANNING_SONAR, scanning_sonar_cm);
  //     scanning_sonar_last_cm = scanning_sonar_cm;
  //   }

  //   int scanning_ir_cm = IR_prox.getDistance();

  //   if (scanning_ir_cm != scanning_ir_last_cm &&
  //       scanning_ir_cm != scanning_ir_last_cm - 1 &&
  //       scanning_ir_cm != scanning_ir_last_cm + 1) {
  //    // sendSensorData(SCANNING_IR, scanning_ir_cm);
  //     scanning_ir_last_cm = scanning_ir_cm;
  //   }

    
  //   control_loop_timestamp = millis();
  // }
}

void readLidar() {
  if (IS_OK(lidar.waitPoint())) {
      float distance =
          lidar.getCurrentPoint().distance;        // distance value in mm unit
      float angle = lidar.getCurrentPoint().angle; // anglue value in degree
      bool startBit =
          lidar.getCurrentPoint()
              .startBit; // whether this point is belong to a new scan
      byte quality =
          lidar.getCurrentPoint().quality; // quality of the current measurement

      JSONVar myObject;
      myObject["device"] =LIDAR;
      myObject["distance"] = distance;
      myObject["startBit"] = startBit;
      myObject["quality"] = quality;
      myObject["angle"] = angle;
      sendSensorData(myObject);

    } else {
      analogWrite(RPLIDAR_MOTOR, 0); // stop the rplidar motor

      // try to detect RPLIDAR...
      rplidar_response_device_info_t info;
      if (IS_OK(lidar.getDeviceInfo(info, 100))) {
        // detected...
        lidar.startScan();

        // start motor rotating at max allowed speed
        analogWrite(RPLIDAR_MOTOR, 255);
        delay(1000);
      }
    }
}
void orientationCheck() {
  // compass

  sensors_event_t event;
  mag.getEvent(&event);
   
  float Pi = 3.14159;

  // Calculate the angle of the vector y,x
  float heading = (atan2(event.magnetic.y, event.magnetic.x) * 180) / Pi;

  // Normalize to 0-360
  if (heading < 0) {
    heading = 360 + heading;
  }

  //  if (heading > lastHeading + 10 || heading < lastHeading - 10) {
  lastHeading = heading;
 
 

      JSONVar myObject;
      myObject["device"] = COMPASS;
      myObject["x"] =event.acceleration.x;
      myObject["y"] = event.acceleration.y;
      myObject["z"] = event.acceleration.z;
      myObject["heading"] = heading;
      sendSensorData(myObject);
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
    log(map(analogRead(SCANNING_SERVO_FEEDBACK_PIN), scanning_servo_min,
            scanning_servo_max, 20, 160));
    while (abs(map(analogRead(SCANNING_SERVO_FEEDBACK_PIN), scanning_servo_min,
                   scanning_servo_max, 20, 160) -
               newPos) > scanning_servo_tolerance) {
      log(abs(map(analogRead(SCANNING_SERVO_FEEDBACK_PIN), scanning_servo_min,
                  scanning_servo_max, 20, 160) -
              newPos));
    };
    scanning_servo.detach();
  }
}

// Serial Input
void processCommand(String command) {

//  log(command);

  JSONVar myObject = JSON.parse(command);
  if (JSON.typeof(myObject) == "undefined") {
    Serial.println("Parsing input failed!");
    return;
  }

  if (myObject.hasOwnProperty("control") && myObject.hasOwnProperty("value")) {

    // String cmd = (const char *)myObject["control"];
    // String val = (const char *)myObject["value"];
    // if (cmd == MOTOR_1) {
    //   powerMotor(val.toInt(), 1);
    // } else if (cmd == MOTOR_2) {
    //   powerMotor(val.toInt(), 2);
    // } else if (cmd == STEERING_SERVO) {
    //   log("Steering servo");
    //   steerServo(val.toInt());
    // } else if (cmd == SCANNING_SERVO) {
    //   log("scanning servo");
    //   scanServo(val.toInt());
    // } else if (cmd == "PING") {
    //   log("sending pong");
    //   sendSensorData("PONG", millis());
    // }
  }
}

void sendSensorData(JSONVar myObject) {

  String jsonString = JSON.stringify(myObject);
  Serial.println(jsonString);
  Serial1.println(jsonString);
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

void log(String s) { Serial.println("DEBUG:" + s); }
