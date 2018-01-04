int red = 7;
int blue = 8;
int yellow = 9;

void setup() {
  // put your setup code here, to run once:
  pinMode(red,OUTPUT);
  pinMode(blue,OUTPUT);
  pinMode(yellow,OUTPUT);

}

void loop() {
  // put your main code here, to run repeatedly:
  digitalWrite(red,HIGH);
  digitalWrite(blue,HIGH);
  digitalWrite(yellow,HIGH);
  delay(1000);  
  digitalWrite(red,LOW);
  digitalWrite(blue,LOW);
  digitalWrite(yellow,LOW);
}
