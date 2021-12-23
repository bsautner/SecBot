#include <RPLidar.h>
#include <Servo.h>
#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>
#include <SoftwareSerial.h> 

const int MOTOR_PIN_1 = 2;
const int STEERING_SERVO_PIN = 3;
const int RPLIDAR_MOTOR = 8;

const int RPWM_Output = 5; // Arduino PWM output pin 5; connect to IBT-2 pin 1 (RPWM)
const int LPWM_Output = 6; // Arduino PWM output pin 6; connect to IBT-2 pin 2 (LPWM)
const int PIN_PING_PONG_LED = 50;


Servo steering_servo;
RPLidar lidar;
String s = "";

boolean pingLedOn = false;

void setup() {
 

  pinMode(RPWM_Output, OUTPUT);
  pinMode(LPWM_Output, OUTPUT);
 

  Serial.begin(115200);
  Serial1.begin(115200);
  pinMode(PIN_PING_PONG_LED, OUTPUT);
  digitalWrite(PIN_PING_PONG_LED, HIGH);
  Serial1.println("PONG");
  lidar.begin(Serial3);

  pinMode(RPLIDAR_MOTOR, OUTPUT);
  //
  //
  //  steerServo(20);
  //  delay(1000);
  //  steerServo(70);
  //  delay(1000);
  //  steerServo(45);


  Serial.println("stopping lidar");
  analogWrite(RPLIDAR_MOTOR, 0); // stop the rplidar motor

  // try to detect RPLIDAR...
  rplidar_response_device_info_t info;
  if (IS_OK(lidar.getDeviceInfo(info, 100))) {
    // detected...
    lidar.startScan();
    Serial.println("starting lidar");


    // start motor rotating at max allowed speed
    analogWrite(RPLIDAR_MOTOR, 255);
    delay(1000);
  }


  digitalWrite(PIN_PING_PONG_LED, LOW);
}

void loop() {


  readSerial();
 // readLidar();

  //delay(100);

}

void readSerial() {

    
  while (Serial1.available() ) {
    char inChar = (char)Serial1.read();
   
    if (inChar == '\n') {
  //  Serial.println(s);
      if (s == "PING") {
         if (pingLedOn) {
              digitalWrite(PIN_PING_PONG_LED, LOW);
              pingLedOn = false;
         }
         else {
              digitalWrite(PIN_PING_PONG_LED, HIGH);
              pingLedOn = true;
         }
         Serial1.println("PONG");
      }
      else if (s = "STEER,RIGHT") {
        steerServo(20);
      }
        else if (s = "STEER,LEFT") {
        steerServo(70);
      }
         else if (s = "STEER,FORWARD") {
        steerServo(45);
      }
      else {
       // Serial.println("unhandled serial input $s);
      }
      s = "";
    } else {
      s += inChar;
    }

  }

}



void readLidar() {
  if (IS_OK(lidar.waitPoint())) {
    float distance = lidar.getCurrentPoint().distance;        // distance value in mm unit
    float angle = lidar.getCurrentPoint().angle; // anglue value in degree
    bool startBit = lidar.getCurrentPoint().startBit; // whether this point is belong to a new scan
    byte quality = lidar.getCurrentPoint().quality; // quality of the current measurement
    if (! startBit && quality >= 15) {
      Serial1 << "LDR," << angle << "," << distance  << "," << startBit << ","  << quality << "\n" ;
      Serial << "LDR," << angle << "," << distance  << "," << startBit << ","  << quality << "\n" ;
    }
  }
}



void steerServo(int newPos) {
  //  log("steerServo");
  if (newPos <= 70 && newPos >= 20) {
    steering_servo.attach(STEERING_SERVO_PIN);
    delay(100);
    steering_servo.write(newPos);
    delay(1000);
    steering_servo.detach();
  }
}

void drive(int s) {



  if (s < 512)
  {
    // reverse rotation
    int reversePWM = -(s - 511) / 2;
    analogWrite(LPWM_Output, 0);
    analogWrite(RPWM_Output, reversePWM);
  }
  else
  {
    // forward rotation
    int forwardPWM = (s - 512) / 2;
    analogWrite(RPWM_Output, 0);
    analogWrite(LPWM_Output, forwardPWM);
  }

}

void stop() {
  analogWrite(LPWM_Output, 0);
  analogWrite(RPWM_Output, 0);
}


template <typename T>
Print& operator<<(Print& printer, T value)
{
  printer.print(value);
  return printer;
}
