//this is the working sketch for wandering around 

#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <Servo.h>

SoftwareSerial SWSerial(NOT_A_PIN, 2); // RX on no pin (unused), TX on pin 11 (to S1).
SyRenSimplified SR(SWSerial); // Use SWSerial as the serial port.

Servo myservo;
int pos = 0;


//SONAR
int trigPin = 13;    // Trigger
int echoPin = 5;    // Echo
long duration, cm, inches;
unsigned long lastSonarPing;
unsigned long lastObsticalTimestamp;
boolean obstructed = false;


//SERIAL COM
String inputString = "";         // a String to hold incoming data
bool stringComplete = false;  // whether the string is complete

void setup()
{
  SWSerial.begin(9600);
   Serial.begin(9600);
     while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

   myservo.attach(9);
   

//    //Sonar Setup
   pinMode(trigPin, OUTPUT);
   pinMode(echoPin, INPUT);

inputString.reserve(200);
   
}

void loop()
{
  int power;

 if (stringComplete) {
    
    // clear the string:
    inputString = "";
    stringComplete = false;
  }
 
//SONAR LOOP

   if (millis() - lastSonarPing > 100) {
    digitalWrite(trigPin, LOW);
    delayMicroseconds(5);
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin, LOW);
    pinMode(echoPin, INPUT);
    duration = pulseIn(echoPin, HIGH);
    // Convert the time into a distance
    cm = (duration/2) / 29.1;     // Divide by 29.1 or multiply by 0.0343
 
 
    lastSonarPing = millis();

    if (! obstructed && cm < 5) {
        SR.motor(0);
        Serial.print("front_sonar,");
        Serial.println(cm);
   
        lastObsticalTimestamp = millis();
        obstructed = true;
    } else {
       if (obstructed && millis() - lastObsticalTimestamp > 250) {
// 
//          Serial.print("front_sonar,");
//          Serial.println(cm);
           obstructed = false;
           //SR.motor(10);
       }
    }
    
  

  }

 
//   for (pos = 0; pos <= 80; pos += 1) { // goes from 0 degrees to 180 degrees
//    // in steps of 1 degree
//    myservo.write(pos);              // tell servo to go to position in variable 'pos'
//    delay(15);                       // waits 15ms for the servo to reach the position
//   
//  }
// 
//  for (pos = 80; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
//    myservo.write(pos);              // tell servo to go to position in variable 'pos'
//    delay(15);                       // waits 15ms for the servo to reach the position
//     Serial.println(pos);
//  }
}

  void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag so the main loop can
    // do something about it:
    if (inChar == '\n') {
        processCommand(inputString);
      stringComplete = true;
    
    }
    
  }
 }

 void processCommand(String command) {

    Serial.print(inputString);
    String cmd = getValue(inputString, ':', 0);
    String val = getValue(inputString, ':', 1);
      Serial.print("command: " + cmd);
      Serial.println("value: " + val);

        if (cmd == "M1") {
            SR.motor(val.toInt());
        } 
        else 
        if (cmd == "S1") {
            steerServo(val.toInt());
        }
  
 }

 void steerServo(int newPos) {

  Serial.print("Steering ");
    Serial.print(pos);
      Serial.print(" to ");
        Serial.println(newPos);

  if (pos >= newPos) {

  for (pos = newPos; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
      Serial.print("Turning Right " ) ;
       Serial.println(pos);
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
    
  } else {

  for (pos; pos <= newPos; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
       Serial.print("Turning Left " ) ;
       Serial.println(pos);
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
    
  }



  
 }

 String getValue(String data, char separator, int index)
{
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;

    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}

  
  // Ramp from -127 to 127 (full reverse to full forward), waiting 20 ms (1/50th of a second) per value.
//  for (pos = 0; pos <= 80; pos += 1) { // goes from 0 degrees to 180 degrees
//    // in steps of 1 degree
//    myservo.write(pos);              // tell servo to go to position in variable 'pos'
//    delay(15);                       // waits 15ms for the servo to reach the position
//  }
////  for (power = -10; power <= 10; power ++)
////  {
////    SR.motor(power);
////   delay(20);
////  }
////
//  for (pos = 80; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
//    myservo.write(pos);              // tell servo to go to position in variable 'pos'
//    delay(15);                       // waits 15ms for the servo to reach the position
//  }
  
//  // Now go back the way we came.
//  for (power = 10; power >= -10; power --)
//  {
//    SR.motor(power);
//    delay(20);
//  }
//}
