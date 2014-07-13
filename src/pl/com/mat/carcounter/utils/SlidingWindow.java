package pl.com.mat.carcounter.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pl.com.mat.carcounter.backpropagation.NeuralNetwork;


public class SlidingWindow {
	
	private final int size = 100;
	private NeuralNetwork network;
	
	public SlidingWindow(NeuralNetwork network) {
		this.network = network;
	}

	public int process(BufferedImage img) {
		int width = (img.getWidth() % size != 0) ? img.getWidth() + (size-(img.getWidth() % size)) : img.getWidth();
		int height = (img.getHeight() % size != 0) ? img.getHeight() + (size-(img.getHeight() % size)) : img.getHeight();
		
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		
		int wt = width / 100;
		int ht = height / 100;
		
		BufferedImage sub = null;
		
		int counter = 0;
		
		for (int i = 0; i < wt; i++) {
			for (int j = 0; j < ht; j++) {
				System.out.println(i + " " + j + "/" + wt + " " + ht);
				sub = resized.getSubimage(i*size, j*size, size, size); 
				network.setInput(PatternSupplier.getFeatures(sub));
				network.activateNetwork();

				if (PatternSupplier.decodeOutput(network.getOutput())) {
					counter++;
				}
			}
		}
		
		return counter;
	}
	
}