import java.awt.*;

public class Player extends Object{
	
	private boolean up, down, left, right;
	public double acceleration = 0.5;
	public double mdir;
	public int[] weapon = new int[2];
	private int recoil = 0;
	private int maxLife;
	private boolean firing;
	private int hit;
	public int[] ammo = new int[2];

	public Player(int x, int y, int life){
		super(x,y,16,16,life);
		maxLife = life;
		up = false;
		down = false;
		left = false;
		right = false;
		firing = false;
		hit = 0;
		weapon[0] = 1;
		weapon[1] = 0;
		ammo[0] = 135;
		ammo[1] = 0;
	}

	public void addAmmo(int type, int amnt){
		ammo[type] += amnt;
	}

	public int getWeapon(int index){
		return weapon[index];
	}

	public boolean getFiring(){
		return firing;
	}

	public void setFiring(boolean firing){
		this.firing = firing;
	}

	public void swapWep(){
		if (weapon[1] != 0){
			int tempWep = weapon[0];
			weapon[0] = weapon[1];
			weapon[1] = tempWep;
		}
	}

	public void hit(){
		hit = 10;
	}

	public boolean isHit(){
		return (hit > 0);
	}

	public int getRecoil(){
		return recoil;
	}

	public void setRecoil(int recoil){
		this.recoil = recoil;
	}

	public void setMove(double pDir){
		if (hit > 0)
			hit -= 1;
		if (recoil > 0)
			recoil -= 1;

		if (getLife() < 1){
			left = false;
			right = false;
			up = false;
			down = false;
			setFiring(false);
		}
		if (getLife() > maxLife)
			setLife(maxLife);

		if (left)
			if (getdx() > -4)
				changedx(-acceleration);

		if (right)
			if (getdx() < 4)
				changedx(acceleration);

		if (up)
			if (getdy() > -4)
				changedy(-acceleration);

		if (down)
			if (getdy() < 4)
				changedy(acceleration);

		if (!down && !up){
			if (getdy() > 0)
				changedy(-acceleration);
			if (getdy() < 0)
				changedy(acceleration);
		}

		if (!left && !right){
			if (getdx() > 0)
				changedx(-acceleration);
			if (getdx() < 0)
				changedx(acceleration);
		}

		if (getdx() > 4)
			setdx(4);
		if (getdy() > 4)
			setdy(4);
		if (getdx() < -4)
			setdx(-4);
		if (getdy() < -4)
			setdy(-4);
/*		if (Math.abs(getdx())<acceleration)
			setdx(0);
		if (Math.abs(getdy())<acceleration)
			setdy(0);
*/
		//super.move();
	}

	public void setUp(boolean t){
		up = t;
	}
	public void setDown(boolean t){
		down = t;
	}
	public void setLeft(boolean t){
		left = t;
	}
	public void setRight(boolean t){
		right = t;
	}

	public int getMaxLife(){
		return maxLife;
	}

	public int pickupWeapon(int ptype){
		int returnType = 0;
		if (weapon[1] == 0){
			weapon[1] = weapon[0];
			weapon[0] = ptype;
		}
		else{
			returnType = weapon[0];
			weapon[0] = ptype;
		}
		return returnType;
	}

	public void paint(Graphics g){
		g.setColor(Color.blue);
		if (hit > 0)
			g.setColor(Color.white);
		g.drawRoundRect((getX()-8),(getY()-8),16,16,5,5);
		g.setColor(Color.blue.darker());
		if (getLife() > 0){
			g.drawLine(getX(),getY(),(getX()+(int)lengthdir_x(12,mdir)),(getY()+(int)lengthdir_y(12,mdir)));
			/*
			switch(weapon[0]){
				case Weapon.PISTOL:
				g.drawLine(getX(),getY(),(getX()+(int)lengthdir_x(12,mdir)),(getY()+(int)lengthdir_y(12,mdir)));
				break;
				case Weapon.SHOTGUN:
				g.drawLine(getX(),getY(),(getX()+(int)lengthdir_x(12,mdir)),(getY()+(int)lengthdir_y(12,mdir)));
				break;
			}
			*/
			g.setColor(new Color (220,230,220));
			g.drawString("[ "+Weapon.getName(weapon[0])+" ]",20,72);
			g.drawString("[ "+Weapon.getName(weapon[1])+" ]",20,94);
			g.drawString("Bullets: "+ammo[0]+" | Shells: "+ammo[1],20,98+18);
		}
	}
}