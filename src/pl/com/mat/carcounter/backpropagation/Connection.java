package pl.com.mat.carcounter.backpropagation;



import java.io.Serializable;

public class Connection implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private double weight = 0;
    private double prevDeltaWeight = 0; 	// for momentum
    private double deltaWeight = 0;
 
    private final Neuron from;
    private final Neuron to;
    
    public static int counter = 0;
    public final int id; 
 
    public Connection(Neuron fromN, Neuron toN) {
        this.from = fromN;
        this.to = toN;
        this.id = counter;
        counter++;
    }
 
    public double getWeight() {
        return weight;
    }
 
    public void setWeight(double w) {
        weight = w;
    }
 
    public void setDeltaWeight(double w) {
        prevDeltaWeight = deltaWeight;
        deltaWeight = w;
    }
 
    public double getPrevDeltaWeight() {
        return prevDeltaWeight;
    }
 
    public Neuron getFromNeuron() {
        return from;
    }
 
    public Neuron getToNeuron() {
        return to;
    }

}