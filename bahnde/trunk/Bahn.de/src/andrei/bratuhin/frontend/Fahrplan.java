package andrei.bratuhin.frontend;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Fahrplan extends JPanel {
	private final Frontend frontend;

	public Fahrplan(Frontend frontend) {
		super();
		setVisible(true);
		setBorder(new TitledBorder("Fahrplan"));
		setSize(new Dimension(200, 200));
		setPreferredSize(new Dimension(200, 200));
		setLayout(new GridLayout(1, 1));

		this.frontend = frontend;

		repaint();
	}

}
