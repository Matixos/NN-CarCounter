package pl.com.mat.carcounter.common;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage scene;
	//private Graphics2D graph;        // toDrawSignWhenCount

	public void changeScene(BufferedImage scene) {
		this.scene = scene;
		//this.graph = this.scene.createGraphics();
		
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(scene, 0, 0, null);
	}

}