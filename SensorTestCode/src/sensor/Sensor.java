/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;
import java.util.*;
import com.pi4j.io.gpio.*;
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
public class Sensor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String menu = " ";
        Scanner scanner = new Scanner(System.in);
        String tempLogFileOne = "/sys/bus/w1/devices/28-000007f9caa2/w1_slave";
        String tempLogFileTwo = "/sys/bus/w1/devices/28-000007fa0cdf/w1_slave";
        String tempLogFileThree = "/sys/bus/w1/devices/28-000007fa462b/w1_slave";
        String tempLogFileFour = "/sys/bus/w1/devices/28-000007fa67e6/w1_slave";
        
        //Setting up GPIO controller
        final GpioController gpio = GpioFactory.getInstance();
        //"global" button and mux control variables, as well as incubation time, mux lockout time, and the threshold temperatures, all stored in this object
        SystemController sysControl = new SystemController();
        sysControl.buttonControl = true;
        sysControl.muxControl = true;
        sysControl.incubateTime = 12000;
        sysControl.muxLockoutTime = 12000;
        sysControl.upperThresholdTemp = 46;
        sysControl.lowerThresholdTemp = 33;
        sysControl.systemOutput = true;
        //Button Pin setups in WiringPi GPIO Pinout format
        final GpioPinDigitalInput buttonOnePin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_08, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput buttonTwoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput buttonThreePin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput buttonFourPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, PinPullResistance.PULL_DOWN);
        //LED Pin setups in WiringPi GPIO Pinout format
        final GpioPinDigitalOutput ledOnePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "LEDOne", PinState.LOW);
        final GpioPinDigitalOutput ledTwoPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LEDTwo", PinState.LOW);
        final GpioPinDigitalOutput ledThreePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "LEDThree", PinState.LOW);
        final GpioPinDigitalOutput ledFourPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "LEDFour", PinState.LOW);
        //Logic gate Pin setups in WiringPi GPIO Pinout format
        final GpioPinDigitalOutput LogicOneAPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "LogicOneA", PinState.LOW);
        final GpioPinDigitalOutput LogicOneBPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "LogicOneB", PinState.LOW);
        final GpioPinDigitalOutput LogicTwoAPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "LogicTwoA", PinState.LOW);
        final GpioPinDigitalOutput LogicTwoBPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "LogicTwoB", PinState.LOW);
        final GpioPinDigitalOutput LogicThreeAPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "LogicThreeA", PinState.LOW);
        final GpioPinDigitalOutput LogicThreeBPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "LogicThreeB", PinState.LOW);
        final GpioPinDigitalOutput LogicFourAPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "LogicFourA", PinState.LOW);
        final GpioPinDigitalOutput LogicFourBPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "LogicFourB", PinState.LOW);
        //Mux setups in WiringPi GPIO Pinout format
        final GpioPinDigitalOutput MuxSYNC = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "MUXPinOne", PinState.LOW);
        final GpioPinDigitalOutput MuxSCLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "MUXPinTwo", PinState.LOW);
        final GpioPinDigitalOutput MuxDIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MUXPinThree", PinState.LOW);
        final GpioPinDigitalOutput MuxFourPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MUXPinFour", PinState.LOW);
        
        System.out.println("\nSYSTEM - Pin setup Completed");
        
        //Creating LED indicator classes
        LedIndicator ledOne = new LedIndicator(ledOnePin);
        LedIndicator ledTwo = new LedIndicator(ledTwoPin);
        LedIndicator ledThree = new LedIndicator(ledThreePin);
        LedIndicator ledFour = new LedIndicator(ledFourPin);
        //Creating logic relay control classes
        LogicRelay logicOne = new LogicRelay(LogicOneAPin,LogicOneBPin,sysControl);
        LogicRelay logicTwo = new LogicRelay(LogicTwoAPin,LogicTwoBPin,sysControl);
        LogicRelay logicThree = new LogicRelay(LogicThreeAPin,LogicThreeBPin,sysControl);
        LogicRelay logicFour = new LogicRelay(LogicFourAPin,LogicFourBPin,sysControl);
        //Creating Mux Controller class
        MuxControl muxController = new MuxControl(MuxSYNC,MuxSCLK,MuxDIN,sysControl);
        //Creating Button classes
        Button buttonOne = new Button(sysControl,buttonOnePin,logicOne,ledOne,muxController,1,tempLogFileOne);
        Button buttonTwo = new Button(sysControl,buttonTwoPin,logicTwo,ledTwo,muxController,2,tempLogFileTwo);
        Button buttonThree = new Button(sysControl,buttonThreePin,logicThree,ledThree,muxController,3,tempLogFileThree);
        Button buttonFour = new Button(sysControl,buttonFourPin,logicFour,ledFour,muxController,4,tempLogFileFour);
        
        System.out.println("\nSYSTEM - Element initiation completed"
                + "\n"
                + "\nSystem Initiated. Enter 'd' to enter debug mode, Enter 'q' to quit.");
        
        while(menu.equalsIgnoreCase("q") == false){
            menu = scanner.next();
            sysControl.systemOutput = true;
            while(menu.equalsIgnoreCase("d") == true){
                sysControl.systemOutput = false;
                System.out.println("\nDebug Menu:"
                        + "\n1) Change upper threshold temperature"
                        + "\n2) Change lower threshold temperature"
                        + "\n3) Change incubation time"
                        + "\n4) change mux Lockout time"
                        + "\n5) Exit debug menu");
                menu = scanner.next();
                if(menu.equals("1") == true){
                    System.out.println("\nPlease enter new Upper threshold temperature\n");
                    sysControl.upperThresholdTemp = scanner.nextFloat();
                    System.out.println("\nNew Upper threshold is " + sysControl.upperThresholdTemp);
                    menu = "d";
                }else if(menu.equals("2") == true){
                    System.out.println("\nPlease enter new Lower threshold temperature\n");
                    sysControl.lowerThresholdTemp = scanner.nextFloat();
                    System.out.println("\nNew Lower threshold is " + sysControl.lowerThresholdTemp);
                    menu = "d";
                }else if(menu.equals("3") == true){
                    System.out.println("\nPlease enter a new incubation time\n");
                    sysControl.incubateTime = scanner.nextInt();
                    System.out.println("\nNew Incubate time is " + sysControl.incubateTime);
                    menu = "d";
                }else if(menu.equals("4") == true){
                    System.out.println("\nPlease enter a new Mux lockout time\n");
                    sysControl.muxLockoutTime = scanner.nextInt();
                    System.out.println("\nNew Mux Lockout time is " + sysControl.muxLockoutTime);
                    menu = "d";
                }else if(menu.equals("5") == true){
                    System.out.println("\nExiting debug menu");
                    menu = " ";
                    sysControl.systemOutput = true;
                }else{
                    System.out.println("\nInvalid menu option, please try again.\n");
                    menu = "d";
                }
            }
        }
    }
    
}
