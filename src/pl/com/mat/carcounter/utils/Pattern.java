package pl.com.mat.carcounter.utils;


import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Pattern implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private double[] input;
    
    private transient BufferedImage imgInput;
    
    private double[] output;
    
    private double[] result;
	
	public Pattern(double[] input, double[] output) {
    	this.input = input.clone();
    	this.output = output.clone();
    }
	
	public Pattern(BufferedImage img, double[] output) {
		this.imgInput = img;
		this.output = output.clone();
	}
    
    public double[] getInput() {
    	return(input);
    }
    
    public void setInput(double[] input) {
    	this.input = input;
    }
    
    public double[] getOutput() {
    	return(output);
    }
    
    public BufferedImage getImgInput() {
    	return imgInput;
    }

	public double[] getResult() {
		return result;
	}

	public void setResult(double[] result) {
		this.result = result;
	}

}