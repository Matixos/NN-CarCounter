package pl.com.mat.carcounter.som;

import java.awt.Color;
import java.io.Serializable;
import java.util.Vector;

public class SOMVector extends Vector<Float> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public SOMVector() {}
	
	public SOMVector(Float r, Float g, Float b) {
		super();
		addElement(r);
		addElement(g);
		addElement(b);
	}
	
	public SOMVector(Color c) {
		super();
		addElement(scale(c.getRed()));
		addElement(scale(c.getGreen()));
		addElement(scale(c.getBlue()));
	}
	
	public double euclideanDist(SOMVector v2) {   	// euclidean dist ipt Vect - som vect
		double summation = 0, temp;
		for (int x=0; x<size(); x++) {
			temp = elementAt(x).doubleValue() -
				   v2.elementAt(x).doubleValue();
			temp *= temp;
			summation += temp;
		}
		
		return summation;
	}
	
	public SOMVector minus(SOMVector vec) {
		SOMVector result = new SOMVector();
		
		result.addElement(get(0)-vec.get(0));
		result.addElement(get(1)-vec.get(1));
		result.addElement(get(2)-vec.get(2));
		
		return result;
	}	
	
	@Override
	public boolean equals(Object obj) {
		SOMVector vec = (SOMVector)obj;
		boolean thesame = true;
		
		for (int i = 0; i < this.size(); i++) {
			if (get(i).floatValue() != vec.get(i).floatValue()) {
				thesame = false;
			}
		}
		
		return thesame;
	}
	
	private static float scale(int val) {
		return 1 - ((float)(255-val)/(float)255);
	}
}