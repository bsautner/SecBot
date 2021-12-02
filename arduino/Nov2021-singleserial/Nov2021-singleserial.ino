#include <RPLidar.h>
#include <Servo.h>
#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
const int MOTOR_PIN_1 = 2;
const int STEERING_SERVO_PIN = 3;
const int RPLIDAR_MOTOR = 8;

const int RPWM_Output = 5; // Arduino PWM output pin 5; connect to IBT-2 pin 1 (RPWM)
const int LPWM_Output = 6; // Arduino PWM output pin 6; connect to IBT-2 pin 2 (LPWM)
const int PIN_PING_PONG_LED = 50;
Adafruit_LSM303DLH_Mag_Unified mag = Adafruit_LSM303DLH_Mag_Unified(12345);


Servo steering_servo;
RPLidar lidar;

//const int rs = 13, en = 12, d4 = 11, d5 = 10, d6 = 9, d7 = 8;
//LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

String s = "";



void setup() {

  Serial.println("Hello World");

  pinMode(RPWM_Output, OUTPUT);
  pinMode(LPWM_Output, OUTPUT);


//  lcd.begin(20, 4);
//  // Print a message to the LCD.
//  lcd.print("hello, world!");


  Serial.begin(115200);
  Serial1.begin(115200);
  pinMode(PIN_PING_PONG_LED, OUTPUT);
  digitalWrite(PIN_PING_PONG_LED, HIGH);  
  lidar.begin(Serial3);
  
  pinMode(RPLIDAR_MOTOR, OUTPUT);

    if (!mag.begin()) {
    /* There was a problem detecting the LSM303 ... check your connections */
    Serial.println("Ooops, no LSM303 detected ... Check your wiring!");
    while (1)
      ;
  }

  steerServo(20);
  delay(500);
  steerServo(70);
  delay(500);
  steerServo(45);


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

//    // set the cursor to column 0, line 1
//  // (note: line 1 is the second row, since counting begins with 0):
//  lcd.setCursor(0, 1);
//  // print the number of seconds since reset:
//  lcd.print(millis() / 1000);

   readSerial();
   readLidar();
   orientationCheck();
   delay(100);
   
}

void readSerial() {
   if (Serial1.available() ) {
    char inChar = (char)Serial1.read();

    if (inChar == '\n') {
      //  lcd.setCursor(0, 3);
  // print the number of seconds since reset:
        //lcd.print(s);

      // Serial.println(s);
      if (s == "ping") {
        digitalWrite(PIN_PING_PONG_LED, HIGH);
        delay(100);
        digitalWrite(PIN_PING_PONG_LED, LOW);
        Serial1.println("pong");
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
      Serial1 << "LDR," << distance << "," << angle  << "," << startBit << ","  << quality << "\n" ;
      Serial << "LDR," << distance << "," << angle  << "," << startBit << ","  << quality << "\n" ;
  }
}
void orientationCheck() {
  
  sensors_event_t event;
  mag.getEvent(&event);
  float Pi = 3.14159;
  // Calculate the angle of the vector y,x
  float heading = (atan2(event.magnetic.y, event.magnetic.x) * 180) / Pi;
  // Normalize to 0-360
  if (heading < 0) {
    heading = 360 + heading;
  }
//   lcd.setCursor(0, 3);
//   lcd.print("H:");
//   lcd.print(heading);
     Serial1 << "MAG," << heading  << "\n" ;
 
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
