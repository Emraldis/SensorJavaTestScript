/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPIO_Manager;

import java.time.*;

/**
 *
 * @author Alfie Feltham
 */
public class DelayManager {

    Clock timer;

    public DelayManager() {
        
    }

    public void waitSeconds(int seconds) throws InterruptedException {
        long second;
        long start;
        start = timer.millis();
        second = start;
        while(second < (start + (seconds * 1000))){
            Thread.sleep(1);
            second = timer.millis();
        }
    }

}
