import java.awt.*;

public class Chest extends Object{

	private int type;
	private boolean open;

	public Chest(int x, int y, int type){
		super(x,y,16,16);
		this.type = type;
		open = false;
	}

	public void setOpen(boolean open){
		this.open = open;
	}

	public boolean getOpen(){
		return open;
	}

	public void paint(Graphics g){
		if (!open)
			g.setColor(Color.yellow);
		else{
			g.setColor(Color.yellow.darker());
			g.drawRoundRect(getX()-getWidth()/2,getY()-getHeight()/2-4,getWidth(),getHeight()+4,5,5);
		}
		g.drawRoundRect(getX()-getWidth()/2,getY()-getHeight()/2,getWidth(),getHeight(),5,5);
		g.drawRoundRect(getX()-getWidth()/2,getY()-getHeight()/2,getWidth(),getHeight()/3,5,5);
	}
}