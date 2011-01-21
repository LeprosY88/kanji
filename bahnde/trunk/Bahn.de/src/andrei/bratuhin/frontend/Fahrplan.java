package andrei.bratuhin.frontend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.abratuhi.bahnde.output.Route;

public class Fahrplan extends JPanel {
	private final Frontend frontend;
	private JTextArea text;
	private Route route;
	
	public Fahrplan(Frontend frontend) {
		super();
		setVisible(true);
		setBorder(new TitledBorder("Fahrplan"));
		setSize(new Dimension(200, 200));
		setPreferredSize(new Dimension(200, 200));
		setLayout(new GridLayout(1, 1));

		this.frontend = frontend;
		
		text = new JTextArea();
		text.setEditable(false);
		text.setColumns(40);
		text.setRows(20);
		
		add(new JScrollPane(text));

		repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		if(route != null){
			text.setText(route.getText());
		}
	}
	
	public void updateRoute(Route route){
		this.route = route;
		
		repaint();
	}

}
