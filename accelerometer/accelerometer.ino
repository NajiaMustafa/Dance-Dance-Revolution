#include <SoftwareSerial.h>
SoftwareSerial mySerial(5, 6); // RX, TXs
byte START = 0b00000000;
byte ACC = 0b11111111;
byte ERR = 0b00000101;
byte OVER = 0b00000110;
byte ID = 1;
byte s;
// these constants describe the pins. They won't change:
const int groundpin = 18;             // analog input pin 4 -- ground
const int powerpin = 19;              // analog input pin 5 -- voltage
const int zpin = A2;                  // z-axis (only on 3-axis models)
#define calc(x) (0.01*x) - 5
#define WAITINGTOSTART 1
#define READACCELEROMETERVALUES 2
double minimum;//need the biggest negative value :)
int state = WAITINGTOSTART;
bool c = true;
double acc[10];
int count;
void setup() {
  // initialize the serial communications:
  mySerial.begin(9600);
  pinMode(groundpin, OUTPUT);
  pinMode(powerpin, OUTPUT);
  digitalWrite(groundpin, LOW);
  digitalWrite(powerpin, HIGH);
}
void checking() {
  while (mySerial.available() > 0) {
    byte s = mySerial.read();
    if (s == 0b00000000) state = READACCELEROMETERVALUES;//enter the run the game state
  }
}
void loop() {
  if (state == WAITINGTOSTART){
    checking();
  }
  else{
  switch (state) {
      case READACCELEROMETERVALUES: {
          if (count >= 10) {
            count = 0;// reset to 0 when array full
          }
          acc[count] = calc(analogRead(zpin));// an array to save the latest 10 values
          minimum = acc[0];//store the first value as a minimum to have soemthing to compare with
          count++;
        }
    }
  if (mySerial.available() > 0) {//if data available
    s = mySerial.read();//keep reading
    if (s == ACC) {// if it is a message to send data enter the send accelerometer message state (guard condition)
      for (int i = 0; i < count; i++) {
        if (acc[i] < minimum) minimum = acc[i];
      }
      if (minimum < -1) mySerial.write(0b10000011);// if hit hard enough
      else mySerial.write(0b10000100);
      state = READACCELEROMETERVALUES;// go back to reading accelerometer value state
    }
  else if (s == ERR || s == OVER) {// if game is over or timeout
    state = WAITINGTOSTART;// go bacl to waiting to start state
  }
  }
  }
}
