/*
 Basic MQTT example
 This sketch demonstrates the basic capabilities of the library.
 It connects to an MQTT server then:
  - publishes "hello world" to the topic "outTopic"
  - subscribes to the topic "inTopic", printing out any messages
    it receives. NB - it assumes the received payloads are strings not binary
 It will reconnect to the server if the connection is lost using a blocking
 reconnect function. See the 'mqtt_reconnect_nonblocking' example for how to
 achieve the same result without blocking the main loop.
 
*/
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
IPAddress server(127, 0, 0, 1);
char rootTopic[] = "shelf/drawer/{%d}";
char outTopic[20];
char sensorVal[50];
float sensorData[4] = {0.0,0.0,0.0,0.0};
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i=0;i<length;i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
}

EthernetClient ethClient;
PubSubClient client(ethClient);
void readSensors(){
  sensorData[0] = scale_d1.get_units();
  sensorData[1] = scale_d2.get_units();
  sensorData[2] = scale_d3.get_units();
  sensorData[3] = scale_d4.get_units();
  
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("arduinoClient")) {
      Serial.println("connected");
      readSensors();
      // Once connected, publish an announcement...
      for (int i = 1;i<=3;i++){
        sprintf(outTopic,rootTopic,i);
       float tempval = sensorData[i] ;
       sprintf(sensorVal,"%f",tempval);
        client.publish(outTopic,sensorVal);
      }
     
     
      client.subscribe("inTopic");
    } else {
      
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      
      
      // Wait 5 seconds before retrying
      delay(50000);
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
  Serial.begin(57600);
  client.setServer(server, 1883);
  client.setCallback(callback);

  Ethernet.begin(mac, ip);
  // Allow the hardware to sort itself out
  delay(1500);
}

void loop()
{
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}
