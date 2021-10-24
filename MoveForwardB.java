import lejos.robotics.subsumption.Behavior;

public class MoveForwardB implements Behavior {
	public boolean suppressed;
	private PilotRobot robot;
	
   public MoveForwardB(PilotRobot r){
   	robot = r;
   }
   
   public void suppress() {
      suppressed = true;
   }

   public void action() {
     suppressed = false;
//     System.out.println("On MoveForwardB");
     robot.moveForward();
     while(!suppressed ) {
    	 Thread.yield();
     }
     robot.stopRobot();
     }
   // Moves forward whilst neither colour sensor detects black
   public boolean takeControl() {
	      return !robot.wallCheck() && (!robot.rightDetectBlack() && !robot.leftDetectBlack());
	   }
}