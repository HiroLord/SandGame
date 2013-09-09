import java.awt.*;

public class Text extends Object{

	private String text;

	public Text(String text, int x, int y, int life){
		super(x,y,1,1,life);
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void move(){
		changeLife(-1);
		if (getLife() > 30)
			setdy(-0.4);
		else
			setdy(0);
		super.move();
	}

	public void paint(Graphics g){
		//FontMetrics fm  = g.getFontMetrics(FONT);
		g.setColor(new Color(220,230,220));
		g.drawString(text,getX()/*-fm.stringWidth(text)*/,getY());
	}
}