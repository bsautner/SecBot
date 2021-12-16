#include <Adafruit_LSM303DLH_Mag.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

#define OLED_RESET  16  // Pin 15 -RESET digital signal

#define LOGO16_GLCD_HEIGHT 16
#define LOGO16_GLCD_WIDTH  16

Adafruit_LSM303DLH_Mag_Unified mag = Adafruit_LSM303DLH_Mag_Unified(12345);

float minval = 360;
float maxval = -1;

int i = 0;
int dir = 0;

void setup(void) {
  Serial.begin(115200);  


//  Serial.println("Magnetomeeter Test!");


 // Serial.println("Deg     X     Y     Z     Heading     ");
  
  /* Initialise the sensor */
  if (!mag.begin()) {
    /* There was a problem detecting the LSM303 ... check your connections */
 //   Serial.println("problem...");
    while (1)
      ;
  }
}

void loop(void) {
 

  sensors_event_t event;
  mag.getEvent(&event);

  float x = 0.00;
  float y = 0.00;
  float z = 0.00;

  float Pi = 3.14159;

//08:44:35.774 -> Mag Minimums: -82.09  -91.18  -122.65
////08:44:35.774 -> Mag Maximums: 3.55  43.55  0.00
//
//08:55:02.678 -> Mag Minimums: -9.73  -7.27  -126.63
//08:55:02.678 -> Mag Maximums: 0.00  0.00  0.00
//
//09:19:36.507 -> Mag Minimums: -67.09  -103.00  -106.84
//09:19:36.507 -> Mag Maximums: 47.73  71.00  4.49

//Mag Minimums: -69.36  -89.18  -130.61
//Mag Maximums: 57.00  44.73  47.76

float rawLowX = -69.36;
float rawHighX = 57.00;
float referenceRangeX = 360;
float referenceLowX = 0;
float rawRangeX = rawHighX - rawLowX;

float rawLowY = -89.18;
float rawHighY =44.73;
float referenceRangeY = 360;
float referenceLowY = 0;
float rawRangeY = rawHighY - rawLowY;

float rawLowZ = -130.61;
float rawHighZ = 47.46;
float referenceRangeZ = 360;
float referenceLowZ = 0;
float rawRangeZ = rawHighZ - rawLowZ;

//(((RawValue – RawLow) * ReferenceRange) / RawRange) + ReferenceLow
  x = (((event.magnetic.x - rawLowX) * 360) / rawRangeX) + referenceLowX;
  y = (((event.magnetic.y - rawLowY) * 360) / rawRangeY) + referenceLowY;;
  z = (((event.magnetic.z - rawLowZ) * 360) / rawRangeZ) +  referenceLowZ;;


  float heading = (atan2(y, x) * 180) / Pi;
//
//  // Normalize to 0-360
//  if (heading < 0) {
//    heading = 360 + heading;
//  }

  if (heading > maxval) {
    maxval = heading;
  }
  if (heading < minval) {
    minval = heading;
  }
  Serial.print(heading);
  Serial.println();

  delay(100);
}
