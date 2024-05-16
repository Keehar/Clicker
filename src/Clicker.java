import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Clicker extends JFrame implements MouseListener, WindowListener, ActionListener{

	private static Clicker frame;
	private static Clicker startFrame;
	private static Clicker statsFrame;
	private static Clicker roundStatsFrame;
	private static JPanel startPanel;
	private static JPanel statsPanel;
	private static JPanel roundPanel;
	private static JLabel target;
	private static int targetCap;
	private static int score;
	private static int highScore = 0;
	private static int clickCount;
	private static int targetMode = 0;
	private static File saveFile;
	private static long startTime;
	private static long endTime;
	private static boolean noPreviousClose = true;
	private static boolean frameCreated = false;
	private static boolean isUnlimited = false;
	private static boolean firstTarget = false;
	private static double elapsedTime;
	private static double bestAccuracy;
	private static double bestTargetPerSecond;
	private static double targetPerSecond;
	private static double accuracy;
	
		public Clicker() {
		addMouseListener(this);
		addWindowListener(this);
		}
		public static void main(String[] args) throws IOException {

		//createFrame();
		startScreen();
		saveFile = new File("saveFile.txt");
		Scanner save = new Scanner(saveFile);
		if (save.hasNextInt()) {
			highScore = save.nextInt();
			if (save.hasNextDouble()) {
				bestAccuracy = save.nextDouble();
				if (save.hasNextDouble()) {
					bestTargetPerSecond = save.nextDouble();
				}
			}
		}
		Scanner sc = new Scanner(System.in);
		while (true) {
			if (sc.next().equals("reset")) {
				System.out.println("High Score has been reset to 0");
				highScore = 0;
				System.out.println("Best Accuracy has been reset to 0");
				bestAccuracy = 0;
				System.out.println("Best Target Per Second has been reset to 0");
				bestTargetPerSecond = 0;
			}
			if (sc.next().equals("hs")) {
				System.out.println("High Score: " + highScore);
			}
			
		}
		
	}

	// Frame Methods
		public static void startScreen() throws IOException {
		startFrame = new Clicker();
		startFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		startPanel = new JPanel();
		startFrame.add(startPanel);
		startPanel.setLayout(null);
		
		startFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startFrame.setIconImage(ImageIO.read(new File("images/MousePointer.png")));
		
		JButton startUnlimited = new JButton("Start - Unlimited");
		startUnlimited.addActionListener(startFrame);
		startUnlimited.setBounds(startFrame.getWidth()/2-150-startUnlimited.getPreferredSize().width/2, startFrame.getHeight()/2-100, startUnlimited.getPreferredSize().width, startUnlimited.getPreferredSize().height);
		startPanel.add(startUnlimited);
		
		JButton startTen = new JButton("Start - 10");
		startTen.addActionListener(startFrame);
		startTen.setBounds(startFrame.getWidth()/2, startFrame.getHeight()/2-100, startTen.getPreferredSize().width, startTen.getPreferredSize().height);
		startPanel.add(startTen);
		
		JButton startTwentyFive = new JButton("Start - 25");
		startTwentyFive.addActionListener(startFrame);
		startTwentyFive.setBounds(startFrame.getWidth()/2+150+startTwentyFive.getPreferredSize().width/2, startFrame.getHeight()/2-100, startTwentyFive.getPreferredSize().width, startTwentyFive.getPreferredSize().height);
		startPanel.add(startTwentyFive);
		
		JButton statistics = new JButton("Statistics");
		statistics.addActionListener(startFrame);
		statistics.setBounds(startFrame.getWidth()/2, startFrame.getHeight()/2, statistics.getPreferredSize().width, statistics.getPreferredSize().height);
		startPanel.add(statistics);

		startFrame.setVisible(true);
	}
		public static void statsScreen() throws IOException {
			statsFrame = new Clicker();
			statsPanel = new JPanel(); // ADD A BACK BUTTON TO GO TO THE STARTSCREEN
			statsPanel.setLayout(null);
			
			statsFrame.add(statsPanel);
			statsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			statsFrame.setIconImage(ImageIO.read(new File("images/MousePointer.png")));
			statsFrame.setTitle("Records");
			
			JLabel stats = new JLabel("UNLIMITED: High Score: " + highScore + "          Best Accuracy: " + bestAccuracy + "          Best Targets Per Second: " + bestTargetPerSecond);
			statsFrame.add(stats);
			
			statsFrame.setSize(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT);
			statsFrame.setVisible(true);
	        statsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		}
		public static void createFrame() throws IOException {
		frameCreated = true;
		score = 0;
		clickCount = 0;
        frame = new Clicker();
        
        if (targetMode == 10) {
        	frame.setTitle("10 Targets");
        }
        else if (targetMode == 25) {
        	frame.setTitle("25 Targets");
        }
        else {
        	frame.setTitle("Unlimited Targets                          Score: " + score + "                          High Score = " + highScore);
        }
        
        target = new JLabel(new ImageIcon("images/TransparentCircle.png"));
		frame.add(target);  
		
        frame.setIconImage(ImageIO.read(new File("images/MousePointer.png")));
        frame.setSize(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT);
        frame.setBackground(Color.black);
		//frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //System.out.println(frame.getWidth() + "     " + frame.getHeight());
        
	}
		public static void roundStatsScreen( ) throws IOException {
			roundStatsFrame = new Clicker();
			roundPanel = new JPanel();
			roundPanel.setLayout(null);
			
			roundStatsFrame.add(roundPanel);
			roundStatsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			roundStatsFrame.setIconImage(ImageIO.read(new File("images/MousePointer.png")));
			roundStatsFrame.setTitle("Round Statistics");
			
			statCalc();
			if (isUnlimited) {
				JLabel scoreLabel = new JLabel("Score: " + score);
				roundPanel.add(scoreLabel);
			}
			JLabel tps = new JLabel("Targets hit per second: " + targetPerSecond);
			JLabel a = new JLabel("Accuracy: " + accuracy + "%");
			
			roundPanel.add(tps);
			roundPanel.add(a);
			
			roundStatsFrame.setSize(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT);
			roundStatsFrame.setVisible(true);
			roundStatsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		}
		public static void setTarget() {
		int x = 0;
		int y = 0;
		if ((int)(Math.random() * 2) == 0) {
			x = (int)(Math.random() * 830);
		}
		else {
			x = (int)(Math.random() * -830);
		}
		if ((int)(Math.random() * 2) == 0) {
			y = (int)(Math.random() * 390);
		}
		else {
			y = (int)(Math.random() * -390);
		}
		target.setLocation(x, y);
	}

	
	// Mouse Methods
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	
		public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		try { 
			if (!firstTarget) {
				clickCount++;
			}
		boolean xHit = false;
		boolean yHit = false;
		for (int x = target.getX() - 112; x < target.getX() + 112; x++) {
			if (e.getX()-965 == x) {
				xHit = true;
			}
		}
		for (int y = target.getY() - 112; y < target.getY() + 112; y++) {
			if (e.getY()-500 == y) {
				yHit = true;
			}
		}
		if (xHit && yHit) {
			if (!firstTarget) {
				startTime = System.currentTimeMillis();

			}
			setTarget();
			score++;
			if (score == targetCap) {
				frame.dispose();
				endTime = System.currentTimeMillis();
				printStats();
				roundStatsScreen();
			}
			if (score > highScore) {
				highScore = score;
			}
			if (isUnlimited) {
				frame.setTitle("Unlimited Targets                          Score: " + score + "                          High Score = " + highScore);
			}
		}
		}
		catch(Exception a) {
			//System.out.println(a);
		}
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
		public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		try {
			// DELETE FILE CONTENTS FIRST
			if (frameCreated && noPreviousClose) {
				endTime = System.currentTimeMillis();
				noPreviousClose = false;
				roundStatsScreen();
			}
			PrintWriter writer = new PrintWriter(saveFile);
			writer.println(highScore); 
			writer.println(bestAccuracy);
			writer.println(bestTargetPerSecond);
			writer.close();
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
		public static void printStats() {
		elapsedTime = (endTime - startTime);
		System.out.println("ROUND STATISTICS:");
		System.out.println("Score: " + score);
		System.out.print("Targets hit per second: ");
		System.out.printf("%.4f", (((double)score/elapsedTime)));
		
		statCalc();
		
		System.out.println(", " + score + " targets in " + elapsedTime + " seconds");
		System.out.println("accuracy ((score / number of clicks)*100): " + accuracy);
	}
	@Override
		public void actionPerformed(ActionEvent e){
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("Start - Unlimited")) {
			try {
				targetCap = Integer.MAX_VALUE;
				isUnlimited = true;
				createFrame();
			} 
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getActionCommand().equals("Start - 10")) {
			try {
				targetCap = 10;
				targetMode = 10;
				startFrame.dispose();
				createFrame();
			} 
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getActionCommand().equals("Start - 25")) {
			try {
				targetCap = 25;
				targetMode = 25;
				startFrame.dispose();
				createFrame();
			} 
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getActionCommand().equals("Statistics")) {
			try {
				startFrame.dispose();
				statsScreen();
			} 
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			System.out.println("Something Went Wrong...");
		}
	}
		public static void statCalc() {
			targetPerSecond = Math.round((score/(double)elapsedTime)*100)/100.0;
			if (targetPerSecond > bestTargetPerSecond) {
				bestTargetPerSecond = targetPerSecond;
			}
			accuracy = Math.round(((double)score/clickCount) * 10000)/100.0;
			if (accuracy > bestAccuracy) {
				bestAccuracy = accuracy;
			}
			System.out.println(targetPerSecond + "\t" + accuracy);
		}
}
