import java.awt.*;

public class Bullet extends Object{

	private double dir;
	private int speed;
	private boolean user;

	public Bullet(int x, int y, int speed, double dir, boolean user){
		super(x,y,8,8,2000);
		this.speed = speed;
		this.dir = dir;
		this.user = user;
	}

	public void move(){
		setdx(lengthdir_x(speed,dir));
		setdy(lengthdir_y(speed,dir));
		super.move();
		changeLife(-1);
	}

	public double getDir(){
		return dir;
	}

	public boolean getUser(){
		return user;
	}

	public void paint(Graphics g){
		g.setColor(Color.orange);
		g.drawRect((getX()-5),(getY()-5),10,10);
		g.setColor(new Color(220,220,210));
		g.drawRect((getX()-4),(getY()-4),8,8);
	}
}