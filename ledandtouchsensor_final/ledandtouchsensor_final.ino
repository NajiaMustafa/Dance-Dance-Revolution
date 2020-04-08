#include <SoftwareSerial.h>
SoftwareSerial mySerial(6, 5); // RX, TX
const int RtouchSensor = A0;//the piezo is connected to analog pin 0
const int BtouchSensor = A1;
const int GtouchSensor = A3;
const int threshold = 10;  //threshold value to decide when the detected touch is recognized or not
bool fake = true;
#define RGB_red 11
#define RGB_green 10
#define RGB_blue 9
#define WAITINGTOSTART 1
#define WAITINGFORLED 2
#define WAITINGFORTACTILESENSOR 3
#define POINT 4
#define WAITINGFORACCELEROMETER 5
byte ID = 0;
byte START_easy = 0b00000000;
byte START_hard = 0b00001000;
byte HARDHIT = 0b10000011;
byte WEAKHIT = 0b10000100;
//variables that will change:
double h;
double y;
double z;
int w = 0;
int g = 0;
int r;
int state = WAITINGTOSTART;
long time_now;
int time_to_hit;
int readfunction(int c) {
  int y;
  long time_now = millis();
  while (millis() < time_now + time_to_hit) {
    y = analogRead(c);
    if (y >= threshold) {
      return y;
    }
  }
  return -1;// no hit so return -1
}
class LedStage {
  public: void start() {
      while (g == r) {
        r = random(3);//randomly light up any LED
      }
      if (r == 0) {
        g = 0;
        digitalWrite(RGB_red, LOW);//switch on the RED LED (LOW as common anode)
        digitalWrite(RGB_green, HIGH);//to switch off the Green LED
        digitalWrite(RGB_blue, HIGH);//to switch off the Blue LED
        digitalWrite(LED_BUILTIN, LOW);
      }
      else if (r == 1) {
        g = 1;
        digitalWrite(RGB_red, HIGH);//to switch off the blue LED
        digitalWrite(RGB_green, LOW);//to switch the green LED ON (common anode)
        digitalWrite(RGB_blue, HIGH);//to switch off the blue LED
        digitalWrite(LED_BUILTIN, LOW);
      }
      else if (r == 2) {
        g = 2;
        digitalWrite(RGB_red, HIGH);//to switch off the red LED
        digitalWrite(RGB_green, HIGH);//to switch off the green LED
        digitalWrite(RGB_blue, LOW);//to light up blue LED (common anode)
        digitalWrite(LED_BUILTIN, LOW);
      }
    }
};
class TactileSensor {
  public: int start() {
      int y;
      if (r == 0) {
        y = readfunction(RtouchSensor);
      }
      else if (r == 1) {
        y = readfunction(GtouchSensor);
      }
      else if (r == 2) {
        y = readfunction(BtouchSensor);
      }
      return y;
    }
};
byte accelerometer() {
  byte s;
  long timenow = millis();
  while (millis() < timenow + 5000) {//time out so it doesnot block
    if (mySerial.available() > 0) {//check if accelerometer sent anything
      s = mySerial.read();
      return s;
    }
  }
  return 0;//if nothing was sent just return 0
}
LedStage LED;
TactileSensor T;
void setup() {
  // Make all of our LED pins outputs:
  pinMode(RGB_red, OUTPUT);
  pinMode(RGB_green, OUTPUT);
  pinMode(RGB_blue, OUTPUT);
  mySerial.begin(9600);//initialize Serial Monitor
}//end setup()

void loop() {
  if (mySerial.available() > 0) {
    byte s = mySerial.read();
    if (s == START_easy) {
      time_to_hit = 2000;// give the player 2 seconds to hit the right spot
      state = WAITINGFORLED;
    }
    else if (s == START_hard){
      time_to_hit = 1000;// give the player 1 second to hit the right spot
      state = WAITINGFORLED;
    }
  }
  while (state != WAITINGTOSTART) {
    switch (state) {
      case WAITINGFORLED: {
          LED.start();// turn on LED (action)
          state = WAITINGFORTACTILESENSOR; //no guard condition
        }
        break;
      case WAITINGFORTACTILESENSOR: {
          int y;
          y = T.start();//wait for tactile sensor to detect hit
          if ( y != -1) {//if there was a hit on the right spot enter the accelerometer state (guard condition)
            state = WAITINGFORACCELEROMETER;
          }
          else {
            mySerial.write(0b00000010);// no or wrong spot was hit
            w++;//increment the mistake counter
            if (w == 3){// if three mistakes are made
               mySerial.write(0b00000110); // Game over:(
               w = 0;
               state = WAITINGTOSTART;
            }
            state = WAITINGFORLED; //wrong hit
          }
        }
        break;
      case WAITINGFORACCELEROMETER: {
          mySerial.write(0b00000001);//write that the right spot was hit (indicating that the force of the hit needs to be 
          if (fake) {
            mySerial.write(0b00000100);
            state = WAITINGFORLED;
          }
          else {
            mySerial.write(0b00000011);
            state = POINT;
          }
          fake = !fake;
        }
        break;
      case POINT: {
          mySerial.write(0b00000111);//action
          state = WAITINGFORLED;//no guard condition
        }
        break;
    }
  }
}
