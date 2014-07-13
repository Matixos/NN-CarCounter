package pl.com.mat.carcounter.backpropagation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import pl.com.mat.carcounter.utils.Pattern;


public class BackPropagation {
	
	private List<Pattern> patterns;
	private NeuralNetwork network;
	
	public static int fCnt = 1;
	
	public BackPropagation(NeuralNetwork network) {
		this.network = network;
		this.patterns = new ArrayList<Pattern>();
	}
	
	public void addPattern(double[] in, double[] out) {
		this.patterns.add(new Pattern(in, out));
	}
	
	public void addPattern(Pattern pattern) {
		this.patterns.add(pattern);
	}
	
	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;        // have to clone
		/*try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("patterny.bin")));
			this.patterns = (List<Pattern>)in.readObject();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}*/
		/*try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("patterny.bin")));
			os.writeObject(patterns);
			os.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
    public void run(int maxSteps, double minError) {
    	double[] output;
        int i;
        
        double error = 1;
        //for (i = 0; i < maxSteps && error > minError; i++) {  // Train neural network until minError reached or maxSteps exceeded
        for (i = 0; error > minError; i++) {    
        
        	error = 0;
            for (int j = 0; j < patterns.size(); j++) {
                network.setInput(patterns.get(j).getInput());
 
                network.activateNetwork();
 
                output = network.getOutput();
                patterns.get(j).setResult(output);
               // double oError = 0;
                for (int k = 0; k < patterns.get(j).getOutput().length; k++) {
                    double err = Math.pow(output[k] - patterns.get(j).getOutput()[k], 2);
                    error += err;
                }
 
                network.applyBackpropagation(patterns.get(j).getOutput());
            }
            
            error = 1.0/2.0 * error;
            Collections.shuffle(patterns); 					// added recently
        }
         
        System.out.println("Sum of squared errors = " + error + " %");
        System.out.println("##### EPOCH " + i+"\n");
        
        if (i == maxSteps) {
            System.out.println("!Error training try again");
        }
    }
    
    public double tenFoldCrossValidation() {
    	System.out.println("Start 10-fold cross validation");
    	
    	double[] output;
    	double error = 0;
    	double[] results = new double[22];
    	
    	for (int i = 0; i < patterns.size(); i++) {
	    	for (int j = 0; j < patterns.size()-1; j++) {
	            network.setInput(patterns.get(j).getInput());
	
	            network.activateNetwork();
	
	            output = network.getOutput();
	            patterns.get(j).setResult(output);
	
	            network.applyBackpropagation(patterns.get(j).getOutput());
	        }
	    	
	    	network.setInput(patterns.get(patterns.size()-1).getInput());
	    	network.activateNetwork();
	    	
	    	results[i] = 1.0/2.0 * Math.pow(network.getOutput()[0] - patterns.get(patterns.size()-1).getOutput()[0], 2);
	    	
	    	System.out.println("Sample - " + i + " : " + results[i]);
	    	
	    	lastElemToStart();
    	}
    	
    	for (double d: results) {
    		error += d;
    	}
    	
    	System.out.println("Stop 10-fold cross validation");
    	
    	return error/results.length;
    }
    
    private void lastElemToStart() {
    	List<Pattern> newList = new LinkedList<Pattern>();
    	
    	newList.add(patterns.get(patterns.size()-1));
    	newList.addAll(patterns.subList(0, patterns.size()-1));
    	
    	patterns.clear();
    	patterns.addAll(newList);
    }

}