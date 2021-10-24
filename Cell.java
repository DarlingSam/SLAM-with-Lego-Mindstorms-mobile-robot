
public class Cell {
	private int x;
	private int y;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int[] getCoord() {
		int[] A = {x,y};
		return A;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
}