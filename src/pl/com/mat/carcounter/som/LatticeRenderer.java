package pl.com.mat.carcounter.som;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;

public class LatticeRenderer extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int size = 600;
	
	private Vector<SOMVector> inputVect;
	private Graphics2D graph;
	private BufferedImage img = null;
	
	private boolean closestMode = true;
	
	public LatticeRenderer(Vector<SOMVector> inputVect) {
		setTitle("SOM Map");
    	setSize(new Dimension(size, size));
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		this.inputVect = inputVect;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (img != null) {
			g.drawImage(img, 0, 0, this);
		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, size, size);
		}
	}
	
	public void render(SOMLattice lattice, int iteration) {
		float cellWidth = (float)getWidth() / (float)lattice.getWidth();
		float cellHeight = (float)getHeight() / (float)lattice.getHeight();
		
		int imgW = img.getWidth();
		int imgH = img.getHeight();
		
		graph.setColor(Color.BLACK);
		graph.fillRect(0,0,imgW,imgH);
		for (int x=0; x<lattice.getWidth(); x++) {
			for (int y=0; y<lattice.getHeight(); y++) {
				SOMVector vec = lattice.getNode(x,y).getVector();
				
				if (closestMode) graph.setColor(closest(vec, inputVect));
				else graph.setColor(new Color(vec.get(0), vec.get(1), vec.get(2)));
				
				graph.fillRect((int)(x*cellWidth), (int)(y*cellHeight),
							(int)cellWidth +1 , (int)cellHeight +1);
			}
		}
		graph.setColor(Color.RED);
		graph.drawString("Iteration: " + String.valueOf(iteration), 5, 15);
		
		this.repaint();
	}
	
	private Color closest(SOMVector vec, Vector<SOMVector> inputs) {        // for better visualization
		SOMVector best = inputs.get(0);
		double bestDist = vec.euclideanDist(best);
		double dist;
		
		for (int i = 0; i < inputs.size(); i++) {
			dist = vec.euclideanDist(inputs.get(i));
			if (dist < bestDist) {
				bestDist = dist;
				best = inputs.get(i);
			}
		}
		
		return new Color(best.get(0), best.get(1), best.get(2));
	}
	
	public BufferedImage getImage() {
		if (img == null) {
			img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
			graph = img.createGraphics();
		}
		return img;
	}
}