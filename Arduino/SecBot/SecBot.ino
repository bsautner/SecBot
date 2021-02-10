//this is the working sketch for wandering around

#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <Servo.h>

SoftwareSerial SWSerial(NOT_A_PIN, 2); // RX on no pin (unused), TX on pin 11 (to S1).
SyRenSimplified SR(SWSerial); // Use SWSerial as the serial port.

Servo myservo;


//SONAR
int trigPin = 13;    // Trigger
int echoPin = 5;    // Echo
int servoPin = 12;
long duration, cm, inches;
unsigned long lastSonarPing;
int lastSonarCm = 0;


//SERIAL COM
String inputString = "";         // a String to hold incoming data
bool stringComplete = false;  // whether the string is complete

bool isInit = false;

void setup()
{
  SWSerial.begin(9600);
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  


  //    //Sonar Setup
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  inputString.reserve(200);

}

void loop()
{
  if (! isInit) {
      SR.motor(0);
      steerServo(45);
      delay(2000);
      isInit = true;

  }
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
    cm = (duration / 2) / 29.1;   // Divide by 29.1 or multiply by 0.0343
  

    lastSonarPing = millis();
    if (cm > lastSonarCm +1 || cm < lastSonarCm - 1) {
           Serial.print("front_sonar:");
           Serial.println(cm);
           lastSonarCm = cm;
           if (cm < 5) {
              SR.motor(0);
           }

    }
  



  }
}

void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    inputString += inChar;
    if (inChar == '\n') {
      processCommand(inputString);
      stringComplete = true;

    }

  }
}

void processCommand(String command) {

   
  String cmd = getValue(inputString, ':', 0);
  String val = getValue(inputString, ':', 1);

  if (cmd == "M1") {
    powerMotor(val.toInt());
  }
  else if (cmd == "S1") {
    steerServo(val.toInt());
  }

}

void steerServo(int newPos) {

  myservo.attach(servoPin);
  delay(100);
  myservo.write(newPos);
  delay(100);
  myservo.detach();
  


}

void powerMotor(int val) {

  if (val < 0 && lastSonarCm < 5) {
    return;
  }
  else {
     SR.motor(val);
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
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}


 
