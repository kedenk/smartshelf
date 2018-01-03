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


void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
 
  Serial.print("] ");
  for (int i=0;i<length;i++) {
    Serial.print((char)payload[i]);
  }
  readJeson(payload);
  Serial.println();
}

EthernetClient ethClient;
PubSubClient client(ethClient);
void readJeson(const char json[]){
   //char json[] = ;
   StaticJsonBuffer<200> jsonBuffer;
   JsonObject& root = jsonBuffer.parseObject(json); 
   if (!root.success()) {
    Serial.println("parseObject() failed");
    return;
  }
  int boxid = root["boxid"];
  int color = root["color"];
  Serial.print("boxid : ");
  Serial.println(boxid);
  Serial.print("color : ");
  Serial.println(color);
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
  
  //sendWeights();

delay(1000);

}
