import lejos.robotics.subsumption.Behavior;

public class DetectWallB implements Behavior {
	public boolean suppressed;
	private PilotRobot robot;
	private boolean[] leftRight = new boolean[2];
	
    public void checkLandR() {
        robot.turnHeadTo(90);
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        if (!robot.wallCheck()) {
            leftRight[1] = true;
        }
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        robot.turnHeadTo(-90);
        if (!robot.wallCheck()) {
            leftRight[1] = true;
        }
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        robot.turnHeadTo(0);
    }
	
    public void suppress(){
    	suppressed = true;
    }
    
    public DetectWallB(PilotRobot r){
    	robot = r;
    }
    
    public void action() {
    	suppressed = false;
//		System.out.println("On DetectWallB");
//		robot.directionDecision(robot.checkLandR());
    	checkLandR();
	      if (leftRight[1]) {
	          robot.turn90Right();
	      } else if (leftRight[0]) {
	          robot.turn90Left();
	      } else {
	          robot.travelAmount(-12.25);
	      }
		
		
    	
    }
    
    public boolean takeControl() {
    	return robot.wallCheck();
    }

}