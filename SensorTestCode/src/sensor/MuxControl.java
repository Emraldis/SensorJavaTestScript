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
public class MuxControl {
    GpioPinDigitalOutput pinA;
    GpioPinDigitalOutput pinB;
    GpioPinDigitalOutput pinC;
    GpioPinDigitalOutput pinD;
    SystemController lockout;
    public MuxControl (GpioPinDigitalOutput pinOne, GpioPinDigitalOutput pinTwo, GpioPinDigitalOutput pinThree, GpioPinDigitalOutput pinFour, SystemController sysControl){
        pinA = pinOne;
        pinB = pinTwo;
        pinC = pinThree;
        pinD = pinFour;
        pinA.low();
        pinB.low();
        pinC.low();
        pinD.low();
        lockout = sysControl;
    }
    public void setMux (int setting) throws InterruptedException{
        pinA.low();
        pinB.low();
        pinC.low();
        pinD.low();
        if(lockout.muxControl != false){
            switch(setting){
                case 1:
                    pinA.high();
                    break;
                case 2:
                    pinB.high();
                    break;
                case 3:
                    pinA.high();
                    pinB.high();
                    break;
                case 4:
                    pinC.high();
                    break;
                    
                default:
                    System.out.println("MUX Control encountered unexpected error");
            }
            lockout.muxControl = false;
            System.out.println("SYSTEM - MUX set, locking down mux for 2 minuites");
            Thread.sleep(lockout.muxLockoutTime);
            lockout.muxControl = true;
            System.out.println("SYSTEM - MUX Lockdown Dropped");
        }
    }
}
