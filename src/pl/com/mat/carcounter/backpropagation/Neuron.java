package pl.com.mat.carcounter.backpropagation;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Neuron implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static int counter = 0;
	public final int id;  // auto increment, starts at 0
	
	private Connection biasConnection;
	private final double bias = -1;
	private double output;
     
	private ArrayList<Connection> Inconnections = new ArrayList<Connection>();
	private HashMap<Integer,Connection> connectionLookup = new HashMap<Integer,Connection>();
     
    public Neuron(){       
        id = counter;
        counter++;
    }
     
    public void calculateOutput(){
        double s = 0;
        for(Connection con : Inconnections){
            Neuron leftNeuron = con.getFromNeuron();
            double weight = con.getWeight();
            double a = leftNeuron.getOutput(); //output from previous layer
             
            s = s + (weight*a);
        }
        s = s + (biasConnection.getWeight()*bias);
         
        output = sigmoid(s);
    }
 
    private double sigmoid(double x) {
        return 1.0 / (1.0 +  (Math.exp(-x)));
    }
     
    public void addInConnectionsS(ArrayList<Neuron> inNeurons){
        for(Neuron n: inNeurons){
            Connection con = new Connection(n,this);
            Inconnections.add(con);
            connectionLookup.put(n.id, con);
        }
    }
     
    public Connection getConnection(int neuronIndex){
        return connectionLookup.get(neuronIndex);
    }
 
    public void addInConnection(Connection con){
        Inconnections.add(con);
    }
    public void addBiasConnection(Neuron n){
        Connection con = new Connection(n,this);
        biasConnection = con;
        Inconnections.add(con);
    }
    public ArrayList<Connection> getAllInConnections(){
        return Inconnections;
    }
     
    public double getBias() {
        return bias;
    }
    public double getOutput() {
        return output;
    }
    public void setOutput(double o){
        output = o;
    }

}