package pl.com.mat.carcounter.som;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Vector;

public class SOMLattice implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int width, height;
	private SOMNode[][] matrix;
	
	private boolean closestType = false;
	
	public SOMLattice(int w, int h) {
		width = w;
		height = h;
		matrix = new SOMNode[width][height];
		
		for (int x=0; x<w; x++) {
			for (int y=0; y<h; y++) {
				matrix[x][y] = new SOMNode(3);
				matrix[x][y].setX(x);
				matrix[x][y].setY(y);
			}
		}
	}
	
	public SOMNode getNode(int x, int y) {
		return matrix[x][y];
	}
	
	public void setNode(int x, int y, SOMNode node) {
		matrix[x][y] = node;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public SOMNode[][] getMatrix() {
		return matrix;
	}
	
	// Winner - Best matching unit
	public SOMNode getWinner(SOMVector inputVector) {
		SOMNode bmu = matrix[0][0];
		double bestDist = inputVector.euclideanDist(bmu.getVector());
		double curDist;
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				curDist = inputVector.euclideanDist(matrix[x][y].getVector());
				if (curDist < bestDist) {
					bmu = matrix[x][y];
					bestDist = curDist;
				}
			}
		}
		
		return bmu;
	}
	
	public BufferedImage use(BufferedImage src, Vector<SOMVector> inputVectors) {
		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		SOMVector tempVec = null;
		
		if (closestType) {
			for (int i = 0; i < getWidth(); i++) {
				for (int j = 0; j < getHeight(); j++) {
					SOMNode node = getNode(i, j);
					node.setWeights(closestVect(node.getVector(), inputVectors));
					setNode(i, j, node);
				}
			}
		}
		
		for (int i = 0; i < src.getWidth(); i++) {
			for (int j = 0; j < src.getHeight(); j++) {
				double percent = (i / (double)src.getWidth() * 100);
					System.out.println(percent + " %");
				
				Color c = new Color(src.getRGB(i, j));
				tempVec = new SOMVector(scale(c.getRed()), scale(c.getGreen()), scale(c.getBlue()));
				SOMNode winner = getWinner(tempVec);
				
				SOMVector colorVector = winner.getVector();
				Color resC = new Color(unscale(colorVector.get(0)), unscale(colorVector.get(1)),
						unscale(colorVector.get(2)));
				result.setRGB(i, j, resC.getRGB());
			}
		}
		
		return result;
	}
	
	public static SOMVector closestVect(SOMVector vec, Vector<SOMVector> inputs) {  // end
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
		
		return best;
	}
	
	private float scale(int val) {
		return 1 - ((float)(255-val)/(float)255);
	}
	
	private int unscale(double val) {
		return (int)(255*val);
	}
}