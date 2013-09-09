import java.awt.*;

public class Object{
	
	private double x, y;
	private double dx, dy;
	private int life;
	private int width, height;

	public static Color c_black;
	public static Color c_ltgray;
	public static Color c_blue;
	public static int SCALE;

	public static Font FONT;

	public Object(int x, int y, int width, int height, int life){
		this.x = x;
		this.y = y;
		this.life = life;
		this.width = width;
		this.height = height;
		dx = 0;
		dy = 0;
	}

	public Object(int x, int y, int width, int height){
		this(x,y,width,height,1);
	}

	public void move(){
		moveX(dx);
		moveY(dy);
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getX(){
		return (int)x;
	}

	public int getY(){
		return (int)y;
	}

	public double getdx(){
		return dx;
	}

	public double getdy(){
		return dy;
	}

	public void moveX(double dx){
		x = (x+dx);
	}

	public void moveY(double dy){
		y = (y+dy);
	}

	public void changedx(double ddx){
		dx += ddx;
	}

	public void changedy(double ddy){
		dy += ddy;
	}

	public void setdx(double dx){
		this.dx = dx;
	}

	public void setdy(double dy){
		this.dy = dy;
	}

	public void changeLife(int l){
		life += l;
		if (life < 0)
			life = 0;
	}

	public void setMove(double pDir){
	}

	public void setLife(int l){
		life = l;
		if (life < 0)
			life = 0;
	}

	public int getLife(){
		return life;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public Point coordinates(){
		return new Point((int)x,(int)y);
	}

	public double lengthdir_x(int speed, double dir){
		return Math.cos(dir)*speed;
	}

	public double lengthdir_y(int speed, double dir){
		return -Math.sin(dir)*speed;
	}

	public void paint(Graphics g){
	}
}