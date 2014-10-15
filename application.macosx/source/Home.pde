/* 
 * Home (for Mac/PC) - программа для управления умным домом.
 * Выпущена для частного использования, все права на программу принадлежат AB.
 * (byte) 2014 Все права защищены битой. 
 */

import processing.serial.*; // импортируем библиотечку для данных с портов

Serial port; // создаем порт

PFont font; // создаем шрифт

PImage colorsPic;

String outLight = ""; // значение внешнего света
String temperatureIn = ""; // температура в комнате
String temperatureOut = ""; // температура на улице
String data = ""; // входящее сообщение
int index = 0; // индекс "," в входящем сообщения
int indexTwo = 0; // индекс "/" в входящем сообщения

color greenBubble = color(137, 213, 72); // цвет зеленого пузыря
color redBubble = color(219, 105, 90); // цвет красного пузыря
color blueBubble = color(93, 136, 241); // цвет синего пузыря
color yellowBubble = color(250, 185, 80); // цвет желтого пузыря
color backgroundColor = color(179, 220, 255); // цвет фона
color redStrokeBubble = color(255, 179, 191); // цвет обводки красного пузыря
color blueStrokeBubble = color(170, 194, 238); // цвет обводки синего пузыря
color greenStrokeBubble = color(174, 255, 182); // цвет обводки зеленого пузыря
color yellowStrokeBubble = color(255, 226, 186); // цвет обводки желтого пузыря

int dayInfoBubbleX = 100; // координаты пузыря с процентом дня
int dayInfoBubbleY = 300;

int redLED = 255; // красный светодиод ленты
int greenLED = 255; // зеленый светодиод ленты
int blueLED = 255; // синий светодиод ленты
float brightnessRGB = 0.7; // яркость цветной ленты

float brightnessWhiteLED = 0; // яркость белой ленты

color colorIn; // цвет цветной ленты

boolean isStartedLight = false;

///////////////////////////////////////////////настройка//////////////////////////////////////////////
void setup() {
  
  size (816, 460); // определяем размер окошка
  background(179, 220, 255); // закрашиваем фон
  
  port = new Serial(this, Serial.list()[2], 9600); // подключаем порт
  port.bufferUntil(';'); // грузим до ';'
  
  font = loadFont("Helvetica-Bold.vlw"); // грузим шрифт
  
  stroke(255, 255, 255); // устанавливаем белую обводку 
  strokeWeight(3); // устанавливаем жирность обводки
  
  colorsPic = loadImage("Colors.png");
  
}

////////////////////////////////////////рисуем объекты////////////////////////////////////////////////
void draw() {
 
  // хреновина, которая должна позволять перетаскивать пузырики, но она нихрена не работает правильно
  /*if(mousePressed && (((mouseX - dayInfoBubbleX)*(mouseX - dayInfoBubbleX)) + ((mouseY - dayInfoBubbleY)*(mouseY - dayInfoBubbleY)) < 100*100)) {
    
    background(backgroundColor);
    lightAndDayBubbleDraw(mouseX, mouseY, 235, true);
    dayInfoBubbleX = mouseX;
    dayInfoBubbleY = mouseY;
  
  }*/
  
  temperatureBubbleDraw(130, 100, int(temperatureOut), true, blueBubble); // рисуем пузырь с температурой на улице
  temperatureBubbleDraw(300, 330, int(temperatureIn), false, redBubble); // рисуем пузырь с температурой в комнате
  
  dateBubbleDraw(340, 130, true, blueBubble); // рисуем пузырь с текущей датой
  dateBubbleDraw(490, 330, false, greenBubble); // рисуем пузырь с количеством дней до солнцестояния
  
  lightAndDayBubbleDraw(100, 300, 0, true, yellowBubble); // рисуем пузырь с процентом дня
  lightAndDayBubbleDraw(500, 100, int(outLight), false, redBubble); // рисуем пузырь со светом в комнате
  
  rgbAndWhiteLightBubbleDraw(700, 300, true, yellowBubble); // рисуем пузырь с цветной подсветкой
  rgbAndWhiteLightBubbleDraw(730, 100, false, blueBubble); // рисуем пузырь с белой подсветкой
 
  if (!isStartedLight) {
    
    startingLight();
    
  }
  
  port.write("R" + redLED + "G" + greenLED + "B" + blueLED + "W" + brightnessWhiteLED*255 + ";"); // зажигаем нужные цвета на подсветках
  
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////класс пузырей с температурой/////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////  
void temperatureBubbleDraw(int bubbleX, int bubbleY, int temperature, boolean isOut, color colorBubble) { // пузыри с температурой
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);
    
  ellipse(bubbleX, bubbleY, 157, 157);
  
////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////класс пузыря с температурой на улице//////////////////////////////////
  if (isOut) {
    fill(255, 255, 255);
    textFont(font, 20);
    text("Outdoor", bubbleX - 52, bubbleY - 37);
    text("temperature", bubbleX - 50, bubbleY - 20);
    textFont(font, 65);
    if (temperature > 9) {
      text(temperature + "º", bubbleX - 45, bubbleY + 40);
    } else { 
      text(temperature + "º", bubbleX - 25, bubbleY + 40);
    }
  } else { 
////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////класс пузыря с температурой в комнате//////////////////////////////////  
    fill(255, 255, 255);
    textFont(font, 20);
    text("Indoor", bubbleX - 52, bubbleY - 37);
    text("temperature", bubbleX - 50, bubbleY - 20);
    textFont(font, 65);
    if (temperature > 9) {
      text(temperature + "º", bubbleX - 45, bubbleY + 40);
    } else { 
      text(temperature + "º", bubbleX - 25, bubbleY + 40);
    }
  }
  
  stroke(255, 255, 255);
    
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////класс пузырей-календарей/////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
void dateBubbleDraw(int bubbleX, int bubbleY, boolean isSimple, color colorBubble) { 
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);

//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////класс пузыря с текущей датой////////////////////////////////////////  
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
///////////////////////////класс пузыря с количеством дней до солнцестояния///////////////////////////     
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
////////////////////////////////класс процента дня и освещенности/////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////  
void lightAndDayBubbleDraw(int bubbleX, int bubbleY, float inputData, boolean isDay, color colorBubble) { 
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);

//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////класс пузыря с процентом дня/////////////////////////////////////  
  if (isDay) { 
    
    ellipse(bubbleX, bubbleY, 170, 170);
    fill(255, 255, 255);
    
    if ((hour() >= 7) && (hour() <=23)) {
      
      inputData = map(hour(), 7, 23, 0, 0.9);
      inputData += map(minute(), 0, 59, 0, 0.1);
      
      noStroke();
      arc(bubbleX, bubbleY, 140, 140, -HALF_PI, -HALF_PI + (TWO_PI * inputData));
      fill(yellowBubble);
      ellipse(bubbleX, bubbleY, 100, 100);
      stroke(255, 255, 255);
      
      fill(255, 255, 255);
      textFont(font, 35);
      text("Day", bubbleX - 32, bubbleY - 8);
      textFont(font, 30);
      if ((int(inputData*100)) >= 100) {
        text(str(int(inputData*100)) + "%", bubbleX - 36, bubbleY + 26);
      } else if ((int(inputData*100)) < 10) {
        text(str(int(inputData*100)) + "%", bubbleX - 22, bubbleY + 26);
      } else {
        text(str(int(inputData*100)) + "%", bubbleX - 26, bubbleY + 26);
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
      if ((int(inputData*100)) == 100) {
        text(str(int(inputData*100)) + "%", bubbleX - 36, bubbleY + 26);
      } else if ((int(inputData*100)) < 10) {
        text(str(int(inputData*100)) + "%", bubbleX - 22, bubbleY + 26);
      } else {
        text(str(int(inputData*100)) + "%", bubbleX - 26, bubbleY + 26);
      }
      
    }  
    
    dayInfoBubbleX = bubbleX;
    dayInfoBubbleX = bubbleY;
    
  } else {  
//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////класс пузыря освещенности////////////////////////////////////////
    
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
//////////////////////////////класс пузырей для управления лентами////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////// 
void rgbAndWhiteLightBubbleDraw(int bubbleX, int bubbleY, boolean isRGB, color colorBubble) { 
  
  feelBubbleStroke(colorBubble);
  fill(colorBubble);

//////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////цветная лента/////////////////////////////////////////////  
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
    
    redLED = int(((colorIn >> 16) & 0xFF) * brightnessRGB);  
    greenLED = int(((colorIn >> 8) & 0xFF) * brightnessRGB);   
    blueLED = int((colorIn & 0xFF) * brightnessRGB);  
    
  } else { 
//////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////белая лента///////////////////////////////////////////////    
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

////////////////////////////////////////функция ожидания//////////////////////////////////////////////
void someDelay(int ms) {
   try
  {    
    Thread.sleep(ms);
  }
  catch(Exception e){}
}
 
///////////////////////////////обработка данных пришедших в порт////////////////////////////////////// 
void serialEvent (Serial port) {
  
  data = port.readStringUntil(';');
  
  data = data.substring(0, data.length() - 1);
  
  index = data.indexOf(",");
  indexTwo = data.indexOf("/");
  
  temperatureIn = data.substring(0, index);
  temperatureOut = data.substring(index + 1, indexTwo);
  outLight = data.substring(indexTwo + 1, data.length());
  
}

//////////////////////////////////////счёт дней до события//////////////////////////////////////////// 
int daysInYears(int day, int month, int year) {
  
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
  
//////////////////////////заполнение обводки пузырей в зависимости от цвета/////////////////////////// 
void feelBubbleStroke(color colorInput) { 
  
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
 
////////////////////////////////////////////запуск цветной ленты////////////////////////////////////// 
void startingLight() { 
  
  isStartedLight = true;
  
  colorIn = color(int(random(0, 255)), int(random(0, 255)), int(random(0, 255)));
  
  for (int i = 0; i <= map(brightnessRGB, 0, 1, 0, 255); ++i) {
    
    redLED = int(((colorIn >> 16) & 0xFF) * map(i, 0, 255, 0, 1));  
    greenLED = int(((colorIn >> 8) & 0xFF) * map(i, 0, 255, 0, 1));   
    blueLED = int((colorIn & 0xFF) * map(i, 0, 255, 0, 1));    
     
    port.write("R" + redLED + "G" + greenLED + "B" + blueLED + "W" + brightnessWhiteLED*255 + ";"); // зажигаем нужные цвета на подсветках 
     
    someDelay(5); 
  
  }
  
}
