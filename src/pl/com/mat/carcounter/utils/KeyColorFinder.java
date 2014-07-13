package pl.com.mat.carcounter.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

import pl.com.mat.carcounter.som.SOMVector;


public class KeyColorFinder {
	
	private static final int startColorThreshold = 7000;
	
	private static Vector<Color> getKeyColors(Vector<Color> src, int cnt, int threshold) {
		Vector<Color> groups = new Vector<Color>();
		groups.add(src.get(0));
		
		boolean add;
		
		for (int i = 0; i < src.size(); i++) {
				Color c = src.get(i);
				add = true;
				
				for (Color col: groups) {
					if (getDist(c, col) < threshold) {
						add = false;
						break;
					}
				}
				
				if (add) {
					groups.add(c);
				}
		}
		
		while (groups.size() > cnt) {
			groups = getKeyColors(src, cnt, threshold + (int)(200 * ((groups.size()-cnt) * (groups.size()-cnt) * 0.04)));
		}
		
		while (groups.size() < cnt) {
			groups = getKeyColors(src, cnt, threshold - (int)(200 * ((groups.size()-cnt) * (groups.size()-cnt) * 0.04)));
		}
		
		return groups;
	}
	
	public static Vector<SOMVector> getKeyColorInputVector(BufferedImage src, int cnt) {
		Vector<Color> input = getColorsFromImage(src);
		Vector<Color> resultKeyColors = getKeyColors(input, cnt, KeyColorFinder.startColorThreshold);
		Vector<SOMVector> inputs = new Vector<SOMVector>();
		
		for (Color c: resultKeyColors) {
			inputs.addElement(new SOMVector(scale(c.getRed()), scale(c.getGreen()), scale(c.getBlue())));
		}
		
		return inputs;
	}
	
	private static int getDist(Color c1, Color c2) {
		int summation = 0, temp;
		
		temp = c1.getRed() - c2.getRed();
		temp *= temp;
		summation += temp;
		
		temp = c1.getGreen() - c2.getGreen();
		temp *= temp;
		summation += temp;
		
		temp = c1.getBlue() - c2.getBlue();
		temp *= temp;
		summation += temp;
		
		return summation;
	}
	
	private static float scale(int val) {
		return 1 - ((float)(255-val)/(float)255);
	}
	
	public static Vector<Color> getColorsFromImage(BufferedImage img) {
		Vector<Color> result = new Vector<Color>();
		
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				result.add(new Color(img.getRGB(i, j)));
			}
		}
		
		return result;
	}
	
}