package pl.com.mat.carcounter.som;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;

public class ShowPanel extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage img = null;
	
	public ShowPanel() {
		setTitle("Input Vector");
    	setSize(new Dimension(500, 520));
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.drawImage(img, 0, 20, this);
	}
	
	public void render(Vector<SOMVector> inputs) {
		float fl = (float)Math.sqrt(inputs.size());
		int qb = Math.round(fl);
		
		if (fl > qb) qb += 1;
		
		float cellWidth = img.getWidth() / qb;
		float cellHeight = img.getHeight() / qb;
		
		int imgW = img.getWidth();
		int imgH = img.getHeight();
		
		Graphics2D g2 = img.createGraphics();
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,imgW,imgH);
		for (int x=0; x<qb; x++) {
			for (int y=0; y<qb; y++) {
				SOMVector vec = null;
				if ((y+qb*x) < inputs.size()) vec = inputs.get(y+qb*x);
				else vec = getNone();
				
				Color c = new Color(vec.get(0), vec.get(1), vec.get(2));
				g2.setColor(c);
				g2.fillRect((int)(x*cellWidth), (int)(y*cellHeight),
							(int)cellWidth +1 , (int)cellHeight +1);
			}
		}
		g2.dispose();
		repaint();
	}
	
	private SOMVector getNone() {
		return new SOMVector(1f, 1f, 1f);
	}
}