package pl.com.mat.carcounter.som;

import java.io.Serializable;


public class SOMNode implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private SOMVector furtherDeltaWeights;
	private SOMVector weights;
	private int xp, yp;
	
	public SOMNode(int numWeights) {
		weights = new SOMVector();
		for (int x=0; x<numWeights; x++) {
			weights.addElement(new Float(Math.random()*0.7));
		}
		
		this.furtherDeltaWeights = new SOMVector(0f, 0f, 0f);
	}
	
	public void setWeights(SOMVector weights) {
		this.weights = weights;
	}
	
	public void setX(int xpos) {
		xp = xpos;
	}
	
	public void setY(int ypos) {
		yp = ypos;
	}
	
	public int getX() {
		return xp;
	}
	
	public int getY() {
		return yp;
	}
	
	public double distanceTo(SOMNode n2) {         // eucides distance som node - som node
		int xleg, yleg;
		xleg = getX() - n2.getX();
		xleg *= xleg;
		yleg = getY() - n2.getY();
		yleg *= yleg;
		return xleg + yleg;
	}
	
	public SOMVector getVector() {
		return weights;
	}
	
	public void adjustWeights(SOMVector input, double learningRate, double distanceFalloff) {
		SOMVector weightsTemp = (SOMVector)weights.clone();
		
		double wt, vw;
		for (int w=0; w<weights.size(); w++) {
			wt = weights.elementAt(w).doubleValue();
			vw = input.elementAt(w).doubleValue();
			wt += distanceFalloff * learningRate * (vw - wt);
			wt += SOMTrainer.MOMENTUM_COEFFICIENT * furtherDeltaWeights.elementAt(w).doubleValue();
			
			if (wt > 1) wt = 1;
			if (wt < 0) wt = 0;
			
			weights.setElementAt(new Float(wt), w);
		}
		
		furtherDeltaWeights = weights.minus(weightsTemp);
	}
	
	@Override
	public boolean equals(Object obj) {
		SOMNode elem = (SOMNode)obj;
		
		return getVector().equals(elem.getVector());
	}
	
	public boolean isNeighboor(SOMNode n2) {
		double dist = this.distanceTo(n2);
		
		return dist <= 2;
	}
}