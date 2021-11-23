#include <SoftwareSerial.h>
#include "SyRenSimplified.h"


const int MOTOR_PIN_1 = 2;
SoftwareSerial SWSerial1(NOT_A_PIN, MOTOR_PIN_1);
SyRenSimplified SR1(SWSerial1);

String s = "";

void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  SWSerial1.begin(9600);


}

void loop() {
   
  if (Serial1.available() ) {
    char inChar = (char)Serial1.read();
 
    if (inChar == '\n') {
      
       // Serial.println(s);
        if (s == "ping") {
         Serial1.println("pong");
         }
        s = "";
    } else {
         s += inChar;
    }
   
  }
    SR1.motor(128);
    Serial.println(128);
    delay(2000);
    SR1.motor(135);
    Serial.println(135);
    delay(2000);
    SR1.motor(128);
    Serial.println(128);
    delay(2000);
    SR1.motor(120);
    Serial.println(120);
    delay(2000);
    SR1.motor(0);
    Serial.println(0);
     delay(2000);
    SR1.motor(255);
    Serial.println(255);
    delay(2000);
  

  
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

 
   
  
