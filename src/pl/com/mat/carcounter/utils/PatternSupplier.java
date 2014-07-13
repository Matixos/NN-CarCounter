package pl.com.mat.carcounter.utils;


import java.awt.Color;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PatternSupplier {
	
	public static double[] getFeatures(BufferedImage img) {
		double[] features = new double[3];
		double[] firstAndSec = getFirstAndSecondFeature(img);
		
		features[0] = firstAndSec[0];
		features[1] = firstAndSec[1];
		features[2] = getThirdFeature(img);
		
		return features;
	}
	
	private static double[] getFirstAndSecondFeature(BufferedImage img) {
		double[] result = new double[2];
		
		int cntr = 1;
		ArrayList<Color> rgbs = new ArrayList<Color>();
		//img = Scalr.resize(img, 80);
		int areaCnt = 0;
		
		BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null);
		BufferedImage color = op.filter(img, null);
		
		for (int i = 0; i < color.getHeight(); i++) {
			for (int j = 0; j < color.getWidth(); j++) {
				if (color.getRGB(j, i) == Color.BLACK.getRGB()) {
					rgbs.add(new Color(1000*(cntr++) + cntr*473));
					floodFill(j, i, Color.BLACK, rgbs.get(rgbs.size()-1), color);
					areaCnt++;
				}
			}
		}
		
		result[0] = scaleFirstFeature(areaCnt);
		
		Map<Color, Integer> map = new HashMap<Color, Integer>();
		int old = 0;
		
		for (Color c: rgbs) {
			map.put(c, 0);
		}
		
		for (int i = 0; i < color.getHeight(); i++) {
			for (int j = 0; j < color.getWidth(); j++) {
				for (Color c: rgbs) {
					if (color.getRGB(j, i) == c.getRGB()) {
						old = map.get(c);
						map.put(c, old + 1);
						break;
					}
				}
			}
		}
		int maxVal = 0;
		
		for (Map.Entry<Color, Integer> entry : map.entrySet()) {
			if (entry.getValue() > maxVal) {
				maxVal = entry.getValue();
			}
		}
		
		result[1] = scaleSecondFeature(maxVal, color.getWidth()*color.getHeight());
		
		return result;
	}
	
	private static double scaleFirstFeature(double val) {
		return (20 - val) / 19.0;
	}
	
	private static double scaleSecondFeature(int val, int size) {
		return (double)val/(double)size;
	}
	
	private static double getThirdFeature(BufferedImage img) {
		int whiteCnt = 0;
		
		for (int i = 0; i < img.getHeight(); i++)
			for (int j = 0; j < img.getWidth(); j++)
				if (img.getRGB(j, i) == -1)
					whiteCnt++;
		
		return (double)whiteCnt / ((img.getWidth() * img.getHeight()) - whiteCnt);
	}
	
	private static void floodFill(int x, int y, Color targetColor, Color replacementColor, BufferedImage image) {
		List<Point> queue = new LinkedList<Point>();
		Point w, e;
		
		queue.add(new Point(x, y));
		
		do {
			Point p = queue.remove(queue.size() - 1);
			if (image.getRGB((int)p.getX(), (int)p.getY()) == targetColor.getRGB()) {
				w = (Point)p.clone();
				e = (Point)p.clone();
				
				while (w.getX() - 1 >= 0)
					if (image.getRGB((int)w.getX() - 1, (int)w.getY()) == targetColor.getRGB()) {
						w = new Point((int)w.getX() - 1, (int)w.getY());
					} else {
						break;
					}
				
				while (e.getX() + 1 < image.getWidth())
					if (image.getRGB((int)e.getX() + 1, (int)e.getY()) == targetColor.getRGB()) {
						e = new Point((int)e.getX() + 1, (int)e.getY());
					} else {
						break;
					}
				
				for (int i = (int)w.getX(); i <= e.getX(); i++)
					image.setRGB(i, (int)w.getY(), replacementColor.getRGB());
				
				for (int i = (int)w.getX(); i <= e.getX(); i++) {
					if (w.getY() - 1 >= 0)
						if (image.getRGB(i, (int)w.getY()-1) == targetColor.getRGB())
							queue.add(new Point(i, (int)w.getY()-1));
					if (w.getY() + 1 < image.getHeight())
						if (image.getRGB(i, (int)w.getY()+1) == targetColor.getRGB())
							queue.add(new Point(i, (int)w.getY()+1));
				}	
			}
		} while (!queue.isEmpty());
	}
	
	public static List<Pattern> processLearningSet(List<Pattern> learningSet) {
		int cnt = 0;
		for (Pattern patt: learningSet) {
			System.out.println(cnt++);
			patt.setInput(getFeatures(patt.getImgInput()));
		}
		
		return learningSet;
	}
	
	public static boolean decodeOutput(double[] out) {
		return Math.round(out[0]) == 1;
	}

}