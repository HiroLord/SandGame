import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.event.*; 
import java.awt.*;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;

public class Controller extends JComponent implements ActionListener{

	private Timer game;
	private ArrayList<Object> objects;
	private ArrayList<Solid> solids;
	private ArrayList<Object> movingObjects;
	private ArrayList<Bullet> bullets;
	private ArrayList<Chest> chests;
	private ArrayList<Weapon> weapons;
	private ArrayList<HealthPack> healthPacks;
	private Object view;
	private JFrame frame;
	private int windowWidth, windowHeight;
	private int mouse_x, mouse_y;
	private Player player;
	private Random rand;
	public static int SCALE;
	public static boolean levelFinished = false;
	private static Color BG_COLOR1 = new Color(60,60,100);
	private static Color BG_COLOR3 = new Color(100,60,60);
	private static Color BG_COLOR2 = new Color(60,100,60);
	public static int DIFFICULTY;
	private int cLevel;

	public static int RIGHT = 0;
	public static int UP = 1;
	public static int LEFT = 2;
	public static int DOWN = 3;

	public boolean paused = false;
	private boolean focused = true;

	public static Font FONT = new Font("SansSerif",Font.PLAIN,14);

	public Controller(JFrame frame, int scale){
		this.SCALE = scale;
		game = new Timer(15,this);
		rand = new Random();
		objects = new ArrayList<Object>();
		solids = new ArrayList<Solid>();
		movingObjects = new ArrayList<Object>();
		bullets = new ArrayList<Bullet>();
		chests = new ArrayList<Chest>();
		weapons = new ArrayList<Weapon>();
		healthPacks = new ArrayList<HealthPack>();

		DIFFICULTY = 0;

		player = new Player(64,64,8);
		objects.add(player);
		movingObjects.add(player);

		view = new Object(64,64,16,16);
		objects.add(view);

		generateMap();
		setFocusable(true);
		addKeyListener(new PlayerControls());
		addMouseListener(new MouseControls());
		this.frame = frame;
		game.start();
	}

	public void reset(){
		clearLevel();
		DIFFICULTY = 0;
		objects.remove(player);
		movingObjects.remove(player);
		player = null;
		player = new Player(64,64,8);
		player.setLife(8);
		generateMap();
	}

	public void generateMap(){
		DIFFICULTY += 1;
		cLevel = DIFFICULTY;
		while (cLevel > 9)
			cLevel -= 9;
		cLevel = (int)Math.ceil(cLevel/3.0);

		player.setX(64);
		player.setY(64);
		ArrayList<FloorGenerator> floorGen = new ArrayList<FloorGenerator>();
		ArrayList<Floor> floors = new ArrayList<Floor>();
		floorGen.add(new FloorGenerator(64,64,rand.nextInt(4)));

		ArrayList<FloorGenerator> toAdd = new ArrayList<FloorGenerator>();
		while (floors.size() < (55*SCALE)){
			for (FloorGenerator f : floorGen){
				boolean addFloor = true;
				for (Floor iF : floors)
					if (checkCollision(iF,f))
						addFloor = false;
				if (addFloor){
					Floor nf = new Floor(f.getX(),f.getY());
					floors.add(nf);
					objects.add(nf);
				}
				f.move();
				int changeDir = rand.nextInt(6);
				changeDir -= 5;
				if (changeDir == -1 || changeDir == 1){
					f.changeDir(changeDir);
				}
				if (rand.nextInt(20) > 17){
					toAdd.add(new FloorGenerator(f.getX(),f.getY(),rand.nextInt(4)));
				}
			}
			for (FloorGenerator tA : toAdd)
				floorGen.add(tA);
			toAdd.clear();
		}

		Enemy enemy;
		Chest chest;

		ArrayList<Enemy> addEnemies = new ArrayList<Enemy>();

		for (Floor fl : floors){
			solids.add(new Solid(fl.getX()+32,fl.getY(),solids.size()));
			solids.add(new Solid(fl.getX()-32,fl.getY(),solids.size()));
			solids.add(new Solid(fl.getX(),fl.getY()+32,solids.size()));
			solids.add(new Solid(fl.getX(),fl.getY()-32,solids.size()));
			int chance = (int)Math.ceil(94.0 / ((DIFFICULTY+29)/30.0));
			int chance2 = (int)Math.ceil(120.0 / ((DIFFICULTY+89)/90.0));

			if (rand.nextInt(100) > chance || movingObjects.size() < 3){
				if (pointDistance(new Point(fl.getX(),fl.getY()), player.coordinates()) >= 150){
					int crX, crY;
					double randDir = (Math.PI/6) * rand.nextInt(12);
					int dist = rand.nextInt(8);
					crX = fl.getX() + (int)lengthdir_x(dist,randDir);
					crY = fl.getY() + (int)lengthdir_y(dist,randDir);
					enemy = new Enemy(crX,crY,1,2,rand.nextInt(1000));
					objects.add(enemy);
					movingObjects.add(enemy);
				}
			}
			else if (rand.nextInt(122) > chance2 || movingObjects.size() < 2){
				if (pointDistance(new Point(fl.getX(),fl.getY()), player.coordinates()) >= 150){
					int crX, crY;
					double randDir = (Math.PI/6) * rand.nextInt(12);
					int dist = rand.nextInt(8);
					crX = fl.getX() + (int)lengthdir_x(dist,randDir);
					crY = fl.getY() + (int)lengthdir_y(dist,randDir);
					enemy = new Enemy(crX,crY,3,3,rand.nextInt(1000));
					objects.add(enemy);
					movingObjects.add(enemy);
				}
			}
			else if (rand.nextInt(100) > 97 && chests.size() < 2){
				if (pointDistance(new Point(fl.getX(),fl.getY()), player.coordinates()) >= 150){
					chest = new Chest(fl.getX(),fl.getY(),1);
					objects.add(objects.size()-2,chest);
					chests.add(chest);
				}
			}
		}

		for (Solid so : solids){
			for (Floor fr: floors){
				if (checkCollision(so,fr))
					so.setLife(0);
			}
		}

		for (Enemy ae : addEnemies){
			boolean toAddE = true;
			for (Solid se : solids){
				if (checkCollision(se,ae) && se.getLife() > 0){
					toAddE = false;
				}
			}
			if (toAddE){
				objects.add(ae);
				movingObjects.add(ae);
			}
		}

		addEnemies.clear();

		for (Solid sd : solids){
			objects.add(sd);
		}
		floorGen.clear();

		player.setX(64);
		player.setY(64);
		bringToFront(player);		
	}

	public void bringToFront(Object obj){
		objects.remove(obj);
		objects.add(obj);
	}

	public void actionPerformed(ActionEvent arg){
		windowWidth  = frame.getContentPane().getWidth();
		windowHeight = frame.getContentPane().getHeight();
		PointerInfo mo = MouseInfo.getPointerInfo();
		Point m = mo.getLocation();
		mouse_x = (int) m.getX() - frame.getX() - (frame.getWidth() - windowWidth)/2;
		mouse_y = (int) m.getY() - frame.getY() - (frame.getHeight() - windowHeight);
		player.mdir = pointDirection(player.coordinates(),new Point(mouse_x,mouse_y));

		double vDir = player.mdir;
		int vDist = (int)pointDistance(player.coordinates(),new Point(mouse_x,mouse_y))/4;
		view.setX(player.getX()+(int)lengthdir_x(vDist,vDir));
		view.setY(player.getY()+(int)lengthdir_y(vDist,vDir));

		paused = true;
		if (mouse_x >= 0 && mouse_y >= 0 && mouse_x <= windowWidth && mouse_y <= windowHeight)
			paused = false;
		if (!paused && focused)
			update();
		repaint();
	}

	public void clearLevel(){
		ArrayList<Object> deleteObjects = new ArrayList<Object>();
		for (Object j : objects){
			if (j instanceof Player){}
			else
				deleteObjects.add(j);
		}
		for (Object o : deleteObjects){
			if (o instanceof Player);
			else
				clearObject(o);
		}
		deleteObjects.clear();
		/*for (Object o : objects){
			if (o instanceof Player){}
			else
				o.setLife(0);
		}*/
	}

	public void clearObject(Object r){
		if (r instanceof Player);
		else{
			objects.remove(r);
			bullets.remove(r);
			solids.remove(r);
			movingObjects.remove(r);
			chests.remove(r);
			weapons.remove(r);
			healthPacks.remove(r);
		}
	}

	private class MouseControls implements MouseListener {
		public void mousePressed(MouseEvent e) {
			if (player.getLife() > 0){
				player.setFiring(true);
			}
		}

		public void mouseReleased(MouseEvent e) {
			player.setFiring(false);
		}

		public void mouseEntered(MouseEvent e) {
			focused = true;
	    }

	    public void mouseExited(MouseEvent e) {
	    	focused = false;
	    }

	    public void mouseClicked(MouseEvent e) {
	    }
	}

	private class PlayerControls implements KeyListener {

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A :
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D :
                    player.setRight(true);
                    break;
                case KeyEvent.VK_W :
                    player.setUp(true);
                    break;
                case KeyEvent.VK_S :
                    player.setDown(true);
                    break;
                case KeyEvent.VK_E:
                	Weapon newWep = null;
                	for (Weapon w: weapons){
                		if (checkCollision(w,player)){
                			int oldWep = player.pickupWeapon(w.getType());
                			if (oldWep != 0){
	                			newWep = new Weapon(w.getX(),w.getY(),oldWep);
								objects.add(newWep);
							}
							w.setLife(0);
                		}
                	}
                	if (newWep != null)
                		weapons.add(newWep);
                	break;
                case KeyEvent.VK_Q:
                	player.swapWep();
                	break;
                case KeyEvent.VK_SPACE :
                	if (player.getLife() < 1){
                		System.exit(0);
                	}
                	break;
                case KeyEvent.VK_ESCAPE :
                	System.exit(0);
                	break;
            }
        }
        public void keyReleased(KeyEvent e) {
        	switch (e.getKeyCode()) {
                case KeyEvent.VK_A :
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D :
                    player.setRight(false);
                    break;
                case KeyEvent.VK_W :
                    player.setUp(false);
                    break;
                case KeyEvent.VK_S :
                    player.setDown(false);
                    break;
            }
        }

        public void keyTyped(KeyEvent e) {
        }

    }

///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////

	public void update(){

		ArrayList<Object> toRemove = new ArrayList<Object>();
		ArrayList<Object> toAddObjects = new ArrayList<Object>();

		levelFinished = true;

		// Health Pakcs
		for (HealthPack h : healthPacks){
			if (checkCollision(h,player)){
				if (h.getLife() > 0){
					player.changeLife(3 + rand.nextInt(3));
					objects.add(new Text("+Health",h.getX(),h.getY(),100));
				}
				h.setLife(0);
			}
		}

		for (Object mp : movingObjects)
			mp.setMove(pointDirection(mp.coordinates(),player.coordinates()));

		/* here */

		// Firing
		if (player.getFiring() && player.getRecoil() == 0){
			Point c = player.coordinates();
			Point d = mouseCoordinates();
			double dir = pointDirection(c,d);
			ArrayList<Bullet> newBullets = new ArrayList<Bullet>();
			int wep = player.getWeapon(0);
			int ammoType = Weapon.getAmmoType(wep);
			boolean hasAmmo = (player.ammo[ammoType] > 0);
			if (wep == Weapon.PISTOL && hasAmmo){
				player.addAmmo(ammoType,-1);
				player.setRecoil(16);
				newBullets.add(new Bullet(player.getX(),player.getY(),8,dir,true));
			}
			else if (wep == Weapon.SHOTGUN && hasAmmo){
				player.addAmmo(ammoType,-1);
				player.setRecoil(60);
				newBullets.add(new Bullet(player.getX(),player.getY(),8,dir,true));
				newBullets.add(new Bullet(player.getX(),player.getY(),8,dir-Math.PI/12,true));
				newBullets.add(new Bullet(player.getX(),player.getY(),8,dir+Math.PI/12,true));
			}
			else if (wep == Weapon.MACHINEGUN && hasAmmo){
				player.addAmmo(ammoType,-1);
				player.setRecoil(7);
				dir -= Math.PI/29;
				dir += Math.PI/29*(rand.nextInt(21)/10.0);
				newBullets.add(new Bullet(player.getX(),player.getY(),7,dir,true));
			}
			for (Bullet b : newBullets){
				objects.add(b);
				bullets.add(b);
			}
		}

		for (Bullet bb : bullets){
			for (Solid s : solids){
				if (checkCollision(s,bb)){
					bb.setLife(0);
				}
			}
			if (bb.getUser() == true){
				for (Object e : movingObjects){
					if (e instanceof Enemy){
						if (checkCollision(bb,e)){
							((Enemy)e).hit();
							bb.setLife(0);
							e.changeLife(-1);
							double bDir = ((Bullet)bb).getDir();
							e.changedx(lengthdir_x(3,bDir));
							e.changedy(lengthdir_y(3,bDir));
						}
					}
				}
			}
			else {
				if (checkCollision(bb,player)){
					bb.setLife(0);
					player.hit();
					player.changeLife(-1);
				}
			}
		}

		for (Object p : movingObjects){
			p.moveX(p.getdx());
			for (Solid s : solids)
				if (checkCollision(s,p)){
					p.moveX(-p.getdx());
					p.setdx(0);
//					p.setdx(-p.getdx()*2/3);
				}
			p.moveY(p.getdy());
			for (Solid s : solids)
				if (checkCollision(s,p)){
					p.moveY(-p.getdy());
					p.setdy(0);
//					p.setdy(-p.getdy()*2/3);
				}
			p.moveX(-p.getdx());
			p.moveY(-p.getdy());
		}

		for (Object i : objects){
			i.move();
			if (i instanceof Enemy){
				if (player.getLife() > 0 && ((Enemy)i).getFiring() && pointDistance(new Point(i.getX(),i.getY()),player.coordinates()) < 260){
					double dir = pointDirection(new Point(i.getX(),i.getY()),player.coordinates());
					dir -= Math.PI/35;
					dir += Math.PI/35*(rand.nextInt(21)/10.0);
					Bullet eb = new Bullet(i.getX(),i.getY(),5,dir,false);
					bullets.add(eb);
					toAddObjects.add(eb);
					((Enemy)i).setFiring(false);
				}
				if (player.getLife() > 0 && ((Enemy)i).getType() == Enemy.MAGGOT && i.getLife() > 0){
					if (checkCollision(i,player)){
						player.changeLife(-2);
						player.hit();
						((Enemy)i).hit();
						i.setLife(0);
					}
				}
				if (((Enemy)i).getType() == Enemy.HOUSE && pointDistance(i.coordinates(),player.coordinates()) < 72){
					if (!((Enemy)i).isHit()){
						((Enemy)i).hit();
						i.changeLife(-1);
					}
				}
			}
			
			if (i.getLife() < 1){
				if (i instanceof Player);
				if (i instanceof Enemy){
					if (!((Enemy)i).isHit())
						toRemove.add(i);
				}
				else 
					toRemove.add(i);
			}
		}

		// Destruction
		HealthPack hp = null;
		for (Object r : toRemove){
			if (r.getLife() < 1){
				if (r instanceof Enemy){
					toAddObjects.add(new Corpse(r.getX(),r.getY(),((Enemy)r).getType(),r.getWidth(),r.getHeight()));
					if (rand.nextInt(100) > 94 && hp == null){
						hp = new HealthPack(r.getX(),r.getY());
					}
					if (((Enemy)r).getType() == 3){
						for (double i = 0; i < Math.PI; i += Math.PI*4/11){
							int crX = (int)(r.getX() + lengthdir_x(8,i));
							int crY = (int)(r.getY() + lengthdir_y(8,i));
							Enemy newMag = new Enemy(crX,crY,2,1,rand.nextInt(1000));
							toAddObjects.add(newMag);
							movingObjects.add(newMag);
						}
					}
				}
				clearObject(r);
				r = null;
			}
		}
		toRemove.clear();
		if (hp != null){
			toAddObjects.add(hp);
			healthPacks.add(hp);
		}

		// Chests
		for (Chest c : chests){
			if (c.getOpen() == false && checkCollision(c,player)){
				c.setOpen(true);
				int newWepType = 1+rand.nextInt(3);
				Weapon newWep = new Weapon(c.getX(),c.getY(),newWepType);
				int ammoAmnt = 25 + rand.nextInt(11);
				player.addAmmo(Weapon.getAmmoType(newWepType),ammoAmnt);
				String addString = String.format("+%d %s",ammoAmnt, Weapon.getAmmoName(Weapon.getAmmoType(newWepType)));
				objects.add(new Text(addString,c.getX(),c.getY(),100));
				objects.add(objects.size()-2,newWep);
				weapons.add(newWep);
			}
		}

		for (Weapon w : weapons){
			w.drawText = false;
			if (w.getType() == player.getWeapon(0) || w.getType() == player.getWeapon(1))
				w.setLife(0);
			if (w.getLife() > 0 && checkCollision(w,player)){
				w.drawText = true;
			}
		}

		for (Object a : toAddObjects){
			objects.add(objects.size()-2,a);
		}

		toAddObjects.clear();


		// Bouncing Collisions
		/*for (Solid s : solids){
			Point sCoor = s.coordinates();
			Point pCoor = player.coordinates();
			if (checkCollision(s,player)){
				if (pCoor.getX()+player.getWidth()/2 > sCoor.getX()-s.getWidth()/2 && pCoor.getX()-player.getWidth()/2 < sCoor.getX()+s.getWidth()/2 && Math.abs(sCoor.getY()-pCoor.getY()) > s.getWidth()/2-(player.acceleration*2)){
					player.setdy(-(player.getdy()*2));
					break;
				}
			}
		}
		for (Solid s : solids){
			Point sCoor = s.coordinates();
			Point pCoor = player.coordinates();
			if (checkCollision(s,player)){
				if (pCoor.getY()+player.getHeight()/2 > sCoor.getY()-s.getHeight()/2 && pCoor.getY()-player.getHeight()/2 < sCoor.getY()+s.getHeight()/2){
					player.setdx(-(player.getdx()*2));
					break;
				}
			}
		}*/

		Object follower = view;
		if (follower.getX() != getWindowWidth()/2 || follower.getY() != getWindowHeight()/2){
			int ch_x = follower.getX() - getWindowWidth()/2;
			int ch_y = follower.getY() - getWindowHeight()/2;
			for (Object o : objects){
				o.moveX(-Math.ceil(ch_x/3));
				o.moveY(-Math.ceil(ch_y/3));
			}
		}

		for (Object of : objects){
			if (of instanceof Enemy){
				//if (of.getLife() > 0 && ){
					levelFinished = false;
				//}
			}
		}

		if (levelFinished){
			System.out.println("Next level...");
			clearLevel();
			generateMap();
		}
	}

	public int getWindowWidth(){
		return windowWidth;
	}

	public int getWindowHeight(){
		return windowHeight;
	}

	public boolean checkCollision(Object a, Object b){
		int xdist = Math.abs((int)(b.getX() - a.getX()));
		int ydist = Math.abs((int)(b.getY() - a.getY()));
		if ((xdist < a.getWidth()/2+b.getWidth()/2) && (ydist < a.getHeight()/2+b.getHeight()/2))
			return true;
		return false;
	}

	public Point mouseCoordinates(){
		return new Point(mouse_x,mouse_y);
	}

	public void drawMouse(Graphics g, int mouse_x, int mouse_y){
		g.drawLine(mouse_x,mouse_y,mouse_x,mouse_y+14);
		g.drawLine(mouse_x,mouse_y,mouse_x+10,mouse_y+10);
		g.drawLine(mouse_x,mouse_y+14,mouse_x+4,mouse_y+10);
		g.drawLine(mouse_x+10,mouse_y+10,mouse_x+4,mouse_y+10);
	}

	public double lengthdir_x(int speed, double dir){
		return Math.cos(dir)*speed;
	}

	public double lengthdir_y(int speed, double dir){
		return -Math.sin(dir)*speed;
	}

	public double pointDistance(Point a, Point b){
		int xchange = Math.abs((int)(a.getX()-b.getX()));
		int ychange = Math.abs((int)(a.getY()-b.getY()));
		double output = Math.sqrt((xchange*xchange)+(ychange*ychange));
		return output;
	}

	public double pointDirection(Point a, Point b){
		int xchange = (int)(b.getX()-a.getX());
		int ychange = (int)(b.getY()-a.getY());
		double dir;
		if (xchange == 0){
			if (ychange >= 0)
				dir = -Math.PI/2;
			else
				dir = Math.PI/2;
		}
		else{
			dir = Math.atan2(xchange,ychange)-Math.PI/2;
		}
		return dir;
	}

	public void paint(Graphics g){
		if (cLevel == 1)
			g.setColor(BG_COLOR1);
		else if (cLevel == 2)
			g.setColor(BG_COLOR2);
		else
			g.setColor(BG_COLOR3);
		g.setFont(FONT);
		g.fillRect(-1,-1,getWindowWidth()+1,getWindowHeight()+1);
		for (Object i : objects){
			i.paint(g);
		}
		g.setColor(Color.green);
		g.drawRect(10,10,getWindowWidth()-20,getWindowHeight()-20);

		int hpBarW = 96;
		g.setColor(Color.black);
		g.fillRect(20,20,hpBarW,16);
		g.setColor(Color.green.darker());
		int hpFull = (int)(hpBarW * ((double)player.getLife()/player.getMaxLife()));
		g.fillRect(20,20,hpFull,16);
		g.setColor(Color.white);
		g.drawRect(20,20,hpBarW,16);

		g.setColor(new Color(220,230,220));
		g.drawString(String.format("%d / %d",player.getLife(),player.getMaxLife()),23,33);
		g.drawString(String.format("Level: %d",DIFFICULTY),122,33);
		if (player.getLife() < 1){
			float alpha = (float)(.6);
			g.setColor(new Color(.2f,.2f,.2f,alpha));
			g.fillRect(-1,-1,getWindowWidth()+1,getWindowHeight()+1);
			g.setColor(new Color(240,240,240));
			String finish = "You did not become WasteLand Lord.";
			if (DIFFICULTY >= 10){
				finish = "YOU ARE WASTELAND LORD";
			}
			g.drawString(finish,32,getWindowHeight()/2);
			g.drawString("Difficulty reached: " + DIFFICULTY,48,getWindowHeight()/2+17);
		}
		else if (paused){
			float alpha = (float)(.6);
			g.setColor(new Color(.2f,.2f,.2f,alpha));
			g.fillRect(-1,-1,getWindowWidth()+1,getWindowHeight()+1);
			g.setColor(new Color(240,240,240));
			String msg = "- Game Paused -";
			g.drawString(msg,32,getWindowHeight()/2);
		}
		g.setColor(new Color(200,200,200));
		drawMouse(g,mouse_x+1,mouse_y+2);
		g.setColor(Color.red);
		drawMouse(g,mouse_x,mouse_y);
	}
}