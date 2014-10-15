import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Home extends PApplet {

/* 
 * Home (for Mac/PC) - \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0430 \u0434\u043b\u044f \u0443\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f \u0443\u043c\u043d\u044b\u043c \u0434\u043e\u043c\u043e\u043c.
 * \u0412\u044b\u043f\u0443\u0449\u0435\u043d\u0430 \u0434\u043b\u044f \u0447\u0430\u0441\u0442\u043d\u043e\u0433\u043e \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f, \u0432\u0441\u0435 \u043f\u0440\u0430\u0432\u0430 \u043d\u0430 \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0443 \u043f\u0440\u0438\u043d\u0430\u0434\u043b\u0435\u0436\u0430\u0442 AB.
 * (byte) 2014 \u0412\u0441\u0435 \u043f\u0440\u0430\u0432\u0430 \u0437\u0430\u0449\u0438\u0449\u0435\u043d\u044b \u0431\u0438\u0442\u043e\u0439. 
 */

 // \u0438\u043c\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u043c \u0431\u0438\u0431\u043b\u0438\u043e\u0442\u0435\u0447\u043a\u0443 \u0434\u043b\u044f \u0434\u0430\u043d\u043d\u044b\u0445 \u0441 \u043f\u043e\u0440\u0442\u043e\u0432

Serial port; // \u0441\u043e\u0437\u0434\u0430\u0435\u043c \u043f\u043e\u0440\u0442

PFont font; // \u0441\u043e\u0437\u0434\u0430\u0435\u043c \u0448\u0440\u0438\u0444\u0442

PImage colorsPic;

String outLight = ""; // \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435 \u0432\u043d\u0435\u0448\u043d\u0435\u0433\u043e \u0441\u0432\u0435\u0442\u0430
String temperatureIn = ""; // \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0430 \u0432 \u043a\u043e\u043c\u043d\u0430\u0442\u0435
String temperatureOut = ""; // \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0430 \u043d\u0430 \u0443\u043b\u0438\u0446\u0435
String data = ""; // \u0432\u0445\u043e\u0434\u044f\u0449\u0435\u0435 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435
int index = 0; // \u0438\u043d\u0434\u0435\u043a\u0441 "," \u0432 \u0432\u0445\u043e\u0434\u044f\u0449\u0435\u043c \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f
int indexTwo = 0; // \u0438\u043d\u0434\u0435\u043a\u0441 "/" \u0432 \u0432\u0445\u043e\u0434\u044f\u0449\u0435\u043c \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f

int greenBubble = color(137, 213, 72); // \u0446\u0432\u0435\u0442 \u0437\u0435\u043b\u0435\u043d\u043e\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f
int redBubble = color(219, 105, 90); // \u0446\u0432\u0435\u0442 \u043a\u0440\u0430\u0441\u043d\u043e\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f
int blueBubble = color(93, 136, 241); // \u0446\u0432\u0435\u0442 \u0441\u0438\u043d\u0435\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f
int yellowBubble = color(250, 185, 80); // \u0446\u0432\u0435\u0442 \u0436\u0435\u043b\u0442\u043e\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f
int backgroundColor = color(179, 220, 255); // \u0446\u0432\u0435\u0442 \u0444\u043e\u043d\u0430
int redStrokeBubble = color(255, 179, 191); // \u0446\u0432\u0435\u0442 \u043e\u0431\u0432\u043e\u0434\u043a\u0438 \u043a\u0440\u0430\u0441\u043d\u043e\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f
int blueStrokeBubble = color(170, 194, 238); // \u0446\u0432\u0435\u0442 \u043e\u0431\u0432\u043e\u0434\u043a\u0438 \u0441\u0438\u043d\u0435\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f
int greenStrokeBubble = color(174, 255, 182); // \u0446\u0432\u0435\u0442 \u043e\u0431\u0432\u043e\u0434\u043a\u0438 \u0437\u0435\u043b\u0435\u043d\u043e\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f
int yellowStrokeBubble = color(255, 226, 186); // \u0446\u0432\u0435\u0442 \u043e\u0431\u0432\u043e\u0434\u043a\u0438 \u0436\u0435\u043b\u0442\u043e\u0433\u043e \u043f\u0443\u0437\u044b\u0440\u044f

int dayInfoBubbleX = 100; // \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u043f\u0443\u0437\u044b\u0440\u044f \u0441 \u043f\u0440\u043e\u0446\u0435\u043d\u0442\u043e\u043c \u0434\u043d\u044f
int dayInfoBubbleY = 300;

int redLED = 255; // \u043a\u0440\u0430\u0441\u043d\u044b\u0439 \u0441\u0432\u0435\u0442\u043e\u0434\u0438\u043e\u0434 \u043b\u0435\u043d\u0442\u044b
int greenLED = 255; // \u0437\u0435\u043b\u0435\u043d\u044b\u0439 \u0441\u0432\u0435\u0442\u043e\u0434\u0438\u043e\u0434 \u043b\u0435\u043d\u0442\u044b
int blueLED = 255; // \u0441\u0438\u043d\u0438\u0439 \u0441\u0432\u0435\u0442\u043e\u0434\u0438\u043e\u0434 \u043b\u0435\u043d\u0442\u044b
float brightnessRGB = 0.7f; // \u044f\u0440\u043a\u043e\u0441\u0442\u044c \u0446\u0432\u0435\u0442\u043d\u043e\u0439 \u043b\u0435\u043d\u0442\u044b

float brightnessWhiteLED = 0; // \u044f\u0440\u043a\u043e\u0441\u0442\u044c \u0431\u0435\u043b\u043e\u0439 \u043b\u0435\u043d\u0442\u044b

int colorIn; // \u0446\u0432\u0435\u0442 \u0446\u0432\u0435\u0442\u043d\u043e\u0439 \u043b\u0435\u043d\u0442\u044b

boolean isStartedLight = false;

///////////////////////////////////////////////\u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430//////////////////////////////////////////////
public void setup() {
  
  size (816, 460); // \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u044f\u0435\u043c \u0440\u0430\u0437\u043c\u0435\u0440 \u043e\u043a\u043e\u0448\u043a\u0430
  background(179, 220, 255); // \u0437\u0430\u043a\u0440\u0430\u0448\u0438\u0432\u0430\u0435\u043c \u0444\u043e\u043d
  
  port = new Serial(this, Serial.list()[2], 9600); // \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0430\u0435\u043c \u043f\u043e\u0440\u0442
  port.bufferUntil(';'); // \u0433\u0440\u0443\u0437\u0438\u043c \u0434\u043e ';'
  
  font = loadFont("Helvetica-Bold.vlw"); // \u0433\u0440\u0443\u0437\u0438\u043c \u0448\u0440\u0438\u0444\u0442
  
  stroke(255, 255, 255); // \u0443\u0441\u0442\u0430\u043d\u0430\u0432\u043b\u0438\u0432\u0430\u0435\u043c \u0431\u0435\u043b\u0443\u044e \u043e\u0431\u0432\u043e\u0434\u043a\u0443 
  strokeWeight(3); // \u0443\u0441\u0442\u0430\u043d\u0430\u0432\u043b\u0438\u0432\u0430\u0435\u043c \u0436\u0438\u0440\u043d\u043e\u0441\u0442\u044c \u043e\u0431\u0432\u043e\u0434\u043a\u0438
  
  colorsPic = loadImage("Colors.png");
  
}

////////////////////////////////////////\u0440\u0438\u0441\u0443\u0435\u043c \u043e\u0431\u044a\u0435\u043a\u0442\u044b////////////////////////////////////////////////
public void draw() {
 
  // \u0445\u0440\u0435\u043d\u043e\u0432\u0438\u043d\u0430, \u043a\u043e\u0442\u043e\u0440\u0430\u044f \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u043e\u0437\u0432\u043e\u043b\u044f\u0442\u044c \u043f\u0435\u0440\u0435\u0442\u0430\u0441\u043a\u0438\u0432\u0430\u0442\u044c \u043f\u0443\u0437\u044b\u0440\u0438\u043a\u0438, \u043d\u043e \u043e\u043d\u0430 \u043d\u0438\u0445\u0440\u0435\u043d\u0430 \u043d\u0435 \u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442 \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e
  /*if(mousePressed && (((mouseX - dayInfoBubbleX)*(mouseX - dayInfoBubbleX)) + ((mouseY - dayInfoBubbleY)*(mouseY - dayInfoBubbleY)) < 100*100)) {
    
    background(backgroundColor);
    lightAndDayBubbleDraw(mouseX, mouseY, 235, true);
    dayInfoBubbleX = mouseX;
    dayInfoBubbleY = mouseY;
  
  }*/
  
  temperatureBubbleDraw(130, 100, PApplet.parseInt(temperatureOut), true, blueBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u043e\u0439 \u043d\u0430 \u0443\u043b\u0438\u0446\u0435
  temperatureBubbleDraw(300, 330, PApplet.parseInt(temperatureIn), false, redBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u043e\u0439 \u0432 \u043a\u043e\u043c\u043d\u0430\u0442\u0435
  
  dateBubbleDraw(340, 130, true, blueBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441 \u0442\u0435\u043a\u0443\u0449\u0435\u0439 \u0434\u0430\u0442\u043e\u0439
  dateBubbleDraw(490, 330, false, greenBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e\u043c \u0434\u043d\u0435\u0439 \u0434\u043e \u0441\u043e\u043b\u043d\u0446\u0435\u0441\u0442\u043e\u044f\u043d\u0438\u044f
  
  lightAndDayBubbleDraw(100, 300, 0, true, yellowBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441 \u043f\u0440\u043e\u0446\u0435\u043d\u0442\u043e\u043c \u0434\u043d\u044f
  lightAndDayBubbleDraw(500, 100, PApplet.parseInt(outLight), false, redBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441\u043e \u0441\u0432\u0435\u0442\u043e\u043c \u0432 \u043a\u043e\u043c\u043d\u0430\u0442\u0435
  
  rgbAndWhiteLightBubbleDraw(700, 300, true, yellowBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441 \u0446\u0432\u0435\u0442\u043d\u043e\u0439 \u043f\u043e\u0434\u0441\u0432\u0435\u0442\u043a\u043e\u0439
  rgbAndWhiteLightBubbleDraw(730, 100, false, blueBubble); // \u0440\u0438\u0441\u0443\u0435\u043c \u043f\u0443\u0437\u044b\u0440\u044c \u0441 \u0431\u0435\u043b\u043e\u0439 \u043f\u043e\u0434\u0441\u0432\u0435\u0442\u043a\u043e\u0439
 
  if (!isStartedLight) {
    
    startingLight();
    
  }
  
  port.write("R" + redLED + "G" + greenLED + "B" + blueLED + "W" + brightnessWhiteLED*255 + ";"); // \u0437\u0430\u0436\u0438\u0433\u0430\u0435\u043c \u043d\u0443\u0436\u043d\u044b\u0435 \u0446\u0432\u0435\u0442\u0430 \u043d\u0430 \u043f\u043e\u0434\u0441\u0432\u0435\u0442\u043a\u0430\u0445
  
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u0435\u0439 \u0441 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u043e\u0439/////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////  
public void temperatureBubbleDraw(int bubbleX, int bubbleY, int temperature, boolean isOut, int colorBubble) { // \u043f\u0443\u0437\u044b\u0440\u0438 \u0441 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u043e\u0439
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);
    
  ellipse(bubbleX, bubbleY, 157, 157);
  
////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u044f \u0441 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u043e\u0439 \u043d\u0430 \u0443\u043b\u0438\u0446\u0435//////////////////////////////////
  if (isOut) {
    fill(255, 255, 255);
    textFont(font, 20);
    text("Outdoor", bubbleX - 52, bubbleY - 37);
    text("temperature", bubbleX - 50, bubbleY - 20);
    textFont(font, 65);
    if (temperature > 9) {
      text(temperature + "\u00ba", bubbleX - 45, bubbleY + 40);
    } else { 
      text(temperature + "\u00ba", bubbleX - 25, bubbleY + 40);
    }
  } else { 
////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u044f \u0441 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u043e\u0439 \u0432 \u043a\u043e\u043c\u043d\u0430\u0442\u0435//////////////////////////////////  
    fill(255, 255, 255);
    textFont(font, 20);
    text("Indoor", bubbleX - 52, bubbleY - 37);
    text("temperature", bubbleX - 50, bubbleY - 20);
    textFont(font, 65);
    if (temperature > 9) {
      text(temperature + "\u00ba", bubbleX - 45, bubbleY + 40);
    } else { 
      text(temperature + "\u00ba", bubbleX - 25, bubbleY + 40);
    }
  }
  
  stroke(255, 255, 255);
    
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u0435\u0439-\u043a\u0430\u043b\u0435\u043d\u0434\u0430\u0440\u0435\u0439/////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
public void dateBubbleDraw(int bubbleX, int bubbleY, boolean isSimple, int colorBubble) { 
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);

//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u044f \u0441 \u0442\u0435\u043a\u0443\u0449\u0435\u0439 \u0434\u0430\u0442\u043e\u0439////////////////////////////////////////  
  if (isSimple) { 
    
    ellipse(bubbleX, bubbleY, 130, 130);
    
    fill(255, 255, 255);
    textFont(font, 65);
    if (day() > 9) {
      text(day(), bubbleX - 38, bubbleY + 10);
    } else {
      text(day(), bubbleX - 19, bubbleY + 10);
    }
    textFont(font, 20);
    switch(month()) {
      case 1: 
        text("january", bubbleX - 50, bubbleY + 29);
        break;
      case 2: 
        text("february", bubbleX - 50, bubbleY + 29);
        break;
      case 3: 
        text("march", bubbleX - 50, bubbleY + 29);
        break;
      case 4: 
        text("april", bubbleX - 50, bubbleY + 29);
        break;
      case 5: 
        text("may", bubbleX - 50, bubbleY + 29);
        break;
      case 6: 
        text("june", bubbleX - 50, bubbleY + 29);
        break;
      case 7: 
        text("july", bubbleX - 50, bubbleY + 29);
        break;
      case 8: 
        text("august", bubbleX - 50, bubbleY + 29);
        break;
      case 9: 
        text("september", bubbleX - 50, bubbleY + 29);
        break;
      case 10: 
        text("october", bubbleX - 37, bubbleY + 29);
        break;
      case 11: 
        text("november", bubbleX - 50, bubbleY + 29);
        break;
      case 12: 
        text("december", bubbleX - 50, bubbleY + 29);
        break;  
      default:
        break;
    }
    
  } else { 
//////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u044f \u0441 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e\u043c \u0434\u043d\u0435\u0439 \u0434\u043e \u0441\u043e\u043b\u043d\u0446\u0435\u0441\u0442\u043e\u044f\u043d\u0438\u044f///////////////////////////     
    ellipse(bubbleX, bubbleY, 170, 170);
    
    fill(255, 255, 255);
    
    if (((day() <= 21 ) && (month() == 12)) || (month() < 12)) {
    
      if (daysInYears(21, 12, 2014) > 99) {
      
        textFont(font, 55);
        text(str(daysInYears(21, 12, 2014)), bubbleX - 57, bubbleY - 13);
      
      } else if ((daysInYears(21, 12, 2014) > 9) && (daysInYears(21, 12, 2014) < 99)) {
     
        textFont(font, 60);
        text(str(daysInYears(21, 12, 2014)), bubbleX - 57, bubbleY - 13);
     
      } else {
      
        textFont(font, 65);
        text(str(daysInYears(21, 12, 2014)), bubbleX - 57, bubbleY - 13);
    
      }  
    
    } else if (((month() == 6) && (day() <= 22)) || (month() < 6)) {
      
      if (daysInYears(21, 12, 2014) > 99) {
      
        textFont(font, 55);
        text(str(daysInYears(22, 6, 2015)), bubbleX - 57, bubbleY - 13);
      
      } else if ((daysInYears(22, 6, 2015) > 9) && (daysInYears(21, 12, 2014) < 99)) {
     
        textFont(font, 60);
        text(str(daysInYears(22, 6, 2015)), bubbleX - 57, bubbleY - 13);
     
      } else {
      
        textFont(font, 65);
        text(str(daysInYears(22, 6, 2015)), bubbleX - 57, bubbleY - 13);
    
      } 
      
    } else {
      
      
    }  
      
    
    textFont(font, 33);
    text("days", bubbleX + 5, bubbleY + 5);
    textFont(font, 40);
    text("to", bubbleX - 37, bubbleY + 20);
    textFont(font, 40);
    text("Soltice", bubbleX - 67, bubbleY + 53);
    
    
  }
 
}  

//////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0440\u043e\u0446\u0435\u043d\u0442\u0430 \u0434\u043d\u044f \u0438 \u043e\u0441\u0432\u0435\u0449\u0435\u043d\u043d\u043e\u0441\u0442\u0438/////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////  
public void lightAndDayBubbleDraw(int bubbleX, int bubbleY, float inputData, boolean isDay, int colorBubble) { 
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);

//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u044f \u0441 \u043f\u0440\u043e\u0446\u0435\u043d\u0442\u043e\u043c \u0434\u043d\u044f/////////////////////////////////////  
  if (isDay) { 
    
    ellipse(bubbleX, bubbleY, 170, 170);
    fill(255, 255, 255);
    
    if ((hour() >= 7) && (hour() <=23)) {
      
      inputData = map(hour(), 7, 23, 0, 0.9f);
      inputData += map(minute(), 0, 59, 0, 0.1f);
      
      noStroke();
      arc(bubbleX, bubbleY, 140, 140, -HALF_PI, -HALF_PI + (TWO_PI * inputData));
      fill(yellowBubble);
      ellipse(bubbleX, bubbleY, 100, 100);
      stroke(255, 255, 255);
      
      fill(255, 255, 255);
      textFont(font, 35);
      text("Day", bubbleX - 32, bubbleY - 8);
      textFont(font, 30);
      if ((PApplet.parseInt(inputData*100)) >= 100) {
        text(str(PApplet.parseInt(inputData*100)) + "%", bubbleX - 36, bubbleY + 26);
      } else if ((PApplet.parseInt(inputData*100)) < 10) {
        text(str(PApplet.parseInt(inputData*100)) + "%", bubbleX - 22, bubbleY + 26);
      } else {
        text(str(PApplet.parseInt(inputData*100)) + "%", bubbleX - 26, bubbleY + 26);
      }
      
    } else {
      
      inputData = map(hour(), 0, 7, 0, 1);
      
      noStroke();
      arc(bubbleX, bubbleY, 140, 140, -HALF_PI, -HALF_PI + (TWO_PI * inputData));
      fill(yellowBubble);
      ellipse(bubbleX, bubbleY, 100, 100);
      stroke(255, 255, 255);
      
      fill(255, 255, 255);
      textFont(font, 29);
      text("Night", bubbleX - 34, bubbleY - 8);
      textFont(font, 30);
      if ((PApplet.parseInt(inputData*100)) == 100) {
        text(str(PApplet.parseInt(inputData*100)) + "%", bubbleX - 36, bubbleY + 26);
      } else if ((PApplet.parseInt(inputData*100)) < 10) {
        text(str(PApplet.parseInt(inputData*100)) + "%", bubbleX - 22, bubbleY + 26);
      } else {
        text(str(PApplet.parseInt(inputData*100)) + "%", bubbleX - 26, bubbleY + 26);
      }
      
    }  
    
    dayInfoBubbleX = bubbleX;
    dayInfoBubbleX = bubbleY;
    
  } else {  
//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u044f \u043e\u0441\u0432\u0435\u0449\u0435\u043d\u043d\u043e\u0441\u0442\u0438////////////////////////////////////////
    
    inputData = map(inputData, 0, 255, 0, 1);
    
    ellipse(bubbleX, bubbleY, 170, 170);
    fill(255, 255, 255);
    noStroke();
    arc(bubbleX, bubbleY, 140, 140, -HALF_PI, -HALF_PI + (TWO_PI * inputData));
    fill(redBubble);
    ellipse(bubbleX, bubbleY, 100, 100);
    stroke(255, 255, 255);
    
    fill(255, 255, 255);
    textFont(font, 29);
    text("Light", bubbleX - 34, bubbleY - 8);
    textFont(font, 40);
    text("out", bubbleX - 31, bubbleY + 28);
    
  } 
 
} 

//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////\u043a\u043b\u0430\u0441\u0441 \u043f\u0443\u0437\u044b\u0440\u0435\u0439 \u0434\u043b\u044f \u0443\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f \u043b\u0435\u043d\u0442\u0430\u043c\u0438////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////// 
public void rgbAndWhiteLightBubbleDraw(int bubbleX, int bubbleY, boolean isRGB, int colorBubble) { 
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);

//////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////\u0446\u0432\u0435\u0442\u043d\u0430\u044f \u043b\u0435\u043d\u0442\u0430/////////////////////////////////////////////  
  if(isRGB) { 
    
    ellipse(bubbleX, bubbleY, 200, 200);
    fill(255, 255, 255);
    ellipse(bubbleX, bubbleY, 173, 173);
    image(colorsPic, bubbleX - 85, bubbleY - 85, 170, 170);
    noStroke();
    ellipse(bubbleX, bubbleY, 131, 131);
    fill(colorBubble);
    ellipse(bubbleX, bubbleY, 130, 130);
    fill(255, 255, 255);
    arc(bubbleX, bubbleY, 130, 130, -HALF_PI, -HALF_PI + (TWO_PI * brightnessRGB));
    fill(colorBubble);
    ellipse(bubbleX, bubbleY, 100, 100);
    feelBubbleStroke(colorBubble);
    
    fill(255, 255, 255);
    textFont(font, 30);
    text("RGB", bubbleX - 33, bubbleY);
    
    fill(colorIn);
    ellipse(bubbleX, bubbleY + 20, 23, 23);
    
    if((((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY)*(mouseY - bubbleY)) <= (88*88)) && (((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY)*(mouseY - bubbleY)) >= (67*67)) && mousePressed) {
     
      colorIn = get(mouseX, mouseY); 
      
      fill(((colorIn >> 16) & 0xFF), ((colorIn >> 8) & 0xFF), (colorIn & 0xFF));
      ellipse(mouseX, mouseY, 18, 18);
      
    } else if((((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY)*(mouseY - bubbleY)) <= (66*66)) && (((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY - 20)*(mouseY - bubbleY - 20)) >= (12*12)) && mousePressed) {
     
      fill(255, 255, 255);
      
      if(mouseX  >= (bubbleX - 130) && mouseX <= bubbleX) { 
      
        brightnessRGB = (acos((mouseY - bubbleY)/(sqrt(((sq(mouseX - bubbleX)) + (sq(mouseY - bubbleY)))))) + PI)/TWO_PI ;
      
      } else if (mouseX  < (bubbleX + 130) && mouseX > bubbleX) {
        
        brightnessRGB = (acos(-((mouseY - bubbleY)/(sqrt(((sq(mouseX - bubbleX)) + (sq(mouseY - bubbleY))))))))/TWO_PI;
        
      }
     
      
    } else if ((((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY - 20)*(mouseY - bubbleY - 20)) <= (12*12)) && mousePressed) {
      
       if (colorIn == color(0, 0, 0)) {
         
         colorIn = color(255, 255, 255);
         
       } else {
         
         colorIn = color(0, 0, 0);
         
       }
       
       someDelay(100);
      
    }  
    
    redLED = PApplet.parseInt(((colorIn >> 16) & 0xFF) * brightnessRGB);  
    greenLED = PApplet.parseInt(((colorIn >> 8) & 0xFF) * brightnessRGB);   
    blueLED = PApplet.parseInt((colorIn & 0xFF) * brightnessRGB);  
    
  } else { 
//////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////\u0431\u0435\u043b\u0430\u044f \u043b\u0435\u043d\u0442\u0430///////////////////////////////////////////////    
    ellipse(bubbleX, bubbleY, 150, 150);
    fill(255, 255, 255);
    noStroke();
    arc(bubbleX, bubbleY, 120, 120, -HALF_PI, -HALF_PI + (TWO_PI * brightnessWhiteLED));
    fill(colorBubble);
    ellipse(bubbleX, bubbleY, 90, 90);
    stroke(255, 255, 255);
    
    fill(255, 255, 255);
    textFont(font, 20);
    text("White", bubbleX - 27, bubbleY - 5);
    textFont(font, 30);
    text("LED", bubbleX - 28, bubbleY + 23);
    textFont(font, 15);
    
    if (brightnessWhiteLED == 0) {
        
      text("off", bubbleX - 11, bubbleY - 25);
        
    } else {
       
      text("on", bubbleX - 10, bubbleY - 25);
        
    }
    
    if((((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY)*(mouseY - bubbleY)) <= (60*60)) && (((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY + 25)*(mouseY - bubbleY + 25)) >= (15*15)) && mousePressed) {
     
      fill(255, 255, 255);
      
      if(mouseX  >= (bubbleX - 130) && mouseX <= bubbleX) { 
      
        brightnessWhiteLED = (acos((mouseY - bubbleY)/(sqrt(((sq(mouseX - bubbleX)) + (sq(mouseY - bubbleY)))))) + PI)/TWO_PI ;
      
      } else if (mouseX  < (bubbleX + 130) && mouseX > bubbleX) {
        
        brightnessWhiteLED = (acos(-((mouseY - bubbleY)/(sqrt(((sq(mouseX - bubbleX)) + (sq(mouseY - bubbleY))))))))/TWO_PI;
        
      }
     
      
    } else if ((((mouseX - bubbleX)*(mouseX - bubbleX))+((mouseY - bubbleY + 25)*(mouseY - bubbleY + 25)) <= (15*15)) && mousePressed) {
      
      if (brightnessWhiteLED == 0) {
        
        brightnessWhiteLED = 100;
        
      } else {
       
        brightnessWhiteLED = 0; 
        
      }
      
      someDelay(100);  
   
    }
    
  }
  
}

////////////////////////////////////////\u0444\u0443\u043d\u043a\u0446\u0438\u044f \u043e\u0436\u0438\u0434\u0430\u043d\u0438\u044f//////////////////////////////////////////////
public void someDelay(int ms) {
   try
  {    
    Thread.sleep(ms);
  }
  catch(Exception e){}
}
 
///////////////////////////////\u043e\u0431\u0440\u0430\u0431\u043e\u0442\u043a\u0430 \u0434\u0430\u043d\u043d\u044b\u0445 \u043f\u0440\u0438\u0448\u0435\u0434\u0448\u0438\u0445 \u0432 \u043f\u043e\u0440\u0442////////////////////////////////////// 
public void serialEvent (Serial port) {
  
  data = port.readStringUntil(';');
  
  data = data.substring(0, data.length() - 1);
  
  index = data.indexOf(",");
  indexTwo = data.indexOf("/");
  
  temperatureIn = data.substring(0, index);
  temperatureOut = data.substring(index + 1, indexTwo);
  outLight = data.substring(indexTwo + 1, data.length());
  
}

//////////////////////////////////////\u0441\u0447\u0451\u0442 \u0434\u043d\u0435\u0439 \u0434\u043e \u0441\u043e\u0431\u044b\u0442\u0438\u044f//////////////////////////////////////////// 
public int daysInYears(int day, int month, int year) {
  
  int daysTo = 0;
   
  if  (year == year()) {
    
    for (int i = month() + 1;i < month; ++i) {
      
      if (i == 1) {
        
        daysTo += 31;
        daysTo += (31 - day());
        
      } else if (i == 2) {
        
        daysTo += 28;
        daysTo += (28 - day());
        
      } else if (i == 3) {
        
        daysTo += 31;
        daysTo += (31 - day());
        
      } else if (i == 4) {
        
        daysTo += 30;
        daysTo += (30 - day());
        
      } else if (i == 5) {
        
        daysTo += 31;
        daysTo += (31 - day());
        
      } else if (i == 6) {
        
        daysTo += 30;
        daysTo += (30 - day());
        
      } else if (i == 7) {
        
        daysTo += 30;
        daysTo += (30 - day());
        
      } else if (i == 8) {
      
        daysTo += 31;
        daysTo += (31 - day());
        
      } else if (i == 9) {
        
        daysTo += 30;
        daysTo += (30 - day());
        
      } else if (i == 10) {
        
        daysTo += 31;
        daysTo += (31 - day());
        
      } else if (i == 11) {
        
        daysTo += 30;
        daysTo += (30 - day());
        
      } else if (i == 12) {
        
        daysTo += 31;
        daysTo += (31 - day());
        
      }
    
    }

    daysTo += day;    
     
  }   
        
  return daysTo;
  
}  
  
//////////////////////////\u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u0435 \u043e\u0431\u0432\u043e\u0434\u043a\u0438 \u043f\u0443\u0437\u044b\u0440\u0435\u0439 \u0432 \u0437\u0430\u0432\u0438\u0441\u0438\u043c\u043e\u0441\u0442\u0438 \u043e\u0442 \u0446\u0432\u0435\u0442\u0430/////////////////////////// 
public void feelBubbleStroke(int colorInput) { 
  
  if (colorInput == redBubble) {
    
    stroke(redStrokeBubble);
  
  } else if (colorInput == greenBubble) {
    
    stroke(greenStrokeBubble);
    
  } else if (colorInput == blueBubble) {
    
    stroke(blueStrokeBubble);
    
  }  else if (colorInput == yellowBubble) {
    
    stroke(yellowStrokeBubble);
    
  }
  
}
 
////////////////////////////////////////////\u0437\u0430\u043f\u0443\u0441\u043a \u0446\u0432\u0435\u0442\u043d\u043e\u0439 \u043b\u0435\u043d\u0442\u044b////////////////////////////////////// 
public void startingLight() { 
  
  isStartedLight = true;
  
  colorIn = color(PApplet.parseInt(random(0, 255)), PApplet.parseInt(random(0, 255)), PApplet.parseInt(random(0, 255)));
  
  for (int i = 0; i <= map(brightnessRGB, 0, 1, 0, 255); ++i) {
    
    redLED = PApplet.parseInt(((colorIn >> 16) & 0xFF) * map(i, 0, 255, 0, 1));  
    greenLED = PApplet.parseInt(((colorIn >> 8) & 0xFF) * map(i, 0, 255, 0, 1));   
    blueLED = PApplet.parseInt((colorIn & 0xFF) * map(i, 0, 255, 0, 1));    
     
    port.write("R" + redLED + "G" + greenLED + "B" + blueLED + "W" + brightnessWhiteLED*255 + ";"); // \u0437\u0430\u0436\u0438\u0433\u0430\u0435\u043c \u043d\u0443\u0436\u043d\u044b\u0435 \u0446\u0432\u0435\u0442\u0430 \u043d\u0430 \u043f\u043e\u0434\u0441\u0432\u0435\u0442\u043a\u0430\u0445 
     
    someDelay(5); 
  
  }
  
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Home" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
