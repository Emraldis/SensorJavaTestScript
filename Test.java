/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
/**
 *
 * @author Alfie Feltham
 */
public class Test {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Setting up pins");
        final GpioController gpio = GpioFactory.getInstance();
        System.out.println("Light turning on");
        final GpioPinDigitalOutput ledOne = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "LED", PinState.HIGH);
        ledOne.setShutdownOptions(true,PinState.LOW);
        Thread.sleep(1000);
        System.out.println("Light turning off");
        ledOne.low();
    }
    
}
