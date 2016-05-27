#include <Smartcar.h>
#include <Wire.h>
#include <Servo.h>
Odometer encoderLeft, encoderRight;
Gyroscope gyro(23);
Car car;
const int encoderLeftPin = 2;
const int encoderRightPin = 3;
int motorSpeed = 100; //100% of the max speed
boolean isRotate = false;
String a = "a";
String d = "d";
int currentAngle;
long lastUpdate = 0;
long prevEncoderDistance = 0;
long curEncoderDistance = 0;
long distance = 0;

void setup() {
  Serial.begin(9600);
  encoderLeft.attach(encoderLeftPin);
  encoderLeft.attach(encoderRightPin);
  encoderLeft.begin();
  encoderRight.begin();
  Serial.setTimeout(200);
  gyro.begin();
  car.begin();
  gyro.attach();
}

void(* resetFunc) (void) = 0; //declare reset function @ address 0

void loop() {
  handleInput();
  sendData();
  gyro.update();
}

void handleInput() { //handle serial input if there is any
  if (Serial.available()) {
    String input = Serial.readStringUntil('/');

    if (input.startsWith("w")) {
      isRotate = false;
      int throttle = input.substring(1).toInt(); // Example: recieve 'w100' = move forward with speed 100.
      if (throttle > 0 || throttle < 0) {
        car.setSpeed(throttle);
      } else {
        car.stop();
      }
    }else if (input.startsWith("r")){
      isRotate = true;
      int side = input.substring(1).toInt();      // Example: recieve 'r1' = rotate right
      rotateOnSpot(side);
    }else if (input.startsWith("a")){
      int deg = input.substring(1).toInt();      // Example: recieve 'a90' = turn 90 degrees
      car.setAngle(deg);
      gyro.update();
    }else if (input.startsWith("c")){
      Serial.end();
    }else if (input.startsWith("x")){
      resetFunc();
    }else{
      car.stop();
    }
  }
}

void rotateOnSpot(int orientation){
  /* Let's set opposite speed on each side of the car, so it rotates on spot */
  if (orientation == 1){ //positive value means we should rotate clockwise
    car.setMotorSpeed(motorSpeed, -motorSpeed); // left motors spin forward, right motors spin backward
  }else if(orientation == -1){ //rotate counter clockwise
    car.setMotorSpeed(-motorSpeed, motorSpeed); // left motors spin backward, right motors spin forward
  }else{
    car.setMotorSpeed(0,0); //stop rotation
  }
  gyro.update();
}

void sendData() {
  if (lastUpdate < millis() - 500) {
      updatedOdometry();
      Serial.print(d + distance);
      getCurrentAngle();
      lastUpdate = millis();
    }
}

void getCurrentAngle() {
   gyro.update();
   currentAngle = gyro.getAngularDisplacement();
   if(gyro.getAngularDisplacement() >= 360 || gyro.getAngularDisplacement() < 0){
      currentAngle = ((gyro.getAngularDisplacement() % 360) + 360) % 360;
   }
   Serial.println(a + currentAngle);
}

void updatedOdometry(){
    curEncoderDistance = ((encoderLeft.getDistance() + encoderRight.getDistance()) / 2);

    if(!isRotate)
    {
        distance += curEncoderDistance - prevEncoderDistance;
    }

    prevEncoderDistance = curEncoderDistance;
}
