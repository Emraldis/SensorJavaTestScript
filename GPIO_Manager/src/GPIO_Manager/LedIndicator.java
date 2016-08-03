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

/**
 *
 * @author Alfie Feltham
 */
public class LedIndicator {
    GpioPinDigitalOutput ledController;
    delayManager timer;
    /*-----------------------|Basic Constructor|-----------------------*/
    public LedIndicator (GpioPinDigitalOutput ledControl){
        ledController = ledControl;
        ledController.low();
    }
    /*-----------------------|CURRENTLY UNUSED, Blinks light for a set time, with alterable on and off periods|-----------------------*/
    public void blink (int duration, int onTime, int offTime) throws InterruptedException{
        int i = 0;
        while(i <duration){
            timer.waitSeconds(offTime);
            ledController.high();
            timer.waitSeconds(onTime);
            ledController.low();
            i = (i + onTime + offTime);
        }
    }
    /*-----------------------|Toggles the Light|-----------------------*/
    public void toggle(){
        ledController.toggle();
    }
    
    public void off(){
        ledController.low();
    }
}
