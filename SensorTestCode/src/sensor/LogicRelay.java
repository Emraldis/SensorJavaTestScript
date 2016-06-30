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
    public LogicRelay (GpioPinDigitalOutput outputOne, GpioPinDigitalOutput outputTwo, String tempLogFile) {
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
    }
    public void incubate (int time, LedIndicator led){
        int i = 0;
        while((tempRead.hasNextLong() == true) && (i < time)){
            led.toggle();
            i = i + 100;
            if((tempRead.nextLong() / 1000) < 33.00){
                pinOne.high();
                pinTwo.low();
            }else if((tempRead.nextLong() / 1000) > 46.00){
                pinOne.low();
                pinTwo.high();
            }
        }
        led.ledController.high();
    }
    
}
