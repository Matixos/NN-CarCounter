package pl.com.mat.carcounter.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;

import pl.com.mat.carcounter.som.SOMVector;
import pl.com.mat.carcounter.som.ShowPanel;


public class ViewManager extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int x = 1200, y = 700;

	private JButton keyColors, somTrain, processSom, restProcess, confirm;
	private JTextField maxCols;
	private MainPanel mPanel;
	private JFileChooser fc;
	
	private ShowPanel showPanel;

	private JMenuBar menu;
	private JMenu m1, m2;
	protected JMenuItem p1, p2, p3, p4;

	private Border bord = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK);

	public ViewManager() {
		super("Neural Network Car Recognizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(x-90, y);
		setLocationRelativeTo(null);
		setResizable(false);

		menu = new JMenuBar();
		m1 = new JMenu("File");
		p1 = new JMenuItem("Load Image", KeyEvent.VK_L);
		p2 = new JMenuItem("Load NN Settings", KeyEvent.VK_K);
		p3 = new JMenuItem("Save NN Settings", KeyEvent.VK_S);
		m1.add(p1);
		m1.add(p2);
		m1.add(p3);
		menu.add(m1);
		m2 = new JMenu("Train");
		p4 = new JMenuItem("Train BP");
		m2.add(p4);
		menu.add(m2);
		setJMenuBar(menu);

		fc = new JFileChooser();
		FileFilter fil = new FileFilter() {

			@Override
			public String getDescription() {
				return "Image files .jpg .png";
			}

			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else {
					String path = file.getAbsolutePath().toLowerCase();
					if (path.endsWith(".jpg") || path.endsWith(".png")) {
						return true;
					}
				}
				return false;
			}
		};
		fc.setFileFilter(fil);
		
		showPanel = new ShowPanel();
		
		maxCols = new JTextField(8);
		maxCols.setText("Max Cols");

		keyColors = new JButton("Key Colors");
		somTrain = new JButton("Train Som");
		processSom = new JButton("Process Som");
		restProcess = new JButton("Rest Of Processes");
		confirm = new JButton("Run BP");
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));
		bottom.setBackground(Color.WHITE);
		
		bottom.add(maxCols);
		bottom.add(keyColors);
		bottom.add(somTrain);
		bottom.add(processSom);
		bottom.add(restProcess);
		bottom.add(confirm);

		mPanel = new MainPanel();
		mPanel.setBorder(bord);

		getContentPane().add(BorderLayout.CENTER, mPanel);
		getContentPane().add(BorderLayout.SOUTH, bottom);

		setVisible(true);
	}

	public void addListener(ActionListener aListener) {
		keyColors.setActionCommand("keyColors");
		keyColors.addActionListener(aListener);
		somTrain.setActionCommand("trainSom");
		somTrain.addActionListener(aListener);
		processSom.setActionCommand("processSom");
		processSom.addActionListener(aListener);
		restProcess.setActionCommand("restProcess");
		restProcess.addActionListener(aListener);
		confirm.setActionCommand("runBP");
		confirm.addActionListener(aListener);
		
		p1.setActionCommand("load");
		p1.addActionListener(aListener);
		p2.setActionCommand("ldSettings");
		p2.addActionListener(aListener);
		p3.setActionCommand("saveSett");
		p3.addActionListener(aListener);
		p4.setActionCommand("trainBP");
		p4.addActionListener(aListener);
	}
	
	public int getKeyColorsSize() {
		return Integer.parseInt(maxCols.getText());
	}

	public void loadImage(BufferedImage img)  {
		mPanel.changeScene(img);
	}

	public int showFileChooser(Component parent) {
		return fc.showOpenDialog(parent);
	}

	public String getLoadPath() {
		return fc.getSelectedFile().getPath();
	}

	public void cleanFileChooser() {
		fc.setSelectedFile(new File(""));
	}
	
	public void showColorsInPanel(Vector<SOMVector> vec) {
		showPanel.render(vec);
		showPanel.setVisible(true);
	}
}
