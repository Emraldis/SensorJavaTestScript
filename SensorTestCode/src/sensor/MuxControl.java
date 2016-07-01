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
    GpioPinDigitalOutput SYNC;
    GpioPinDigitalOutput SCLK;
    GpioPinDigitalOutput DIN;
    SystemController lockout;
    public MuxControl (GpioPinDigitalOutput SYNC, GpioPinDigitalOutput SCLK, GpioPinDigitalOutput DIN, SystemController sysControl){
        this.SYNC = SYNC;
        SCLK = SCLK;
        this.DIN = DIN;
        this.SYNC.low();
        SCLK.low();
        this.DIN.low();
        lockout = sysControl;
    }
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
            lockout.muxControl = false;
            System.out.println("SYSTEM - MUX set, locking down mux for 2 minuites");
            Thread.sleep(lockout.muxLockoutTime);
            lockout.muxControl = true;
            System.out.println("SYSTEM - MUX Lockdown Dropped");
        }
    }
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
