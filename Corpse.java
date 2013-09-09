import java.awt.*;

public class Corpse extends Object{

	private int type;

	public Corpse(int x, int y, int type, int width, int height){
		super(x,y,16,16);
		this.type = type;
		setWidth(width);
		setHeight(height);
	}

	public void paint(Graphics g){
		g.setColor(new Color(255,120,120));
		switch (type){
			case 3:
			g.setColor(new Color(255,100,255));
			case 2:
			case 1:
			g.drawRect(getX()-getWidth()/2+2,getY()-getHeight()/2+2,getWidth()-4,getHeight()-4);
			break;
		}
	}
}