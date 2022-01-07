#include <TinyGPS++.h>
#include <SoftwareSerial.h>

#define USE_ARDUINO_INTERRUPTS false
#include <PulseSensorPlayground.h> 
                         
PulseSensorPlayground pulseSensor;

//Temprature DHT
#include <DHTesp.h>
DHTesp dht;

//LORA HEADER FILE
#include <SPI.h>
#include <LoRa.h>

//Lora Custom Pins
#define ss 15
#define rst 16
#define dio0 2

TinyGPSPlus gps;
SoftwareSerial gpsSerial(D1, D2);

//  Variables
const int PulseWire = 0;
const int LED13 = 13;
int Threshold = 550;   

double latitude,longitude;
int speeds, temperature, BPM;

//Final data for LoRa
String raw_data;

void setup() {
  Serial.begin(9600);
  gpsSerial.begin(9600);
  dht.setup(D3, DHTesp::DHT11);

  pulseSensor.analogInput(PulseWire);   
  pulseSensor.blinkOnPulse(LED13); 
  pulseSensor.setThreshold(Threshold);   
  pulseSensor.begin();
  
  LoRa.setPins(ss, rst, dio0);
  if (!LoRa.begin(433E6)) {
    Serial.println("Starting LoRa failed!");
    while (1);
  }
}

void loop() {
getData();
}


void getData(){

  if (pulseSensor.sawNewSample()) {
    BPM = pulseSensor.getBeatsPerMinute();
  }
  
  while (gpsSerial.available() > 0){
      // get the byte data from the GPS
      byte gpsData = gpsSerial.read();
      gps.encode(gpsData);
      latitude = gps.location.lat();
      longitude = gps.location.lng();
      speeds = gps.speed.kmph();

      if (gps.time.isUpdated()){
        Serial.print("Time: "); Serial.print(gps.time.value());
        raw_data = "CT001,";
        raw_data.concat(String(latitude,7));
        raw_data.concat(",");
        raw_data.concat(String(longitude,7));
        raw_data.concat(",");
        raw_data.concat(speeds);
        raw_data.concat(",");
        raw_data.concat(dht.getTemperature());
        raw_data.concat(",");
        raw_data.concat(BPM);
        sendLoraData();     
       }
    }
  }

void sendLoraData(){
  Serial.print(" Sending packet: ");
  Serial.println(raw_data);
  LoRa.beginPacket();
  LoRa.print(raw_data);
  LoRa.endPacket();
  delay(5000);
}
