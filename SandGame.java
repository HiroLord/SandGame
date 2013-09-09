import java.awt.Graphics;
import java.awt.GraphicsDevice;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.image.*;
//import java.awt.Cursor;
import java.awt.*;

public class SandGame{

	public static int SCALE = 2;

	public static void main(String[] args){
		System.out.println("Initializing...");
		JFrame frame = new JFrame("Wasteland Lords");
		frame.setSize(320*SCALE+frame.getWidth(),240*SCALE+frame.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().add(new Controller(frame,SCALE));
		frame.setLocation(320,100);
		frame.setVisible(false);

		frame.setAlwaysOnTop(true);

		/*GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration gc = device.getDefaultConfiguration();
		DisplayMode basic = device.getDisplayMode();
	
		DisplayMode dm = new DisplayMode(640,480,basic.getBitDepth(),basic.getRefreshRate());
		
		frame.setUndecorated(true); 
		device.setFullScreenWindow(frame);
		device.setDisplayMode(dm);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);*/

		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		frame.getContentPane().setCursor(blankCursor);
		frame.setVisible(false);
		frame.setVisible(true);
		System.gc();
		System.out.println("Running...");
	}
}