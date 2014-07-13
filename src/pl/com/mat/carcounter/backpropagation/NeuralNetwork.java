package pl.com.mat.carcounter.backpropagation;


import java.io.Serializable;
import java.util.ArrayList;


public class NeuralNetwork implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	private double learningRate = 0.1f;
    private double momentum = 0.9f;
    
	private final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
	private final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
	private final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
    
	private int neuronCnt;
 
    public NeuralNetwork(int input, int hidden, int output) {
    	this.neuronCnt = input + hidden + output;
    	
        for (int i = 0; i < input; i++) {		// input layer
            Neuron neuron = new Neuron();
            inputLayer.add(neuron);
        }
        
        Neuron bias = new Neuron();
        
        for (int i = 0; i < hidden; i++) {		// hidden layer
        	Neuron neuron = new Neuron();
            neuron.addInConnectionsS(inputLayer);
            neuron.addBiasConnection(bias);
            hiddenLayer.add(neuron);
        }
        
        for (int i = 0; i < output; i++) {		// output layer
        	Neuron neuron = new Neuron();
            neuron.addInConnectionsS(hiddenLayer);
            neuron.addBiasConnection(bias);
            outputLayer.add(neuron);
        }
 
        for (Neuron neuron : hiddenLayer) {					// initialize random weights
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : outputLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
 
        Neuron.counter = 0;						// reset id counters
    }
 
    /*private double getRandom() {					// rand from -1 to 1  for test
        return (Math.random() * 2 - 1);
    }*/
    
    private double getRandom() {				// rand agile
    	double range = 4/Math.sqrt(neuronCnt);
        return (Math.random() * range - (2/Math.sqrt(neuronCnt)));
    }
 
    public void setInput(double inputs[]) {
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setOutput(inputs[i]);
        }
    }
 
    public double[] getOutput() {
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputLayer.size(); i++)
            outputs[i] = outputLayer.get(i).getOutput();
        return outputs;
    }
 
    public void activateNetwork() {
        for (Neuron n : hiddenLayer)
            n.calculateOutput();
        for (Neuron n : outputLayer)
            n.calculateOutput();
    }
    
    public void applyBackpropagation(double expectedOutput[]) {
        int i = 0;
        for (Neuron n : outputLayer) {					// update weights for output layer
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double ak = n.getOutput();
                double ai = con.getFromNeuron().getOutput();
                double desiredOutput = expectedOutput[i];
 
                double partialDerivative = -ak * (1 - ak) * ai
                        * (desiredOutput - ak);
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
            i++;
        }
 
        for (Neuron n : hiddenLayer) {					// update weights for hidden layer
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double aj = n.getOutput();
                double ai = con.getFromNeuron().getOutput();
                double sumKoutputs = 0;
                int j = 0;
                for (Neuron out_neu : outputLayer) {
                    double wjk = out_neu.getConnection(n.id).getWeight();
                    double desiredOutput = expectedOutput[j];
                    double ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
 
                double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
    }
    
    public void changeLearningRate(double learningRate) {
    	this.learningRate = learningRate;
    }
    
    public void changeMomentum(double momentum) {
    	this.momentum = momentum;
    }

}