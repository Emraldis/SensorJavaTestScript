/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPIO_Manager;

/**
 *
 * @author Alfie Feltham
 */
/*-----------------------|Basic Global System Variables|-----------------------*/
public class SystemController {
    boolean buttonControl;
    boolean muxControl;
    int inductionTime;
    int respondTime;
    int muxLockoutTime;
    float respondTemp;
    float inductionTemp;
    boolean systemOutput;
    public void lockButtons() throws InterruptedException{
        int i;
        this.buttonControl = false;
        for(i=0;i<this.muxLockoutTime;i++){
            Thread.sleep(1);
        }
        this.buttonControl = true;
        System.out.println("\nButton Lockdown Dropped");
    }
}
