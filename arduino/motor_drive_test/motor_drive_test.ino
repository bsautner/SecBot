#include <Servo.h>
#include <Wire.h>
#include <SoftwareSerial.h>


const int RPWM2_Output = 7;  
const int LPWM2_Output = 6; 

const int RPWM1_Output = 5;  
const int LPWM1_Output = 4;  
const int STEERING_SERVO_PIN = 3;


const int PIN_PING_PONG_LED = 50;

const int speedModifier = 50;
const int maxForward = 512 + speedModifier;
const int maxReverse = 512 - speedModifier;

Servo steering_servo;

int lastSteer = 0;

void setup() {
    Serial.begin(115200);
      steering_servo.attach(STEERING_SERVO_PIN);
steerServo(20);
delay(1000);
steerServo(70);
delay(1000);
steerServo(45);
delay(1000);
}

void loop() {
  
 Serial.println("1 F");
drive(maxForward, 1);

delay(2000);
stop();
delay(2000);
Serial.println("1 R");
drive(maxReverse, 1);

delay(2000);
stop();
delay(2000);

Serial.println("2 F");
drive(maxForward, 2);


delay(2000);
stop();
delay(2000);
Serial.println("2 R");
drive(maxReverse, 2);

delay(2000);
stop();
delay(2000);


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


void steerServo(int newPos) {
  //  log("steerServo");
  if (lastSteer != newPos) {
    if (newPos <= 70 && newPos >= 20) {
       steering_servo.attach(STEERING_SERVO_PIN);
      delay(10);
      lastSteer = newPos;
   //   Serial << "Steering " << newPos;
      steering_servo.write(newPos);

    }
  }
}


void stop() {
//   steering_servo.detach();

  analogWrite(LPWM1_Output, 0);
  analogWrite(RPWM1_Output, 0);
    analogWrite(LPWM2_Output, 0);
  analogWrite(RPWM2_Output, 0);
}
