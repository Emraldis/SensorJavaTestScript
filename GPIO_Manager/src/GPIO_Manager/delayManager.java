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

    LocalTime timer;

    public DelayManager() {
    }

    public void waitSeconds(int seconds) throws InterruptedException {
        int second;
        int start;
        start = timer.getSecond();
        second = start;
        while(second < (start + seconds)){
            Thread.sleep(1);
            second = timer.getSecond();
        }
    }

}
