
String serialCommand = "";
String maunalInput = "";


void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  Serial2.begin(115200);
 
}

void loop() {
 
  if (Serial.available()) {
    char inChar = (char)Serial.read();
 
    if (inChar == '\n') {
      if (! maunalInput.startsWith("DEBUG:")) {
        Serial1.print(maunalInput);
      }

      maunalInput = "";
    } else {
         maunalInput += inChar;
    }
  }

  
  if (Serial2.available()) {
    char inChar = (char)Serial2.read();
   
    if (inChar == '\n') {
       processCommand(serialCommand);
      serialCommand = "";
    } else {
       serialCommand += inChar;
    }
  }

 
}


void processCommand(String command) {
   log(command);
// Serial.println("Processing Command: " + command);
}

void log(String s) { Serial.println("DEBUG:" + s); }
