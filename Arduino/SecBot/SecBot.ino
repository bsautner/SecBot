//this is the working sketch for wandering around
#include <Adafruit_LSM303_Accel.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

#include <SoftwareSerial.h>
#include <SyRenSimplified.h>
#include <Servo.h>

SoftwareSerial SWSerial(NOT_A_PIN, 2); // RX on no pin (unused), TX on pin 11 (to S1).
SyRenSimplified SR(SWSerial); // Use SWSerial as the serial port.

/* compass / accel sensor Assign a unique ID to this sensor at the same time */
Adafruit_LSM303_Accel_Unified accel = Adafruit_LSM303_Accel_Unified(54321);
Servo myservo;


//SONAR
int trigPin = 13;    // Trigger
int echoPin = 5;    // Echo
int servoPin = 12;
long duration, cm, inches;
unsigned long lastSonarPing;

int lastSonarCm = 0;
int throttle = 0;
int lastHeading = 0;
int accelerometer_x = 0;
int accelerometer_y = 0;
int accelerometer_z = 0;


//SERIAL COM
String inputString = "";         // a String to hold incoming data
bool stringComplete = false;  // whether the string is complete

bool isInit = false;

void setup()
{
  SWSerial.begin(9600);
  Serial.begin(115200);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }




  //    //Sonar Setup
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  inputString.reserve(200);

  //compass
#ifndef ESP8266
  while (!Serial)
    ; // will pause Zero, Leonardo, etc until serial console opens
#endif

  log("Accelerometer Test");


  /* Initialise the sensor */
  if (!accel.begin()) {
    /* There was a problem detecting the ADXL345 ... check your connections */
    log("no LSM303 detected");
    while (1)
      ;
  }

  displaySensorDetails();
  accel.setRange(LSM303_RANGE_4G);
  log("Range set to: ");
  lsm303_accel_range_t new_range = accel.getRange();
  switch (new_range) {
    case LSM303_RANGE_2G:
      log("+- 2G");
      break;
    case LSM303_RANGE_4G:
      log("+- 4G");
      break;
    case LSM303_RANGE_8G:
      log("+- 8G");
      break;
    case LSM303_RANGE_16G:
      log("+- 16G");
      break;
  }


  accel.setMode(LSM303_MODE_NORMAL);
  log("Mode set to: ");
  lsm303_accel_mode_t new_mode = accel.getMode();
  switch (new_mode) {
    case LSM303_MODE_NORMAL:
      log("Normal");
      break;
    case LSM303_MODE_LOW_POWER:
      log("Low Power");
      break;
    case LSM303_MODE_HIGH_RESOLUTION:
      log("High Resolution");
      break;
  }


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
    if (cm < 1000 && (cm > lastSonarCm + 1 || cm < lastSonarCm - 1)) {

      Serial.print("front_sonar:");
      Serial.println(cm);
      lastSonarCm = cm;
      if (cm < 5) {
        SR.motor(0);
      }

    }




  }

  //compass


  if (millis() - throttle > 500) {
    throttle = millis();
    sensors_event_t event;
    accel.getEvent(&event);

    /* Display the results (acceleration is measured in m/s^2) */

    if (accelerometer_x > accelerometer_x + 1 || event.acceleration.x < accelerometer_x - 1) {
      accelerometer_x = accelerometer_x;
      Serial.print("accelerometer_x:");
      Serial.println(event.acceleration.x);
    }
    if (accelerometer_y > accelerometer_y + 1 || event.acceleration.y < accelerometer_y - 1) {
      accelerometer_y = accelerometer_y;
      Serial.print("accelerometer_y:");
      Serial.println(event.acceleration.y);
    }
    if (accelerometer_z > accelerometer_z + 1 || event.acceleration.x < accelerometer_z - 1) {
      accelerometer_z = accelerometer_z;
      Serial.print("accelerometer_z:");
      Serial.println(event.acceleration.z);
    }


    float Pi = 3.14159;

    // Calculate the angle of the vector y,x
    float heading = (atan2(event.magnetic.y, event.magnetic.x) * 180) / Pi;

    // Normalize to 0-360
    if (heading < 0)
    {
      heading = 360 + heading;
    }

    if (heading > lastHeading + 3 || heading < lastHeading - 3) {
      lastHeading = heading;

      Serial.print("compass_heading:");
      Serial.println(heading);
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


//compass
void displaySensorDetails(void) {
  sensor_t sensor;
  accel.getSensor(&sensor);
  Serial.println("------------------------------------");
  Serial.print("Sensor:       ");
  Serial.println(sensor.name);
  Serial.print("Driver Ver:   ");
  Serial.println(sensor.version);
  Serial.print("Unique ID:    ");
  Serial.println(sensor.sensor_id);
  Serial.print("Max Value:    ");
  Serial.print(sensor.max_value);
  Serial.println(" m/s^2");
  Serial.print("Min Value:    ");
  Serial.print(sensor.min_value);
  Serial.println(" m/s^2");
  Serial.print("Resolution:   ");
  Serial.print(sensor.resolution);
  Serial.println(" m/s^2");
  Serial.println("------------------------------------");
  Serial.println("");
  delay(500);
}

void log(String s) {
  Serial.println("DEBUG:" + s);
}
