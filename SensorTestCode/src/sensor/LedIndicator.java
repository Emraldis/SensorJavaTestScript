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

/**
 *
 * @author Alfie Feltham
 */
public class LedIndicator {
    GpioPinDigitalOutput ledController;
    public LedIndicator (GpioPinDigitalOutput ledControl){
        ledController = ledControl;
        ledController.low();
    }
    public void blink (int duration, int onTime, int offTime) throws InterruptedException{
        int i = 0;
        while(i <duration){
            Thread.sleep(offTime);
            ledController.high();
            Thread.sleep(onTime);
            ledController.low();
            i = (i + onTime + offTime);
        }
    }
    public void toggle(){
        ledController.toggle();
    }
}
