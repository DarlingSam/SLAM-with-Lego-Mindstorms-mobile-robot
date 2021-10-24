import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.subsumption.Behavior;

public class VisualDisplay extends Thread {
	
	private int delay;
	public PilotRobot robot;
	static List<Cell> searchedList = new ArrayList<Cell>();
	static List<Cell> obsList = new ArrayList<Cell>();
	static int posX = 0;
	static int posY = 0;
	static int direction = 0;
	static DataOutputStream dOut;
	
	public static final int port = 1234;
    static GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();
	private ServerSocket server;
	
	Behavior DWB;
	
    // Make the monitor a daemon and set
    // the robot it monitors and the delay
    public VisualDisplay(PilotRobot r, int d, Behavior b4) {
    	this.setDaemon(true);
    	this.DWB = b4;
    	this.delay = d;
    	this.robot = r;
    }

    // The monitor writes various bits of robot state to the screen, then
    // sleeps.
    public void run(){
		try {
			server = new ServerSocket(port);
			System.out.println("Awaiting client..");
			Socket client = server.accept();
			System.out.println("CONNECTED");
			OutputStream out = client.getOutputStream();
			dOut = new DataOutputStream(out);
			dOut.writeUTF("");
			dOut.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    	while(true){
    		lcd.clear();
    		lcd.setFont(Font.getDefaultFont());
    		lcd.drawString("Robot Monitor", lcd.getWidth()/2, 0, GraphicsLCD.HCENTER);
    		lcd.setFont(Font.getSmallFont());   

    		try {
				sendObstacles();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
    		generateGrid();
    		
    		try {
				dOut.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		
    		try{
    			sleep(delay);
    		}
    		catch(Exception e){
    			;
    		}
	    }
    }
    
	public static void sendObstacles() throws IOException {
		int i = 0;
        for (Cell obs: obsList) {
        	i++;
        	dOut.writeUTF("Obs " + i + ": " + obs.getX() + " " + obs.getY());
        	dOut.flush();
        }
	}
	
  	public static void generateGrid() {
  		lcd.clear();
  		for (int i = 1; i <= 111; i = i + 11 ) {
  			horizontalLine(i);
  			verticalLine(i);
  		}
  		lcd.refresh();
  	}
  	
  	public static void horizontalLine(int y) {
  		for (int i = 1; i <= 111; i++) {
  			lcd.setPixel(i,y,1);
  		}
  	}
  	
  	public static void verticalLine(int x) {
  		for (int i = 1; i <= 111; i++) {
  			lcd.setPixel(x,i,1);
  		}
  	}
  	
  	public static void addCell(int x, int y) {
  		generatePixel((11*(x)) + 2, (11*(y)) + 2);
  	}
  	
  	public static void generatePixel(int x, int y) {
  		for (int i = x; i < x+10; i++) {
  			for (int j = y; j < y+10; j++) {
  				lcd.setPixel(i,j,1);
  			}
  		}
  		lcd.refresh();
  	}
	

}