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
public class MuxControl {
    GpioPinDigitalOutput SYNC;
    GpioPinDigitalOutput SCLK;
    GpioPinDigitalOutput DIN;
    SystemController lockout;
    /*-----------------------|Basic Constructor|-----------------------*/
    public MuxControl (GpioPinDigitalOutput SYNC, GpioPinDigitalOutput SCLK, GpioPinDigitalOutput DIN, SystemController sysControl){
        this.SYNC = SYNC;
        this.SCLK = SCLK;
        this.DIN = DIN;
        this.SYNC.high();
        this.SCLK.high();
        this.DIN.high();
        lockout = sysControl;
    }
    /*-----------------------|Sets the value of the mux based on Button ID (will write out the binary equivalent of the buttons ID Number)|-----------------------*/
    /*
    To add more button options, simply add another case with the button option, and then add the binary equivalent in the section below it. For Example: (example using 5)
                case 5:
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(1);
                    this.writeBit(0);
                    this.writeBit(1);
                    break;
    */
    public void setMux (int setting) throws InterruptedException{
        SYNC.high();
        SCLK.high();
        DIN.high();
        if(lockout.muxControl != false){
            switch(setting){
                case 1:
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(1);
                    break;
                case 2:
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(1);
                    this.writeBit(0);
                    break;
                case 3:
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(1);
                    this.writeBit(1);
                    break;
                case 4:
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(1);
                    this.writeBit(0);
                    this.writeBit(0);
                    break;
                    
                default:
                    System.out.println("MUX Control encountered unexpected error");
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
                    this.writeBit(0);
            }
            SYNC.high();
            SCLK.high();
            DIN.high();
            lockout.muxControl = false;
            /*-----------------------|Locks out MUX for the set Mux Lockout time|-----------------------*/
            System.out.println("SYSTEM - MUX set, locking down mux for 2 minuites");
            Thread.sleep(lockout.muxLockoutTime);
            lockout.muxControl = true;
            System.out.println("SYSTEM - MUX Lockdown Dropped");
        }
    }
    /*-----------------------|Writes a single bit out to the steam|-----------------------*/
    public void writeBit(int bit) throws InterruptedException{
        if(bit == 1){
            DIN.high();
        }else{
            DIN.low();
        }
        SCLK.low();
        Thread.sleep(2);
        SCLK.high();
        
    }
}
