import java.awt.event.*; 
import java.awt.*;
import javax.swing.Timer;
import java.util.Random;

public class Enemy extends Object{

	private double dir;
	private boolean moving;
	private int type;
	public static final int GUNNER = 1;
	public static final int MAGGOT = 2;
	public static final int HOUSE = 3;
	private Timer mover, fire;
	private Random rand;
	private double speed;
	private boolean firing;
	private int hit;
	private int maxSpeed;
	private double acceleration;

	public Enemy(int x, int y, int type, int life, int mult){
		super(x,y,16,16,life);
		maxSpeed = 2;
		acceleration = .1;
		if (type != GUNNER){
			if (type == MAGGOT){
				maxSpeed = 3;
				setWidth(12);
				setHeight(8);
				acceleration = .05;
			}
			else if (type == HOUSE){
				maxSpeed = 0;
				setWidth(24);
				setHeight(24);
			}
		}
		this.type = type;
		moving = false;
		if (type == 2)
			moving = true;
		rand = new Random(mult);
		if (type != 2 && type != 3){
			mover = new Timer(1000 + rand.nextInt(1000),new Mover());
			fire = new Timer(3*1000 + rand.nextInt(5)*1000,new Fire());
			mover.start();
			fire.start();
		}
		dir = 0;
		speed = -0.1;
		hit = 0;
	}

	public void hit(){
		if (getLife() > 0)
			hit = 8;
	}

	public boolean isHit(){
		return (hit > 0);
	}

	public void setMove(double pDir){
		if (type == 2)
			dir = pDir;
		if (hit > 0)
			hit -= 1;
		if (moving){
			if (speed < maxSpeed)
				speed += acceleration;
		}
		else{
			if (speed > 0)
				speed -= acceleration;
		}
		if (speed > 0){
			setdx(lengthdir_x((int)Math.ceil(speed),dir));
			setdy(lengthdir_y((int)Math.ceil(speed),dir));
		}
		else{
			if (Math.abs(getdx()) < .3 || maxSpeed == 0)
				setdx(0);
			if (Math.abs(getdy()) < .3 || maxSpeed == 0)
				setdy(0);
			if (getdx() != 0)
				changedx(-getdx()/2);
			if (getdy() != 0)
				changedy(-getdy()/2);
		}
	}

	private class Mover implements ActionListener{
		public void actionPerformed(ActionEvent arg){
			mover.stop();
			if (moving){
				moving = false;
				mover.setDelay(1000*5);
			}
			else{
				dir = (Math.PI/6) * rand.nextInt(12);
				//System.out.println(dir);
				if (rand.nextInt(5) < 3)
					moving = true;
				mover.setDelay(rand.nextInt(1000));
			}
			mover.start();
		}
	}

	public int getType(){
		return type;
	}

	private class Fire implements ActionListener{
		public void actionPerformed(ActionEvent arg){
			setFiring(true);
			fire.stop();
			fire.setDelay(6*1000+1000*rand.nextInt(3));
			fire.start();
		}
	}

	public boolean getFiring(){
		return firing;
	}

	public void setFiring(boolean firing){
		this.firing = firing;
	}

	public void paint(Graphics g){
		g.setColor(new Color(255,40,40));
		if (hit > 0)
			g.setColor(Color.white);
		//g.drawLine(getX(),getY(),getX()+(int)lengthdir_x(12,mdir),getY()+(int)lengthdir_y(12,mdir));
		switch (type){
			case HOUSE:
			if (hit == 0)
				g.setColor(new Color(255,40,105));
			case MAGGOT:
			if (hit == 0 && type != HOUSE)
				g.setColor(new Color(225,100,40));
			case GUNNER:
			g.drawRect(getX()-getWidth()/2,getY()-getHeight()/2,getWidth(),getHeight());
			g.drawRect(getX()-getWidth()/2+2,getY()-getHeight()/2+2,getWidth()-4,getHeight()-4);
			break;
		}
	}
}