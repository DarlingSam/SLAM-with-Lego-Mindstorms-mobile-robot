import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
public class SamMain {	
	public static void main(String[] args) {
		
		PilotRobot pilot = new PilotRobot();
		
	    
//      PilotMonitor pilotMon = new PilotMonitor(pilot, 300);
//      pilotMon.start();
		
		
		Behavior b1 = new ShutDownB(pilot);
		Behavior b2 = new CorrectOnBlackB(pilot);
		Behavior b3 = new MoveForwardB(pilot);
		Behavior b4 = new DetectWallB(pilot);
		
//		VisualDisplay display = new VisualDisplay(pilot, 400, b4);
//		display.run();
		
		
//		priority is right to left
		Behavior[] bArray = {b3, b2, b4, b1};
		Arbitrator arb = new Arbitrator(bArray, false);
		arb.go();	
	}
}
