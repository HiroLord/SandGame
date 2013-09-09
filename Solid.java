import java.awt.*;
import java.util.Random;

public class Solid extends Object{

	private int id;
	private int dotx, doty;
	private Random rand;

	public Solid(int x, int y, int id){
		super(x,y,32,32);
		this.id = id;
		rand = new Random();
		dotx = rand.nextInt(getWidth());
		doty = rand.nextInt(getHeight());

	}

	public int getID(){
		return id;
	}

	public void paint(Graphics g){
		g.setColor(Color.black);
		//g.drawRoundRect(getX()+dotx-2,getY()+doty-2,3,3,3,3);
	}
}