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
    public LogicRelay (GpioPinDigitalOutput outputOne, GpioPinDigitalOutput outputTwo, String tempLogFile, SystemController sysData) {
        pinOne = outputOne;
        pinTwo = outputTwo;
        try {
            tempRead = new Scanner (new FileInputStream(tempLogFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LogicRelay.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("\nCritical Error: temperature probe data file not found");
        }
        pinOne.low();
        pinTwo.low();
        this.sysData = sysData;
    }
    public void incubate (int time, LedIndicator led){
        int i = 0;
        long temp = 0;
        StringTokenizer strTok;
        String input;
        String tempString = " ";
        while(i < time){
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
                    System.out.println("\n" + tempString);
                    temp = Long.parseLong(tempString);
                    temp = temp/1000;
                }
            }
            System.out.println("\nSYSTEM - Current temperature: " + temp + " degrees Celcius");
            led.toggle();
            i = i + 100;
            if(temp < sysData.lowerThresholdTemp){
                pinOne.high();
                pinTwo.low();
            }else if(temp > sysData.upperThresholdTemp){
                pinOne.low();
                pinTwo.high();
            }
        }
        led.ledController.high();
    }
    
}
