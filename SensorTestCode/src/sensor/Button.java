/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;
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
public class Button {
    
    public Button(SystemController sysControl, GpioPinDigitalInput buttonPin, LogicRelay logicGate, LedIndicator led, MuxControl mux, int buttonID, String tempLogFile){
        buttonPin.addListener(new GpioPinListenerDigital(){

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println("\nButton " + buttonID + " pushed");
                if(sysControl.buttonControl != false){
                    System.out.println("\nButton Lockdown Started");
                    sysControl.buttonControl = false;
                    logicGate.incubate(sysControl.incubateTime,led,tempLogFile);
                    
                    try {
                        led.blink(sysControl.incubateTime, 1000, 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    sysControl.buttonControl = true;
                    try {
                        mux.setMux(buttonID);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("\nButton Lockdown dropped");
                }else{
                    System.out.println("\nButtons Currently under Lockdown");
                }
            }
            
        });
        
        
    }
    
}
