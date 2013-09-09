import java.awt.*;

public class Weapon extends Object{

	private int type;
	public static final int PISTOL = 1;
	public static final int SHOTGUN = 2;
	public static final int MACHINEGUN = 3;
	public static final int BULLETS = 0;
	public static final int SHELLS = 1;
	public boolean drawText;
	private String name;

	public Weapon(int x, int y, int type){
		super(x,y,12,12);
		this.type = type;
		drawText = false;
		if (type == 1)
			name = "PISTOL";
		if (type == 2)
			name = "SHOTGUN";
		if (type == 3)
			name = "MACHINEGUN";	
	}

	public int getType(){
		return type;
	}

	public static int getAmmoType(int type){
		if (type == PISTOL || type == MACHINEGUN)
			return BULLETS;
		else if (type == SHOTGUN)
			return SHELLS;
		return 0;
	}

	public static String getAmmoName(int type){
		if (type == BULLETS)
			return "Bullets";
		else if (type == SHELLS)
			return "Shells";
		return "Empty";
	}

	public static String getName(int type){
		if (type == PISTOL)
			return "PISTOL";
		else if (type == SHOTGUN)
			return "SHOTGUN";
		else if (type == MACHINEGUN)
			return "MACHINEGUN";
		return "";
	}

	public void paint(Graphics g){
		g.setColor(new Color(230,230,230));
		if (drawText)
			g.drawString("Press E to pickup "+name,getX(),getY()-24);
		g.setColor(Color.green);
		g.drawRoundRect(getX()-getWidth()/2,getY()-getHeight()/2,getWidth(),getHeight(),4,4);
	}
}