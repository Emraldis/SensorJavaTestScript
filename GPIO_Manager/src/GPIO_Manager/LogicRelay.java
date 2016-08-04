/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPIO_Manager;

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
    SystemController sysData;
    DelayManager timer = new DelayManager();
    boolean inUse = false;
    boolean triggered = false;
    StringTokenizer strTok;
    float temp = 0;
    long startTime;
    long currentTime;
    /*-----------------------|Basic Constructor|-----------------------*/

    public LogicRelay(GpioPinDigitalOutput outputOne, GpioPinDigitalOutput outputTwo, SystemController sysData) {
        pinOne = outputOne;
        pinTwo = outputTwo;
        pinOne.low();
        pinTwo.low();
        this.sysData = sysData;
    }
    /*-----------------------|Incubation Function|-----------------------*/

    public void incubate(int inductionTime, int respondTime, LedIndicator led, String tempLogFile, int ID) throws InterruptedException {
        long tempLong = 0;
        boolean check = true;
        String input;
        String tempString = " ";
        long tempStartTime;
        long tempCurrentTime;
        if (!inUse) {
            System.out.println("\nSYSTEM - Beginning Induction of sample " + ID + ", " + (sysData.inductionTime) + " seconds remaining.");
            sysData.buttonControl = false;
            inUse = true;
            startTime = System.currentTimeMillis();
            currentTime = startTime;
            while ((currentTime < (startTime + ((inductionTime + respondTime) * 1000))) && !sysData.shutdown) {
                tempStartTime = System.currentTimeMillis();
                tempCurrentTime = tempStartTime;
                /*-----------------------|Code for getting temp data from file|-----------------------*/
                if ((i >= sysData.voltammetryTime) && !sysData.buttonControl && !this.triggered) {
                    sysData.buttonControl = true;
                    this.triggered = true;
                    System.out.println("\nButton Lockdown Dropped");
                }
                try {
                    tempRead = new Scanner(new FileInputStream(tempLogFile));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LogicRelay.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("\nCritical Error: temperature probe data file not found");
                }
                while (tempRead.hasNextLine()) {
                    input = tempRead.nextLine();
                    strTok = new StringTokenizer(input, " ");
                    while (strTok.hasMoreTokens()) {
                        tempString = strTok.nextToken();
                    }
                    if ((tempString.equalsIgnoreCase("YES")) && (tempString != null)) {
                        input = tempRead.nextLine();
                        strTok = new StringTokenizer(input, "=");
                        while (strTok.hasMoreTokens()) {
                            tempString = strTok.nextToken();
                        }
                        tempLong = Long.parseLong(tempString);
                        temp = tempLong / 1000;
                    } else {
                        System.out.println("\nSYSTEM - Error Reading Temperature @ t=" + ((startTime - currentTime) / 1000)
                                + "\nTemperature readout was: " + tempString);
                    }
                }
                if (((startTime - currentTime) / 1000) < sysData.voltammetryTime) {
                    led.toggle();
                } else if (((startTime - currentTime) / 1000) >= sysData.voltammetryTime) {
                    led.ledController.high();
                }
                tempCurrentTime = System.currentTimeMillis();
                System.out.println("\n" + tempStartTime + ":" + tempCurrentTime);
                while (tempCurrentTime < (tempStartTime + 1000)) {
                    Thread.sleep(1);
                    tempCurrentTime = System.currentTimeMillis();
                    if (tempCurrentTime >= (tempStartTime + 1000)) {
                        System.out.println("\n" + tempStartTime + ":" + tempCurrentTime);
                    }
                }
                pinOne.low();
                pinTwo.low();
                /*-----------------------|Smart Temp Feedback Code|-----------------------*/
                if (((startTime - currentTime) / 1000) < inductionTime) {
                    /*-----------------------|Beggining Induction|-----------------------*/
                    if (temp < sysData.inductionTemp) {
                        if (sysData.systemOutput) {
                            System.out.println("\nSYSTEM - Current temperature: " + temp + " degrees Celcius");
                            System.out.println("\nSYSTEM - Temperature too low, adjusting.");
                        }
                        pinOne.high();
                        pinTwo.low();
                    } else if (temp > sysData.inductionTemp) {
                        if (sysData.systemOutput) {
                            System.out.println("\nSYSTEM - Current temperature: " + temp + " degrees Celcius");
                            System.out.println("\nSYSTEM - Temperature too high, adjusting.");
                        }
                        pinOne.low();
                        pinTwo.high();
                    }
                    if (sysData.systemOutput) {
                        if ((((inductionTime - ((startTime - currentTime) / 1000))) % 10) == 0) {
                            System.out.println("\nSYSTEM - Sample " + ID + " Induction time remaining: " + ((inductionTime - ((startTime - currentTime) / 1000))));
                        }
                    }
                } else if ((((startTime - currentTime) / 1000) < (inductionTime + respondTime)) && (i > inductionTime)) {
                    /*-----------------------|Beginning Respond|-----------------------*/
                    if (temp < sysData.respondTemp) {
                        if (sysData.systemOutput) {
                            System.out.println("\nSYSTEM - Current temperature: " + temp + " degrees Celcius");
                            System.out.println("\nSYSTEM - Temperature too low, adjusting.");
                        }
                        pinOne.high();
                        pinTwo.low();
                    } else if (temp > sysData.respondTemp) {
                        if (sysData.systemOutput) {
                            System.out.println("\nSYSTEM - Current temperature: " + temp + " degrees Celcius");
                            System.out.println("\nSYSTEM - Temperature too high, adjusting.");
                        }
                        pinOne.low();
                        pinTwo.high();
                    }
                    if (sysData.systemOutput) {
                        if (((((inductionTime - ((startTime - currentTime) / 1000))) % 10) == 0)) {
                            System.out.println("\nSYSTEM - Sample " + ID + " Respond time remaining: " + ((respondTime - ((startTime - currentTime) / 1000) + inductionTime)));
                        }
                    }
                }
                if ((((startTime - currentTime) / 1000) > inductionTime) && check) {
                    check = false;
                    System.out.println("\nSYSTEM - Sample " + ID + " Induction completed");
                }
                //timer.waitSeconds(1);
                //System.out.println("\nTICK");
            }
            System.out.println("\nSYSTEM - Sample " + ID + "incubation completed, setting MUX");
            this.shutDown();
            inUse = false;
            this.triggered = false;
        } else {
            System.out.println("\nERROR - Incubation Already Started");
        }
    }

    public void interrogate(String tempLogFile) {
        String input;
        String tempString = " ";
        long tempLong = 0;

        if (!inUse) {
            try {
                tempRead = new Scanner(new FileInputStream(tempLogFile));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LogicRelay.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("\nCritical Error: temperature probe data file not found");
            }
            System.out.println();
            while (tempRead.hasNextLine()) {
                input = tempRead.nextLine();
                strTok = new StringTokenizer(input, " ");
                while (strTok.hasMoreTokens()) {
                    tempString = strTok.nextToken();
                }
                if ((tempString.equalsIgnoreCase("YES")) && (tempString != null)) {
                    input = tempRead.nextLine();
                    strTok = new StringTokenizer(input, "=");
                    while (strTok.hasMoreTokens()) {
                        tempString = strTok.nextToken();
                    }
                    tempLong = Long.parseLong(tempString);
                    temp = tempLong / 1000;
                } else {
                    System.out.println("\nError Reading Temperature");
                }
            }
            System.out.println("\nNo Sample. Ambient Temperature: " + temp);
        } else {
            System.out.println("\nTemperature:" + temp);
        }
    }

    public void shutDown() throws InterruptedException {
        pinTwo.high();
        timer.waitSeconds(1);
        pinOne.low();
        pinTwo.low();
    }
}
