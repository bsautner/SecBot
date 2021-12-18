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
  Serial.begin(115200);
  Serial1.begin(115200);

}

void loop() {
  // put your main code here, to run repeatedly:

}
