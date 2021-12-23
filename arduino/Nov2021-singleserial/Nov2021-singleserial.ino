#include <RPLidar.h>
#include <Servo.h>
#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>
#include <SoftwareSerial.h>
#include <SharpIR.h>

const int SCANNING_IR_PIN = A0;
const int RPLIDAR_MOTOR = 8;

const int RPWM2_Output = 7;  
const int LPWM2_Output = 6; 

const int RPWM1_Output = 5;  
const int LPWM1_Output = 4;  
const int STEERING_SERVO_PIN = 3;

const int PIN_PING_PONG_LED = 50;

const int speedModifier = 150;
const int maxForward = 512 + speedModifier;
const int maxReverse = 512 - speedModifier;

SharpIR IR_prox(SharpIR::GP2Y0A02YK0F, SCANNING_IR_PIN);
Servo steering_servo;
int lastSteer = 0;
RPLidar lidar;
String s = "";

long scanning_ir_duration;

long scanning_ir_last_cm = 0;

boolean pingLedOn = false;

void setup() {
  
  pinMode(SCANNING_IR_PIN, INPUT);
  pinMode(PIN_PING_PONG_LED, OUTPUT);
  digitalWrite(PIN_PING_PONG_LED, HIGH);

  
  steering_servo.attach(STEERING_SERVO_PIN);

  pinMode(RPWM1_Output, OUTPUT);
  pinMode(LPWM1_Output, OUTPUT);
  pinMode(RPWM2_Output, OUTPUT);
  pinMode(LPWM2_Output, OUTPUT);


  Serial.begin(115200);
  Serial1.begin(115200);
 
  Serial1.println("PONG");
  lidar.begin(Serial3);

  pinMode(RPLIDAR_MOTOR, OUTPUT);




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


 // steering_servo.write(45);
  delay(1000);
    stop();
  digitalWrite(PIN_PING_PONG_LED, LOW);
}

void loop() {

 
  readSerial();
  readLidar();
  readForwardIR();

delay(10);


}

void readForwardIR() {
  
    int scanning_ir_cm = IR_prox.getDistance();

     if (scanning_ir_cm != scanning_ir_last_cm &&
         scanning_ir_cm != scanning_ir_last_cm - 1 &&
         scanning_ir_cm != scanning_ir_last_cm + 1) {
      // sendSensorData(SCANNING_IR, scanning_ir_cm);
       scanning_ir_last_cm = scanning_ir_cm;
       Serial1 << "FORWARD_IR," <<  scanning_ir_cm << "\n";
       Serial << "FORWARD_IR," <<  scanning_ir_cm << "\n";
     }
}

void readSerial() {


  while (Serial1.available() ) {
    char inChar = (char)Serial1.read();

    if (inChar == '\n') {
      Serial.println(s);
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
       else if (s == "MOTOR,STOP") {
        stop();
         steerServo(45);
      }
      else if (s == "MOTOR,FORWARD") {
        drive(maxForward, 1);
        steerServo(45);
      }
      else if (s == "MOTOR,RIGHT_FORWARD") {
        drive(maxForward, 1);
        steerServo(70);
      }
        else if (s == "MOTOR,LEFT_FORWARD") {
          drive(maxForward, 1);
          steerServo(20);
          
      }
        else if (s == "MOTOR,LEFT_REVERSE") {
         drive(maxReverse, 1);
         steerServo(20);
      }
        else if (s == "MOTOR,RIGHT_REVERSE") {
        drive(maxReverse, 1);
        steerServo(70);
      }
        else if (s == "MOTOR,REVERSE") {
        drive(maxReverse, 1);
        steerServo(45);
      }
      else {
        Serial.println(s);
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
     // Serial << "LDR," << angle << "," << distance  << "," << startBit << ","  << quality << "\n" ;
    }
  }
}



void steerServo(int newPos) {
  //  log("steerServo");
  if (lastSteer != newPos) {
    if (newPos <= 70 && newPos >= 20) {
       steering_servo.attach(STEERING_SERVO_PIN);
      delay(10);
      lastSteer = newPos;
      Serial << "Steering " << newPos;
      steering_servo.write(newPos);

    }
  }
}


void drive(int s, int m) {



  if (s < 512)
  {
    // reverse rotation
    int reversePWM = -(s - 511) / 2;
    if (m == 1) {
    analogWrite(LPWM1_Output, 0);
    analogWrite(RPWM1_Output, reversePWM);
    } else {
          analogWrite(LPWM2_Output, 0);
    analogWrite(RPWM2_Output, reversePWM);
    }
  }
  else
  {
    // forward rotation
    int forwardPWM = (s - 512) / 2;
    if (m == 1) {
    analogWrite(RPWM1_Output, 0);
    analogWrite(LPWM1_Output, forwardPWM);
    } else {
        analogWrite(RPWM2_Output, 0);
    analogWrite(LPWM2_Output, forwardPWM);
    }
  }

}

void stop() {
   steering_servo.detach();

  analogWrite(LPWM1_Output, 0);
  analogWrite(RPWM1_Output, 0);
   analogWrite(LPWM2_Output, 0);
  analogWrite(RPWM2_Output, 0);
}


template <typename T>
Print& operator<<(Print& printer, T value)
{
  printer.print(value);
  return printer;
}
