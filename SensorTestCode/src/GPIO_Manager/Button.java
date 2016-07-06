/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPIO_Manager;
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
    /*
    This button class is easily repeatable, just add "Button BUTTON_NAME = new Button(sysControl,APPROPRIATE_PIN_OBJECT,APPROPRIATE_LOGIC_RELAY_OBJECT,APPROPRIATE_LED_CONTROLLER_OBJECT,muxController,BUTTON_NUMBER,APPROPRIATE_THERMOMETER_FILENAME); to the main function in GPIO_Manager.java"
    */
    public Button(SystemController sysControl, GpioPinDigitalInput buttonPin, LogicRelay logicGate, LedIndicator led, MuxControl mux, int buttonID, String tempLogFile){
        buttonPin.addListener(new GpioPinListenerDigital(){
            
            /*-----------------------|Creating Button Action Listener, will activate on a button press|-----------------------*/
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if(sysControl.buttonControl != false){
                    System.out.println("Button " + buttonID + " Pushed");
                    try {
                        sysControl.lockButtons();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    logicGate.incubate(sysControl.inductionTime,sysControl.respondTime,led,tempLogFile);
                    try {
                        System.out.println("\nSYSTEM - Button " + buttonID + " activated, locking down buttons for " + (sysControl.muxLockoutTime / 100) + " seconds");
                        Thread.sleep(sysControl.muxLockoutTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        mux.setMux(buttonID);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Button.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
        
        
    }
    
}
