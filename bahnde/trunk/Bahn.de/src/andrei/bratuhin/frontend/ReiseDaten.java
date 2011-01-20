package andrei.bratuhin.frontend;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ReiseDaten extends JPanel {
	private final Frontend frontend;

	private JComboBox Start;
	private JTextField Ziel;
	private JTextField Abfahrtszeit;
	

	public ReiseDaten(Frontend frontend) {
		super();
		setVisible(true);
		setBorder(new TitledBorder("Reisedaten"));
		setSize(new Dimension(200, 50));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		String[] items = {"Bochum","Essen","Muelheim/Ruhr","Duisburg", "Oberhausen",
				"Bottrop", "Gelsenkirchen",	"Hattingen", "Witten", "Dortmund", "Unna",
				"Schwerte", "Hagen", "Herne"};
			
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.LINE_AXIS));
		
		

		Start = new JComboBox(items);
		Ziel = new JTextField();
		Abfahrtszeit = new JTextField();
		
		panel1.add(new JLabel("Start"));
		panel1.add(Start);
		panel2.add(new JLabel("Ziel"));
		panel2.add(Ziel);
		panel3.add(new JLabel("Abfahrtszeit"));
		panel3.add(Abfahrtszeit);
		this.frontend = frontend;

		add(panel1);
		add(panel2);
		add(panel3);
		
		repaint();
	}


}