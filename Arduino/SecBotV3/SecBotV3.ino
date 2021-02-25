#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <Servo.h>


const int MOTOR_PIN_1 = 2;
const int MOTOR_PIN_2 = 3;

const int SONAR_ECHO_PIN = 5;
const int SONAR_TRIGGER_PIN = 6;
const int STEERING_SERVO_PIN = 12;



SoftwareSerial SWSerial1(NOT_A_PIN, MOTOR_PIN_1);
SyRenSimplified SR1(SWSerial1);


SoftwareSerial SWSerial2(NOT_A_PIN, MOTOR_PIN_2);
SyRenSimplified SR2(SWSerial2);


void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  SWSerial1.begin(9600);
  SWSerial2.begin(9600);
  powerMotor(0, 1);
  powerMotor(0, 2);
}

void loop() {
  
    powerMotor(10, 1);
    delay(1000);

      powerMotor(0, 1);
    delay(1000);

      powerMotor(10, 2);
    delay(1000);

      powerMotor(0, 2);
    delay(1000);


       powerMotor(-10, 1);
    delay(1000);

      powerMotor(0, 1);
    delay(1000);

      powerMotor(-10, 2);
    delay(1000);

      powerMotor(0, 2);
    
                              


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
