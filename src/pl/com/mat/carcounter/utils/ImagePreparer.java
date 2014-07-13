package pl.com.mat.carcounter.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;

import pl.com.mat.carcounter.som.SOMVector;


public class ImagePreparer {

	//private static int learningSetSize = 15;;

	private static final float[] borderMatrix = { 1.0f, 1.0f, 1.0f, 
												  1.0f, -8.0f, 1.0f, 
												  1.0f, 1.0f, 1.0f };

	// private static BufferedImage crop(BufferedImage img) {
	// int top, bottom, left, right;
	// top = bottom = left = right = 0;
	//
	// outerloop:
	// for (int i = 0; i < img.getHeight(); i++) {
	// for (int j = 0; j < img.getWidth(); j++) {
	// if(img.getRGB(j, i) == -1) {
	// top = i;
	// tFStruct.setTop(new Point(j, i));
	// break outerloop;
	// }
	// }
	// }
	//
	// outerloop:
	// for (int i = img.getHeight() - 1; i >= 0; i--) {
	// for (int j = img.getWidth() - 1; j >= 0 ; j--) {
	// if(img.getRGB(j, i) == -1) {
	// bottom = i + 1;
	// tFStruct.setBottom(new Point(j, i));
	// break outerloop;
	// }
	// }
	// }
	//
	// outerloop:
	// for (int i = 0; i < img.getWidth(); i++) {
	// for (int j = 0; j < img.getHeight() ; j++) {
	// if(img.getRGB(i, j) == -1) {
	// left = i;
	// tFStruct.setLeft(new Point(i, j));
	// break outerloop;
	// }
	// }
	// }
	//
	// outerloop:
	// for (int i = img.getWidth() - 1; i >= 0; i--) {
	// for (int j = img.getHeight() - 1; j >= 0 ; j--) {
	// if(img.getRGB(i, j) == -1) {
	// right = i + 1;
	// tFStruct.setRight(new Point(i, j));
	// break outerloop;
	// }
	// }
	// }
	//
	// int newWidth = right - left ;
	// int newHeight = bottom - top ;
	//
	// BufferedImage newImg = new BufferedImage(newWidth, newHeight,
	// BufferedImage.TYPE_INT_RGB);
	//
	// for (int i = top, countY = 0; i < bottom; i++, countY++)
	// for (int j = left, countX = 0; j < right; j++, countX++)
	// newImg.setRGB(countX, countY, img.getRGB(j, i));
	//
	// return newImg;
	// }

	private static BufferedImage useBorderFilter(BufferedImage img) {
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, borderMatrix), ConvolveOp.EDGE_NO_OP, null);
		return op.filter(img, null);
	}

	private static BufferedImage setBlackAndWhite(BufferedImage img) {
		BufferedImage result = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D graph = result.createGraphics();

		graph.drawImage(img, 0, 0, null);
		return result;
	}

	// private static BufferedImage setGreyScale(BufferedImage img) {
	// BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(),
	// BufferedImage.TYPE_BYTE_GRAY);
	// Graphics2D graph = result.createGraphics();
	//
	// graph.drawImage(img, 0, 0, null);
	// return result;
	// }
	//
	// private static BufferedImage removeWhitePoints(BufferedImage img) {
	// for (int i = 0; i < img.getWidth() - 1; i++)
	// for (int j = 0; j < img.getHeight() - 1; j++)
	// if (img.getRGB(i, j) == -1) {
	// BufferedImage tmp = checkNeighbourhood(img, i, j, 0, 1);
	// if (tmp != null) img = tmp;
	// }
	//
	// return img;
	// }

	private static BufferedImage mergeNearestWhitePoints(BufferedImage img) {
		Graphics2D g = img.createGraphics();
		g.setColor(Color.WHITE);

		for (int k = 0; k < 20; k++) {
			for (int i = 0; i < img.getWidth() - 1; i++) {
				for (int j = 0; j < img.getHeight() - 1; j++) {
					if (img.getRGB(i, j) == -1) {
						Vector<Integer> vec = checkNeighbourhood(img, i, j, 2);

						if (vec.size() == 2) {
							g.drawLine(i, j, vec.get(0), vec.get(1));
						}
					}
				}
			}
		}
		return img;
	}

	private static Vector<Integer> checkNeighbourhood(BufferedImage img, int x,
			int y, int r) {
		Vector<Integer> vect = new Vector<>();

		for (int i = x - r; i <= x + r; i++) {
			for (int j = y - r; j <= y + r; j++) {
				if (i >= 0 && j >= 0 && i < img.getWidth()
						&& j < img.getHeight()) {
					if (img.getRGB(i, j) == -1 && !(i == x && j == y)) {
						vect.addElement(i);
						vect.addElement(j);
						return vect;
					}
				}
			}
		}

		return vect;
	}

	// private static BufferedImage checkNeighbourhood(BufferedImage img, int x,
	// int y,
	// int last, int counter) {
	// int cnt = -last;
	// int firstX = 0, firstY = 0;
	//
	// for (int i = x - 1; i <= x + 1; i++) {
	// for (int j = y - 1; j <= y + 1; j++) {
	// if (i >= 0 && j >= 0 && i < img.getWidth() && j < img.getHeight())
	// if (img.getRGB(i, j) == -1 && !(i == x && j == y)) {
	// if (cnt == 0) { firstX= i; firstY = j; }
	// if (last == 1) last--;
	// cnt++;
	// }
	// }
	// }
	//
	// if (counter < 4 && cnt < 2) {
	// if (cnt == 1) {
	// BufferedImage tmp = null;
	//
	// if (firstX >= x && firstY >= y)
	// tmp = checkNeighbourhood(img, firstX, firstY, last+1, counter+1);
	// if (tmp != null) {
	// img.setRGB(x, y, -16777216); // set to Black
	// return img;
	// }
	// } else {
	// img.setRGB(x, y, -16777216);
	// return img;
	// }
	// }
	//
	// return null;
	// }

	public static BufferedImage setBlackAndWhiteNonStrict(BufferedImage img) {
		for (int i = 0; i < img.getWidth() - 1; i++) {
			for (int j = 0; j < img.getHeight() - 1; j++) {
				if (img.getRGB(i, j) != -1 && img.getRGB(i, j) != -16777216) {
					img.setRGB(i, j, -1);
				}
			}
		}

		return img;
	}

	public static BufferedImage processImage(BufferedImage img,
			Vector<SOMVector> inputs) {
		return mergeNearestWhitePoints(setBlackAndWhite(setBlackAndWhiteNonStrict(useBorderFilter(removeBackground(
				medianFilter(img), inputs)))));
	}

	public static BufferedImage removeBackground(BufferedImage img,
			Vector<SOMVector> inputs) {
		Map<SOMVector, Integer> map = new HashMap<SOMVector, Integer>();
		Vector<SOMVector> bckCols = new Vector<SOMVector>();
		int old = 0;

		for (SOMVector s : inputs) {
			map.put(s, 0);
		}

		for (int i = 0; i < img.getWidth() - 1; i++) {
			for (int j = 0; j < img.getHeight() - 1; j++) {
				SOMVector v = new SOMVector(new Color(img.getRGB(i, j)));
				for (SOMVector vec : inputs) {
					if (v.euclideanDist(vec) < 0.02) {
						old = map.get(vec);
						map.put(vec, old + 1);
						break;
					}
				}
			}
		}

		int prc10 = (int) (0.1 * img.getWidth() * img.getHeight());

		for (Map.Entry<SOMVector, Integer> entry : map.entrySet()) {
			if (entry.getValue() >= prc10) {
				bckCols.addElement(entry.getKey());
			}
		}

		for (int i = 0; i < img.getWidth() - 1; i++) {
			for (int j = 0; j < img.getHeight() - 1; j++) {
				for (SOMVector vec : bckCols) {
					SOMVector v = new SOMVector(new Color(img.getRGB(i, j)));
					if (v.euclideanDist(vec) < 0.02) {
						img.setRGB(i, j, -16777216);
					}
				}
			}
		}

		return img;
	}

	public static BufferedImage medianFilter(BufferedImage img) {
		MedianFilter fil = new MedianFilter(3);
		return fil.filter(img);
	}

	public static BufferedImage sharpen(BufferedImage img) {
		float data[] = { -1.0f, -1.0f, -1.0f, -1.0f, 9.0f, -1.0f, -1.0f, -1.0f,
				-1.0f };
		Kernel kernel = new Kernel(3, 3, data);
		ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
				null);
		return convolve.filter(img, null);
	}

	public static List<Pattern> createLearningSet(int cnt, Vector<SOMVector> inputs) {
		List<Pattern> result = new ArrayList<Pattern>();
		Pattern temp = null;
		
		BufferedImage[] images = null;
		try {
			images = loadImages();
		} catch (IOException e) {}
		
		for (int i = 0; i < cnt; i++) {
			System.out.println(i);
			temp = new Pattern(processImage(images[i], inputs), new double[] {outputs[i]});
			result.add(temp);
		}
		Collections.shuffle(result);
		
		return result;
	}
	
	private static BufferedImage[] loadImages() throws IOException {
		BufferedImage[] result = new BufferedImage[22];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = ImageIO.read(new File("tmp/segmented/" + (i+1) + ".png"));
		}
		
		System.out.println("loaded " + result.length);
		
		return result;
	}
	
	private static final double[] outputs = { 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0 };

}