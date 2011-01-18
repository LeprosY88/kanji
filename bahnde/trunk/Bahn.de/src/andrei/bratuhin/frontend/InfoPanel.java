package andrei.bratuhin.frontend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;



public class InfoPanel extends JPanel {
	private final Frontend frontend;

	private JTextField Start;
	private JTextField Ziel;
	private JTextField Abfahrtszeit;
	

	public InfoPanel(Frontend frontend) {
		super();
		setVisible(true);
		setBorder(new TitledBorder("Reisedaten"));
		setSize(new Dimension(200, 200));
		setPreferredSize(new Dimension(200, 200));
		setLayout(new GridLayout(7, 2));

		

		Start = new JTextField();
		Ziel = new JTextField();
		Abfahrtszeit = new JTextField();
		
		add(new JLabel("Start"));
		add(Start);
		add(new JLabel("Ziel"));
		add(Ziel);
		add(new JLabel("Abfahrtszeit"));
		add(Abfahrtszeit);
		this.frontend = frontend;

		repaint();
	}


}
