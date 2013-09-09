import java.awt.*;

public class Floor extends Object{
	
	public Floor(int x, int y){
		super(x,y,32,32);
	}

	public void paint(Graphics g){
		g.setColor(new Color(145,165,165));
		g.fillRect((getX()-16),(getY()-16),32,32);
	}
}