import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.robotics.subsumption.Behavior;

public class ShutDownB implements Behavior{
	public boolean suppressed;
	private PilotRobot robot;
	
    public void suppress(){
    	suppressed = true;
    }
    public ShutDownB(PilotRobot r){
    	robot = r;
    }
    public void action() {
//    	System.out.println("Closing program");
    	robot.closeRobot();
    	System.exit(0);
    }
    
    public boolean takeControl() {
    	return(Button.ESCAPE.isDown());
    }
}
