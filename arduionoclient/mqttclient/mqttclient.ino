#include <ArduinoJson.h>
#include "HX711.h"
#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>
//reding sensors:initializing pins
#define calibration_factor 208 //This value is obtained using the SparkFun_HX711_Calibration sketch

#define DOUT_D1  3
#define DOUT_D2  4
#define DOUT_D3  5
#define DOUT_D4  6
#define CLK  2

HX711 scale_d1(DOUT_D1, CLK);
HX711 scale_d2(DOUT_D2, CLK);
HX711 scale_d3(DOUT_D3, CLK);
HX711 scale_d4(DOUT_D4, CLK);

// Update these with values suitable for your network.
byte mac[]    = {  0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED };
IPAddress ip(172, 16, 0, 100);
IPAddress server(172, 16, 0, 105);
char rootTopic[] = "devices/weight/{%d}";
char outTopic[20];
float sensorData[] = {0.0,0.0,0.0,0.0};

int ledOut[] = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
//Drawer1 7-9,Drawer2 10-12,Drawer3 13,15, Drawer4 16-18 


void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  if( strcmp(topic,"devices/blink/start") == 0) {
      Serial.println("devices/blink/start");
      int *ledVal;
      ledVal =  readJeson(payload);
      
      //Serial.println(ledVal[0]);
     // Serial.println(ledVal[1]);
    // call drawer to switch on the light
      drawerOn(ledVal[0],ledVal[1]);
    
  }
  if( strcmp(topic,"devices/blink/stop") == 0) {
      Serial.println("devices/blink/stop");
      int *ledVal;
      ledVal =  readJeson(payload);
      //Call drawer method to switch off the light 
      Serial.println(ledVal[0]);
      Serial.println(ledVal[1]);
    
  }
 
 
  /*Serial.print("] ");
  for (int i=0;i<length;i++) {
    Serial.print((char)payload[i]);
  }*/
 
  //Serial.println();
}

EthernetClient ethClient;
PubSubClient client(ethClient);
void ledOn(int color, int pin){
   switch(color){
        case 0 :
          Serial.println("red");
          Serial.println(pin);
          break;
          
        case 1:
          Serial.print("blue");
          Serial.println(pin);
          break;
        case 2:
          Serial.print("yellow");
          Serial.println(pin);
          break;
        default:
          Serial.print("Led Number is out of bound") ;  
      }
}
void drawerOn(int boxNumber, int color){
  switch(boxNumber){
    case 0:
      Serial.println("boxOne Glowerd");
      ledOn(color, 7);// 0th box start at pin 7
      break;
    case 1 :
     Serial.println("boxTwo Glowerd");
     ledOn(color, 10);// 1th box start at 10;
     break;
    case 2:
     Serial.println("boxThree Glowerd");
     ledOn(color, 13);// 2nd box start at 13
     break;
    case 3:
      Serial.println("boxFour Glowerd");
      ledOn(color, 16);//3rd box start at 16
      break;
    default:
     Serial.print("No box found");
  }
}
int* readJeson(const char json[]){
   //char json[] = ;
   StaticJsonBuffer<200> jsonBuffer;
   JsonObject& root = jsonBuffer.parseObject(json); 
   if (!root.success()) {
    Serial.println("parseObject() failed");
    return;
  }
  static int r [] = {0,0};
  r[0] = root["boxid"];
  r[1] = root["color"];
  Serial.print("boxid : ");
  Serial.println(r[0]);
  Serial.print("color : ");
  Serial.println(r[1]);
  return r;
  }
void readSensors(){
  sensorData[0] = scale_d1.get_units();
  sensorData[1] = scale_d2.get_units();
  sensorData[2] = scale_d3.get_units();
  sensorData[3] = scale_d4.get_units();
  
}
void subscribe(){
   client.subscribe("inTopic");
   client.subscribe("devices/blink/start");
   client.subscribe("devices/blink/stop");
   }
void sendWeights() {
  readSensors();
      // Once connected, publish an announcement...
      for (int i = 0;i<=3;i++){
        sprintf(outTopic,rootTopic,i);
        float tempval = sensorData[i] ;
        String sf (tempval, 2);
        const char* sensorVal;
        sensorVal = sf.c_str();
        client.publish(outTopic, sensorVal);
        Serial.println(outTopic);
        Serial.println(sensorVal);
       
  
      }
}
void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    
    if (client.connect("arduinoClient")) {
      Serial.println("connected");
     
      
     
      subscribe();
     
      
    } else {
      
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      
      
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
  
}

void setup()
{
  scale_d1.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d1.tare(); //Assuming there is no weight on the scale at start up, reset the scale to 0
  scale_d2.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d2.tare();
  scale_d3.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d3.tare();
  scale_d4.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale_d4.tare();
  // Set up jeson buffer 
 
  Serial.begin(57600);
  client.setServer(server, 1883);
  client.setCallback(callback);

  Ethernet.begin(mac, ip);
  // Allow the hardware to sort itself out
  delay(1500);

  //for testing jeson
 

 
}

void loop()
{
  if (!client.connected()) {
    reconnect();
   
  }
  client.loop();
  
  sendWeights();

delay(5000);

}
