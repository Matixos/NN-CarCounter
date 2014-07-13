package pl.com.mat.carcounter.som;

import java.util.Collections;
import java.util.Vector;

import javax.swing.JOptionPane;

public class SOMTrainer implements Runnable {

	public static double START_LEARNING_RATE 	= 0.1;
	public static double MOMENTUM_COEFFICIENT 	= 0.3;
   	public static int	 NUM_ITERATIONS 		= 500;
	
	private double LATTICE_RADIUS_MAX;
	private SOMLattice lattice;
	private Vector<SOMVector> inputs;
	public boolean running;
	private Thread runner;
	
	private LatticeRenderer renderer;
	
	public SOMTrainer() {
		this.running = false;
	}
	
	private double getNeighborhoodRadius(double iteration) {
		return LATTICE_RADIUS_MAX * Math.pow((1/LATTICE_RADIUS_MAX), iteration/NUM_ITERATIONS);
	}
	
	private double getDistanceFalloff(double distSq, double radius) {     // nbh fct
		double radiusSq = radius * radius;
		return Math.exp(-(distSq)/(2 * radiusSq));
	}
		
	public void setTraining(SOMLattice latToTrain, Vector<SOMVector> in) {
		lattice = latToTrain;
		inputs = in;
		
		this.renderer = new LatticeRenderer(in);
		this.renderer.getImage();
	}
	
	public void start() {
		if (lattice != null) {
			runner = new Thread(this);
			runner.setPriority(Thread.MAX_PRIORITY);
			running = true;
			renderer.setVisible(true);
			runner.start();
		}
	}
	
	@Override
	public void run() {
		int lw = lattice.getWidth();
		int lh = lattice.getHeight();
		int xstart, ystart, xend, yend;
		double dist, dFalloff;
		
		LATTICE_RADIUS_MAX = Math.max(lw, lh)/2;
		
		int iteration = 0;
		double nbhRadius;
		SOMNode bmu = null, temp = null;
		SOMVector curInput = null;
		double learningRate = START_LEARNING_RATE;
		
		while (iteration < NUM_ITERATIONS && running) {
			nbhRadius = getNeighborhoodRadius(iteration);
			
			for (int i=0; i<inputs.size(); i++) {
				curInput = inputs.elementAt(i);
				bmu = lattice.getWinner(curInput);
				
				// update weights in whole neighbourhood
				xstart = (int)(bmu.getX() - nbhRadius - 1);
				ystart = (int)(bmu.getY() - nbhRadius - 1);
				xend = (int)(xstart + (nbhRadius * 2) + 1);
				yend = (int)(ystart + (nbhRadius * 2) + 1);
				if (xend > lw) xend = lw;
				if (xstart < 0) xstart = 0;
				if (yend > lh) yend = lh;
				if (ystart < 0) ystart = 0;
				
				
				for (int x=xstart; x<xend; x++) {
					for (int y=ystart; y<yend; y++) {
						temp = lattice.getNode(x,y);
						dist = bmu.distanceTo(temp);
						if (dist <= (nbhRadius * nbhRadius)) {
							dFalloff = getDistanceFalloff(dist, nbhRadius);
							temp.adjustWeights(curInput, learningRate, dFalloff);
						}
					}
				}
			}
			iteration++;
			Collections.shuffle(inputs);
			learningRate = START_LEARNING_RATE * Math.exp(-(double)iteration/NUM_ITERATIONS);      // desc
		
			if (iteration % 50 == 0) {
				renderer.render(lattice, iteration);
			}
		}
		
		running = false;
		
		JOptionPane.showMessageDialog(null, "Success",
				"Map is trained!",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void stop() {
		if (runner != null) {
			running = false;
			while (runner.isAlive()) {};
		}
	}
	
	public SOMLattice getLattice() {
		return lattice;
	}
}
