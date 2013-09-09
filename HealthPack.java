import java.awt.*;

public class HealthPack extends Object{
	public HealthPack(int x, int y){
		super(x,y,12,12);
	}

	public void paint(Graphics g){
		g.setColor(Color.white);
		g.drawRoundRect(getX()+1-getWidth()/2,getY()+1-getHeight()/2,getWidth()-2,getHeight()-2,4,4);
		g.setColor(Color.green);
		g.fillRect(getX()-1,getY()-3,3,7);
		g.fillRect(getX()-3,getY()-1,7,3);
	}
}