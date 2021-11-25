#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <RPLidar.h>

const int MOTOR_PIN_1 = 2;
SoftwareSerial SWSerial1(NOT_A_PIN, MOTOR_PIN_1);
SyRenSimplified SR1(SWSerial1);


//LIDAR
const int RPLIDAR_MOTOR = 4;
RPLidar lidar;

String s = "";

void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  SWSerial1.begin(9600);
  pinMode(53, OUTPUT);


  //LIDAR
    // bind the RPLIDAR driver to the arduino hardware serial
  lidar.begin(Serial3);
  // set pin modes
  pinMode(RPLIDAR_MOTOR, OUTPUT);
}

void loop() {

    
  if (Serial1.available() ) {
    char inChar = (char)Serial1.read();

    if (inChar == '\n') {

      // Serial.println(s);
      if (s == "ping") {
        digitalWrite(53, HIGH);   // turn the LED on (HIGH is the voltage level)
        delay(100);              // wait for a second
        digitalWrite(53, LOW);    // turn the LED off by making the voltage LOW
        
        Serial1.println("pong");
      }
      s = "";
    } else {
      s += inChar;
    }

  }

   readLidar();
   delay(100);

   
//  SR1.motor(128);
//  Serial.println(128);
//  delay(2000);
//  SR1.motor(135);
//  Serial.println(135);
//  delay(2000);
//  SR1.motor(128);
//  Serial.println(128);
//  delay(2000);
//  SR1.motor(120);
//  Serial.println(120);
//  delay(2000);
//  SR1.motor(0);
//  Serial.println(0);
//  delay(2000);
//  SR1.motor(255);
//  Serial.println(255);
//  delay(2000);



}


void readLidar() {
  if (IS_OK(lidar.waitPoint())) {
    float distance =
        lidar.getCurrentPoint().distance;        // distance value in mm unit
    float angle = lidar.getCurrentPoint().angle; // anglue value in degree
    bool startBit = lidar.getCurrentPoint()
                        .startBit; // whether this point is belong to a new scan
    byte quality = lidar.getCurrentPoint().quality; // quality of the current measurement

        Serial.println(distance);

//    JSONVar myObject;
//    myObject["device"] = LIDAR;
//    myObject["distance"] = distance;
//    myObject["startBit"] = startBit;
//    myObject["quality"] = quality;
//    myObject["angle"] = angle;
//    sendSensorData(myObject);

  } else {
    analogWrite(RPLIDAR_MOTOR, 0); // stop the rplidar motor

    // try to detect RPLIDAR...
    rplidar_response_device_info_t info;
    if (IS_OK(lidar.getDeviceInfo(info, 100))) {
      // detected...
      lidar.startScan();

  
      // start motor rotating at max allowed speed
      analogWrite(RPLIDAR_MOTOR, 255);
   
    }
  }
}

//
//   Serial.println("speeding up");
//  for (int i = 128; i < 255; i++) {
//     SR1.motor(i);
//     delay(100);
//
//  }
//    delay(1000);
//
//
//   SR1.motor(128);
//   Serial.println("stopped");
//   delay(1000);
//
//   Serial.println("backing up");
//  for (int i = 128; i > -1; i--) {
//     SR1.motor(i);
//     delay(20);
//  }
//    delay(1000);
