package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import data.Highscores;
import data.Saver;
import data.Set;


public class GraphicsPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	private static final boolean PEGS_ON = false;
	private static final boolean CUSTOM_FONT_ON = true;
	private static final String CUSTOM_FONT_NAME = "Interfade";
	private static final boolean HIGHSCORES_ON = true;

	private Set set = new Set(3);
	private long startingTime = 0;
	private long moves = 0;
	private char hoverChar = 'A';
	private int hoverSize = 0;
	private String currTime = "";

	private Color colBackground = new Color(140, 180, 220);

	private Color colBoardSide1 = new Color(175, 105, 35);
	private Color colBoardSide2 = new Color(215, 135, 65);
	private Color colBoardTop1 = new Color(205, 135, 65);
	private Color colBoardTop2 = new Color(245, 165, 95);

	private Color colPegSide1 = new Color(175, 105, 35);
	private Color colPegSide2 = new Color(215, 135, 65);
	private Color colPegTop1 = new Color(225, 155, 85);
	private Color colPegTop2 = new Color(255, 185, 115);

	private Color colDiskSide1 = new Color(40, 50, 100);
	private Color colDiskSide2 = new Color(40, 50, 100);
	private Color colDiskTop1 = new Color(60, 70, 120);
	private Color colDiskTop2 = new Color(60, 70, 120);

	private Color colText1 = new Color(40, 50, 100);
	private Color colText2 = new Color(250, 250, 250);
	private Color colText3 = new Color(230, 230, 230);

	private Color colArrow = new Color(40, 50, 100);

	private Font mainFont = null;

	private Highscores highscores;
	private Saver saver;
	private boolean highscoreAdded = false;

	public GraphicsPanel() {
		super();
		addKeyListener(this);
		setFocusable(true);

		try {
			mainFont = Font.createFont(Font.TRUETYPE_FONT, GraphicsPanel.class.getClassLoader().getResourceAsStream(CUSTOM_FONT_NAME + ".ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(HIGHSCORES_ON) {
			saver = new Saver();
			highscores = saver.getSavedGame();

			if(CUSTOM_FONT_ON) {
				Font customFont = mainFont.deriveFont(20f);
				UIManager.put("OptionPane.messageFont", new FontUIResource(customFont));
			}
			
			UIManager.put("OptionPane.background", colText1);
			UIManager.put("Panel.background", colText1);
			UIManager.put("OptionPane.messageForeground", colBackground);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(colBackground);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(new BasicStroke(1));

		GradientPaint boardTopGradient = new GradientPaint(0, 0, colBoardTop1, 450, 0, colBoardTop2);
		g2.setPaint(boardTopGradient);
		Polygon boardTop = new Polygon(new int[]{200, 700, 750, 150}, new int[]{300, 300, 400, 400}, 4);
		g2.fill(boardTop);

		GradientPaint boardSideGradient = new GradientPaint(0, 0, colBoardSide1, 450, 0, colBoardSide2);
		g2.setPaint(boardSideGradient);
		Rectangle boardSide = new Rectangle(150, 400, 600, 30);
		g2.fill(boardSide);

		if(PEGS_ON) {
			GradientPaint pegSideGradient = new GradientPaint(450, 500, colPegSide1, 450, 0, colPegSide2);
			g2.setPaint(pegSideGradient);
			Rectangle pegSideA = new Rectangle(280, 150, 20, 200);
			g2.fill(pegSideA);
			Rectangle pegSideB = new Rectangle(440, 150, 20, 200);
			g2.fill(pegSideB);
			Rectangle pegSideC = new Rectangle(600, 150, 20, 200);
			g2.fill(pegSideC);
			Ellipse2D.Double pegBottomA = new Ellipse2D.Double(280, 345, 20, 10);
			g2.fill(pegBottomA);
			Ellipse2D.Double pegBottomB = new Ellipse2D.Double(440, 345, 20, 10);
			g2.fill(pegBottomB);
			Ellipse2D.Double pegBottomC = new Ellipse2D.Double(600, 345, 20, 10);
			g2.fill(pegBottomC);

			GradientPaint pegTopGradient = new GradientPaint(450, 200, colPegTop1, 450, 0, colPegTop2);
			g2.setPaint(pegTopGradient);
			Ellipse2D.Double pegTopA = new Ellipse2D.Double(280, 145, 20, 10);
			g2.fill(pegTopA);
			Ellipse2D.Double pegTopB = new Ellipse2D.Double(440, 145, 20, 10);
			g2.fill(pegTopB);
			Ellipse2D.Double pegTopC = new Ellipse2D.Double(600, 145, 20, 10);
			g2.fill(pegTopC);
		}

		int[] pegArrayA = set.getPegArray('A');
		int[] pegArrayB = set.getPegArray('B');
		int[] pegArrayC = set.getPegArray('C');

		for(int di = 0; di < pegArrayA.length; di++) {
			GradientPaint diskSideGradient = new GradientPaint(0, 300, colDiskSide1, 900, 300, colDiskSide2);
			g2.setPaint(diskSideGradient);
			Rectangle diskSide = new Rectangle(275 - 6 * pegArrayA[di], 350 - 20 * di, 30 + 12 * pegArrayA[di], 14);
			Ellipse2D.Double diskBottom = new Ellipse2D.Double(275 - 6 * pegArrayA[di], 358 - 20 * di, 30 + 12 * pegArrayA[di], 14);
			g2.fill(diskSide);
			g2.fill(diskBottom);

			GradientPaint diskTopGradient = new GradientPaint(0, 300, colDiskTop1, 900, 300, colDiskTop2);
			g2.setPaint(diskTopGradient);
			Ellipse2D.Double diskTop = new Ellipse2D.Double(275 - 6 * pegArrayA[di], 342 - 20 * di, 30 + 12 * pegArrayA[di], 14);
			g2.fill(diskTop);

			g2.setFont(new Font("Tahoma", 1, 10));
			if(CUSTOM_FONT_ON)
				g2.setFont(mainFont.deriveFont(10f));
			g2.setColor(colText3);
			g2.drawString("" + pegArrayA[di], 287, 367 - 20 * di);
		}

		for(int di = 0; di < pegArrayB.length; di++) {
			GradientPaint diskSideGradient = new GradientPaint(0, 300, colDiskSide1, 900, 300, colDiskSide2);
			g2.setPaint(diskSideGradient);
			Rectangle diskSide = new Rectangle(435 - 6 * pegArrayB[di], 350 - 20 * di, 30 + 12 * pegArrayB[di], 14);
			Ellipse2D.Double diskBottom = new Ellipse2D.Double(435 - 6 * pegArrayB[di], 358 - 20 * di, 30 + 12 * pegArrayB[di], 14);
			g2.fill(diskSide);
			g2.fill(diskBottom);

			GradientPaint diskTopGradient = new GradientPaint(0, 300, colDiskTop1, 900, 300, colDiskTop2);
			g2.setPaint(diskTopGradient);
			Ellipse2D.Double diskTop = new Ellipse2D.Double(435 - 6 * pegArrayB[di], 342 - 20 * di, 30 + 12 * pegArrayB[di], 14);
			g2.fill(diskTop);

			g2.setFont(new Font("Tahoma", 1, 10));
			if(CUSTOM_FONT_ON)
				g2.setFont(mainFont.deriveFont(10f));
			g2.setColor(colText3);
			g2.drawString("" + pegArrayB[di], 447, 367 - 20 * di);
		}

		for(int di = 0; di < pegArrayC.length; di++) {
			GradientPaint diskSideGradient = new GradientPaint(0, 300, colDiskSide1, 900, 300, colDiskSide2);
			g2.setPaint(diskSideGradient);
			Rectangle diskSide = new Rectangle(595 - 6 * pegArrayC[di], 350 - 20 * di, 30 + 12 * pegArrayC[di], 14);
			Ellipse2D.Double diskBottom = new Ellipse2D.Double(595 - 6 * pegArrayC[di], 358 - 20 * di, 30 + 12 * pegArrayC[di], 14);
			g2.fill(diskSide);
			g2.fill(diskBottom);

			GradientPaint diskTopGradient = new GradientPaint(0, 300, colDiskTop1, 900, 300, colDiskTop2);
			g2.setPaint(diskTopGradient);
			Ellipse2D.Double diskTop = new Ellipse2D.Double(595 - 6 * pegArrayC[di], 342 - 20 * di, 30 + 12 * pegArrayC[di], 14);
			g2.fill(diskTop);

			g2.setFont(new Font("Tahoma", 1, 10));
			if(CUSTOM_FONT_ON)
				g2.setFont(mainFont.deriveFont(10f));
			g2.setColor(colText3);
			g2.drawString("" + pegArrayC[di], 607, 367 - 20 * di);
		}

		if(hoverSize != 0) {
			int overOffset = 0;
			if(hoverChar == 'A')
				overOffset = 0;
			if(hoverChar == 'B')
				overOffset = 1;
			if(hoverChar == 'C')
				overOffset = 2;

			GradientPaint diskSideGradient = new GradientPaint(0, 300, colDiskSide1, 900, 300, colDiskSide2);
			g2.setPaint(diskSideGradient);
			Rectangle diskSide = new Rectangle(275 - 6 * hoverSize, 160, 30 + 12 * hoverSize, 14);
			Ellipse2D.Double diskBottom = new Ellipse2D.Double(275 - 6 * hoverSize, 168, 30 + 12 * hoverSize, 14);
			diskSide.translate(160 * overOffset, 0);
			diskBottom.x += 160 * overOffset;
			g2.fill(diskSide);
			g2.fill(diskBottom);

			GradientPaint diskTopGradient = new GradientPaint(0, 300, colDiskTop1, 900, 300, colDiskTop2);
			g2.setPaint(diskTopGradient);
			Ellipse2D.Double diskTop = new Ellipse2D.Double(275 - 6 * hoverSize, 152, 30 + 12 * hoverSize, 14);
			diskTop.x += 160 * overOffset;
			g2.fill(diskTop);
			
			g2.setFont(new Font("Tahoma", 1, 10));
			if(CUSTOM_FONT_ON)
				g2.setFont(mainFont.deriveFont(10f));
			g2.setColor(colText3);
			g2.drawString("" + hoverSize, 286 + 160 * overOffset, 177);
		}

		Polygon arrow = new Polygon(new int[]{280, 300, 290, 290, 270, 270, 260}, new int[]{490, 510, 510, 530, 530, 510, 510}, 7);
		g2.setColor(colArrow);
		if(hoverChar == 'A') {
			arrow.translate(10, -50);
			g2.fill(arrow);
		}
		else if(hoverChar == 'B') {
			arrow.translate(170, -50);
			g2.fill(arrow);
		}
		else if(hoverChar == 'C') {
			arrow.translate(330, -50);
			g2.fill(arrow);
		}

		long playingTime = 0;
		if(startingTime != 0)
			playingTime = System.currentTimeMillis() - startingTime;
		Date date = new Date(playingTime);
		DateFormat formatter = new SimpleDateFormat("mm ss SSS");
		String dateFormatted = formatter.format(date);

		g2.setFont(new Font("Tahoma", 1, 20));
		if(CUSTOM_FONT_ON)
			g2.setFont(mainFont.deriveFont(20f));
		g2.setColor(colText1);
		g2.drawString("Moves " + moves + "", 580, 540);
		g2.setColor(colText2);
		g2.drawString("Moves " + moves + "", 579, 539);
		if(set.isComplete() && hoverSize == 0) {
			g2.setColor(colText1);
			g2.drawString("Finished in " + currTime, 230, 540);
			g2.setColor(colText2);
			g2.drawString("Finished in " + currTime, 229, 539);
			if(HIGHSCORES_ON) {
				if(!highscoreAdded) {
					if(highscores != null) {
						highscores.add(set.size(), currTime);
						highscoreAdded = true;
						saver.saveGame(highscores);
					}
				}
			}
		}
		else {
			g2.setColor(colText1);
			g2.drawString("Time " + dateFormatted, 240, 540);
			g2.setColor(colText2);
			g2.drawString("Time " + dateFormatted, 239, 539);
			currTime = dateFormatted;
		}

		g2.setFont(new Font("Tahoma", 1, 40));
		if(CUSTOM_FONT_ON)
			g2.setFont(mainFont.deriveFont(50f));
		g2.setColor(colText2);
		g2.drawString("Tower of Hanoi", 279, 59);
		g2.setColor(colText1);
		g2.drawString("Tower of Hanoi", 280, 60);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyCode();

		if(key == KeyEvent.VK_SPACE) {
			if(hoverSize == 0) {
				if(!set.empty(hoverChar))
					hoverSize = set.pop(hoverChar);
			}
			else {
				if(set.empty(hoverChar) || set.peek(hoverChar) > hoverSize) {
					set.push(hoverChar, hoverSize);
					hoverSize = 0;
					moves++;
				}
			}
		}
		else if(key == KeyEvent.VK_LEFT) {
			if(hoverChar == 'A')
				hoverChar = 'C';
			else if(hoverChar == 'B')
				hoverChar = 'A';
			else if(hoverChar == 'C')
				hoverChar = 'B';
		}
		else if(key == KeyEvent.VK_RIGHT) {
			if(hoverChar == 'A')
				hoverChar = 'B';
			else if(hoverChar == 'B')
				hoverChar = 'C';
			else if(hoverChar == 'C')
				hoverChar = 'A';
		}
		else if(key == KeyEvent.VK_2)
			resetBoard(2);
		else if(key == KeyEvent.VK_3)
			resetBoard(3);
		else if(key == KeyEvent.VK_4)
			resetBoard(4);
		else if(key == KeyEvent.VK_5)
			resetBoard(5);
		else if(key == KeyEvent.VK_6)
			resetBoard(6);
		else if(key == KeyEvent.VK_7)
			resetBoard(7);
		else if(key == KeyEvent.VK_8)
			resetBoard(8);
		else if(key == KeyEvent.VK_9)
			resetBoard(9);
		else if(key == KeyEvent.VK_R)
			resetBoard(set.size());
		else if(key == KeyEvent.VK_ESCAPE)
			System.exit(0);

		if(startingTime == 0 && (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT))
			startingTime = System.currentTimeMillis();

		if(key == KeyEvent.VK_EQUALS)
			JOptionPane.showMessageDialog(this, highscores.toString(), "Highscores", JOptionPane.PLAIN_MESSAGE);
	}

	private void resetBoard(int size) {
		hoverChar = 'A';
		hoverSize = 0;
		startingTime = 0;
		moves = 0;
		highscoreAdded = false;
		set = new Set(size);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}