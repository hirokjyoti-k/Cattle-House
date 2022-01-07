#include <SPI.h>
#include <LoRa.h>

#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "cattle-house-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "O2UfH2h85hzEdtxzhK3IUM7ANEFcX82CQU9sbIFh"

#define WIFI_SSID "BSNL-NETWORK"
#define WIFI_PASSWORD "DC2020MCA0021"

#define ss 15
#define rst 16
#define dio0 2

void setup() {
  Serial.begin(9600); 

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
      
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  LoRa.setPins(ss, rst, dio0);
  if (!LoRa.begin(433E6)){
    Serial.println("Starting LoRa failed!");
    while (1);
  }
}

void loop() {
  // try to parse packet
  int packetSize = LoRa.parsePacket();
  if (packetSize) {
    
    // received a packet
    Serial.print("Received packet '");
    
    // read packet
    String raw_data = "";
    while (LoRa.available()) {
      raw_data += (char)LoRa.read();
    }
    Serial.print("Latitude: "); Serial.print(getValue(raw_data,',',1));
    Serial.print("Longitude: "); Serial.print(getValue(raw_data,',',2));
    Serial.print("Speed: "); Serial.print(getValue(raw_data,',',3));
    Serial.print("Temperature: "); Serial.print(getValue(raw_data,',',4));
    Serial.print("BPM: "); Serial.print(getValue(raw_data,',',5));
    // print RSSI of packet
    Serial.print("' with RSSI "); Serial.println(LoRa.packetRssi());

    //Upload to firebase
    Firebase.setFloat("cattles/CT001/Latitude",getValue(raw_data,',',1).toFloat());
    Firebase.setFloat("cattles/CT001/Longitude",getValue(raw_data,',',2).toFloat());
    Firebase.setFloat("cattles/CT001/Temperature",getValue(raw_data,',',4).toFloat());
    Firebase.setFloat("cattles/CT001/HeartRate",getValue(raw_data,',',5).toFloat());
    if(getValue(raw_data,',',3).toFloat() > 2){
      Firebase.setString("cattles/CT001/Movement","Moving");
    }
    else{
      Firebase.setString("cattles/CT001/Movement","Standing");
    }
   }
}


String getValue(String data, char separator, int index)
{
  int found = 0;
  int strIndex[] = {0, -1};
  int maxIndex = data.length()-1;

  for(int i=0; i<=maxIndex && found<=index; i++){
    if(data.charAt(i)==separator || i==maxIndex){
      found++;
      strIndex[0] = strIndex[1]+1;
      strIndex[1] = (i == maxIndex) ? i+1 : i;
    }
  }
  return found>index ? data.substring(strIndex[0], strIndex[1]) : "";
}
