import lejos.robotics.subsumption.Behavior;

public class CorrectOnBlackB  implements Behavior {
	public boolean suppressed;
	private PilotRobot robot;
	
   public CorrectOnBlackB(PilotRobot r){
   	robot = r;
   }
   public void suppress() {
      suppressed = true;
   }
   
   // Turns left or right slightly depending on which ever (or both) colour sensor sees the black line
   public void action() {
//     System.out.println("On COBB");
 	while(robot.leftDetectBlack() && !robot.rightDetectBlack()) {
//		System.out.println("Correcting L");
 		
		robot.turnAmount(-1);
	}
	while(robot.rightDetectBlack() && !robot.leftDetectBlack()) {
//		System.out.println("Correcting R");
		robot.turnAmount(1);
	}
	if(robot.rightDetectBlack() && robot.leftDetectBlack() && !robot.wallCheck()) {
//		System.out.println("Both black");
		
		if (robot.direction == robot.direction.NORTH) {
			robot.posY++;
		} else if (robot.direction == robot.direction.WEST) {
			robot.posX--;
		} else if (robot.direction == robot.direction.SOUTH) {
			robot.posY--;
		} else if (robot.direction == robot.direction.EAST){
			robot.posX++;
		}
		robot.travelAmount(17.5);
	}
   }
	
   // Take control if either colour sensor sees the black line
   public boolean takeControl() {
	      return (robot.rightDetectBlack() || robot.leftDetectBlack());
	   }
}