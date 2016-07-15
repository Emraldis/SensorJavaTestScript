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
    boolean shutdown;
    
    LedIndicator ledOne;
    LedIndicator ledTwo;
    LedIndicator ledThree;
    LedIndicator ledFour;
    
    public void AddIndicators (LedIndicator ledOne, LedIndicator ledTwo, LedIndicator ledThree, LedIndicator ledFour){
        this.ledOne = ledOne;
        this.ledTwo = ledTwo;
        this.ledThree = ledThree;
        this.ledFour = ledFour;
    }
    
    public void ControlLight (int target, boolean setting){
        switch(target){
            case 1:
                if(setting){
                    ledOne.ledController.high();
                }else{
                    ledOne.ledController.low();
                }
                break;
            case 2:
                if(setting){
                    ledTwo.ledController.high();
                }else{
                    ledTwo.ledController.low();
                }
                break;
            case 3:
                if(setting){
                    ledThree.ledController.high();
                }else{
                    ledThree.ledController.low();
                }
                break;
            case 4:
                if(setting){
                    ledFour.ledController.high();
                }else{
                    ledFour.ledController.low();
                }
                break;
        }
    }
}
