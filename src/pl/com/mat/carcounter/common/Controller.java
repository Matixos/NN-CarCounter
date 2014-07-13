package pl.com.mat.carcounter.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import pl.com.mat.carcounter.backpropagation.BackPropagation;
import pl.com.mat.carcounter.backpropagation.NeuralNetwork;
import pl.com.mat.carcounter.som.SOMLattice;
import pl.com.mat.carcounter.som.SOMTrainer;
import pl.com.mat.carcounter.som.SOMVector;
import pl.com.mat.carcounter.utils.ImagePreparer;
import pl.com.mat.carcounter.utils.KeyColorFinder;
import pl.com.mat.carcounter.utils.PatternSupplier;
import pl.com.mat.carcounter.utils.SlidingWindow;


public class Controller implements ActionListener {
	
	private static final int SomLatticeSize = 30;

	private ViewManager win;
	
	private BufferedImage processedImg;
	
	private SlidingWindow slWindow;
	private NeuralNetwork network;
	private BackPropagation bp;

	private Vector<SOMVector> somInput;
	private SOMLattice lattice;
	private SOMTrainer trainer;

	private int returnVal;
	
	private static int ctn = 0;

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("keyColors")) {
			somInput = KeyColorFinder.getKeyColorInputVector(processedImg, win.getKeyColorsSize());
			win.showColorsInPanel(somInput);
		} else if (event.getActionCommand().equals("trainSom")) {
			lattice = new SOMLattice(SomLatticeSize, SomLatticeSize);
			trainer.setTraining(lattice, somInput);
			trainer.start();
		} else if (event.getActionCommand().equals("processSom")) {
			processedImg = lattice.use(processedImg, somInput);
			win.loadImage(processedImg);
		} else if (event.getActionCommand().equals("restProcess")) {
			processedImg = ImagePreparer.processImage(processedImg, somInput);
			win.loadImage(processedImg);
		} else if (event.getActionCommand().equals("runBP")) {
			int lb = slWindow.process(processedImg);
			JOptionPane.showMessageDialog(win, "Success",
					"Recognized " + lb + " cars" ,
					JOptionPane.INFORMATION_MESSAGE);
		} else if (event.getActionCommand().equals("load")) {
			returnVal = win.showFileChooser(win);

			switch (returnVal) {
			case JFileChooser.APPROVE_OPTION:
				try {
					processedImg = ImageIO.read(new File(win.getLoadPath()));
					win.loadImage(processedImg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			win.cleanFileChooser();
		} else if (event.getActionCommand().equals("ldSettings")) {
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(new FileInputStream("baza0.dat"));
				network = (NeuralNetwork) in.readObject();
				in.close();
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {}
			JOptionPane.showMessageDialog(win, "Success loading",
					"Load",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (event.getActionCommand().equals("saveSett")) {
			ObjectOutputStream stream;
			try {
				stream = new ObjectOutputStream(new FileOutputStream("baza" + (ctn++) + ".dat"));
				stream.writeObject(network);
				stream.flush();
				stream.close();
			} catch (IOException e) {}
			JOptionPane.showMessageDialog(win, "Success saveing",
					"Save",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (event.getActionCommand().equals("trainBP")) {
			bp.setPatterns(PatternSupplier.processLearningSet(ImagePreparer.createLearningSet(22, somInput)));
			//bp.setPatterns(null);
			
//			int maxRuns = 50000;
//	        double minErrorCondition = 1.0;
//	        Long time = System.currentTimeMillis();
//	        bp.run(maxRuns, minErrorCondition);
//	        System.out.println("Time: " + (System.currentTimeMillis() - time));
			double res = bp.tenFoldCrossValidation();
			System.out.println("RESULT: " + res);
		}
	}

	public Controller(ViewManager win) {
		this.win = win;
		this.win.addListener(this);
		
		this.trainer = new SOMTrainer();
		
		this.network = new NeuralNetwork(3, 2, 1);
		this.bp = new BackPropagation(network);
		this.slWindow = new SlidingWindow(network);
	}

	public static void main(String[] args) {
		new Controller(new ViewManager());
	}
}
