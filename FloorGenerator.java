import java.awt.*;

public class FloorGenerator extends Object{

	public static int RIGHT = 0;
	public static int UP = 1;
	public static int LEFT = 2;
	public static int DOWN = 3;
	private int dir = RIGHT;

	public FloorGenerator(int x, int y, int dir){
		super(x,y,32,32);
		this.dir = dir;
	}

	public void changeDir(int ch){
		dir += ch;
		if (dir < 0)
			dir += 4;
		if (dir > 3)
			dir -= 4;
	}

	public void move(){
		if (dir == RIGHT)
			moveX(32);
		if (dir == UP)
			moveY(-32);
		if (dir == LEFT)
			moveX(-32);
		if (dir == DOWN)
			moveY(32);
	}
}