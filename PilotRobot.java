import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

// PilotRobot.java

public class PilotRobot {
	
    public enum compass {
    	NORTH, EAST, SOUTH, WEST;
    }
    
    compass direction = compass.NORTH;
	private EV3UltrasonicSensor usSensor;
	private EV3ColorSensor leftCSensor, rightCSensor;
	private EV3GyroSensor gSensor;
	private SampleProvider leftCSP, rightCSP, uSP, gyroSP;	
	private float[] leftCSample, rightCSample, usSample, gyroSample; 
	private MovePilot pilot;
	static GraphicsLCD lcd;
	int posX = 0, posY = 0;

	public PilotRobot() {
		Brick myEV3 = BrickFinder.getDefault();
//		GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();

        leftCSensor = new EV3ColorSensor(myEV3.getPort("S1"));
        rightCSensor = new EV3ColorSensor(myEV3.getPort("S4"));
        leftCSP = leftCSensor.getRGBMode();
        rightCSP = rightCSensor.getRGBMode();
        leftCSample = new float[leftCSP.sampleSize()];
        rightCSample = new float[rightCSP.sampleSize()];
		
		usSensor = new EV3UltrasonicSensor(myEV3.getPort("S3"));
		gSensor = new EV3GyroSensor(myEV3.getPort("S2"));
		uSP = usSensor.getDistanceMode();
		gyroSP = gSensor.getAngleMode();
		usSample = new float[uSP.sampleSize()];
		gyroSample = new float[gyroSP.sampleSize()];

		Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 4.05).offset(-4.9);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 4.05).offset(4.9);
		
		Chassis myChassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);

	    pilot = new MovePilot(myChassis);
	    
		gSensor.reset();
	}
	
	public float getDistance() {
    	uSP.fetchSample(usSample, 0);
    	return usSample[0];
	}
	
    public boolean wallCheck() {
        uSP.fetchSample(usSample, 0);
        float distance = usSample[0];
        if (distance <= 0.125) {
            return true;
        }
        return false;
    }
	
    public void courseCorrect(double angle) {
        gyroSP.fetchSample(gyroSample, 0);
        double deviation = gyroSample[0];
        if(deviation == 0) {
        	gSensor.reset();
        	return;
        }
        if(deviation > (angle) || deviation < (angle) && (deviation - angle < 1 || deviation - angle > 1)) {
//        	System.out.println("CourseCorrect");
//        	System.out.println(deviation - angle * -1);
        	pilot.rotate((deviation - angle) * - 1);
        	gSensor.reset();
        	return;
        }
    }
        
    public void travelAmount(double amount) {
//    	pilot.setLinearAcceleration(2);
    	pilot.setLinearSpeed(6);
    	pilot.travel(amount);
    	courseCorrect(0);
    }
	
	
	public void turnHeadTo(int amount) {
		Motor.C.rotateTo(amount);
	}
    
    public boolean leftDetectBlack() {
		leftCSP = leftCSensor.getColorIDMode();
		leftCSample = new float[leftCSP.sampleSize()];
		int currentDetectedColour = leftCSensor.getColorID();
		if(currentDetectedColour == Color.BLACK) {
			return true;
		}
		return false;
    }
    

    
    public boolean rightDetectBlack() {
		rightCSP = rightCSensor.getColorIDMode();
		rightCSample = new float[rightCSP.sampleSize()];
		int currentDetectedColour = rightCSensor.getColorID();
		if(currentDetectedColour == Color.BLACK) {
			return true;
		}
		return false;
    }
  
  public void turnAmount(double amount) {
	  pilot.setAngularSpeed(20);
	  pilot.rotate(amount);
	  courseCorrect(amount);
  }
  
  public void turn90Left() {
	  pilot.setAngularSpeed(20);
	  pilot.rotate(90);
	  courseCorrect(90);
	  setDirection(true);		
  }
  
  public void setDirection(boolean LorR) {
//		LorR is true for if it turns left and false if it turns right
	  switch(direction) {
	  case NORTH:
		  if(LorR) {
			  direction = compass.WEST;
		  }
		  else {
			  direction = compass.EAST;
		  }
		  break;
	  case EAST:
		  if(LorR) {
			  direction = compass.NORTH;
		  }
		  else {
			  direction = compass.SOUTH;
		  }
		  break;
	  case SOUTH:
		  if(LorR) {
			  direction = compass.EAST;
		  }
		  else {
			  direction = compass.WEST;
		  }
		  break;
	  
	  case WEST:
		  if(LorR) {
			  direction = compass.SOUTH;
		  }
		  else {
			  direction = compass.NORTH;
		  }
		  break;
	  }
  }
  
  public void turn90Right() {
	  pilot.setAngularSpeed(20);
	  pilot.rotate(-90);
	  courseCorrect(-90);
	  setDirection(false);
  }
  
  public void moveForward() {
	  pilot.setLinearSpeed(6);
	  pilot.forward();
  }
  
  public void stopRobot() {
	  pilot.stop();
  }
	
	public MovePilot getPilot() {
		return pilot;
	}
	
	public void closeRobot() {
		leftCSensor.close();
		rightCSensor.close();
		usSensor.close();
		gSensor.close();
	}
}