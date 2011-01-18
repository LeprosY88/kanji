package andrei.bratuhin.frontend;

import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class MainWindow extends JFrame {
	private final Frontend frontend;

	VisualizationPanel2 panel2 = null;

	public MainWindow(Frontend frontend) {
		super();
		setSize(800, 600);
		setTitle("Fahrtplanauskunft");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1, 2));

		this.frontend = frontend;

		JPanel sub = new JPanel();
		sub.setLayout(new GridLayout(2, 1));

		add(sub);
		panel2 = new VisualizationPanel2(this.frontend);
		sub.add(new JScrollPane(new InfoPanel(this.frontend)));
		sub.add(new JScrollPane(new InfoPanel(this.frontend)));
		// add(new JScrollPane(new VisualizationPanel(this.frontend)));
		add(panel2);

		repaint();
		
	}

	
}
