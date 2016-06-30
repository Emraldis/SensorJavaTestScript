/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;
import java.util.*;
import java.io.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.pi4j.io.gpio.impl.PinImpl;

/**
 *
 * @author Alfie Feltham
 */
public class LogicRelay {
    Scanner tempRead;
    GpioPinDigitalOutput pinOne;
    GpioPinDigitalOutput pinTwo;
    SystemController sysData;
    public LogicRelay (GpioPinDigitalOutput outputOne, GpioPinDigitalOutput outputTwo, SystemController sysData) {
        pinOne = outputOne;
        pinTwo = outputTwo;
        pinOne.low();
        pinTwo.low();
        this.sysData = sysData;
    }
    public void incubate (int time, LedIndicator led, String tempLogFile){
        int i = 0;
        long tempLong = 0;
        float temp = 0;
        StringTokenizer strTok;
        String input;
        String tempString = " ";
        while((i < time) && (!sysData.buttonControl)){
            try {
                tempRead = new Scanner (new FileInputStream(tempLogFile));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LogicRelay.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("\nCritical Error: temperature probe data file not found");
            }
            while(tempRead.hasNextLine()){
                input = tempRead.nextLine();
                strTok = new StringTokenizer(input," ");
                while(strTok.hasMoreTokens()){
                    tempString = strTok.nextToken();
                }
                if(tempString.equalsIgnoreCase("YES")){
                    input = tempRead.nextLine();
                    strTok = new StringTokenizer(input,"=");
                    while(strTok.hasMoreTokens()){
                        tempString = strTok.nextToken();
                    }
                    tempLong = Long.parseLong(tempString);
                    temp = tempLong/1000;
                }else{
                    System.out.println("\nError Reading Temperature");
                }
            }
            led.toggle();
            i = i + 100;
            pinOne.low();
            pinTwo.low();
            if(temp < sysData.lowerThresholdTemp){
                if(sysData.systemOutput){
                    System.out.println("\nSYSTEM - Current temperature: " + temp + " degrees Celcius");
                    System.out.println("\nSYSTEM - Temperature too low, adjusting.");
                }
                pinOne.high();
                pinTwo.low();
            }else if(temp > sysData.upperThresholdTemp){
                if(sysData.systemOutput){
                    System.out.println("\nSYSTEM - Current temperature: " + temp + " degrees Celcius");
                    System.out.println("\nSYSTEM - Temperature too high, adjusting.");
                }
                pinOne.low();
                pinTwo.high();
            }
            if(sysData.systemOutput){
                if((((time - i)/100) % 10) == 0){
                    System.out.println("\nSYSTEM - Incubation time remaining: " + ((time - i)/100));    
                }
            }
        }
        System.out.println("\nSYSTEM - Incubation completed, setting MUX");
        pinOne.low();
        pinTwo.low();
        led.ledController.high();
    }
    
}
