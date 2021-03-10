//this is the working sketch for wandering around 

#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <Servo.h>

SoftwareSerial SWSerial(NOT_A_PIN, 2); // RX on no pin (unused), TX on pin 11 (to S1).
SyRenSimplified SR(SWSerial); // Use SWSerial as the serial port.
Servo myservo;
int pos = 0;


void setup()
{
  SWSerial.begin(9600);
  myservo.attach(9);
}

void loop()
{
  int power;

  myservo.write(0);  
  
  // Ramp from -127 to 127 (full reverse to full forward), waiting 20 ms (1/50th of a second) per value.
  for (pos = 0; pos <= 80; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  for (power = -10; power <= 10; power ++)
  {
   // SR.motor(power);
   //delay(20);
  }

  for (pos = 80; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  
  // Now go back the way we came.
  for (power = 10; power >= -10; power --)
  {
  //  SR.motor(power);
   // delay(20);
  }
}
