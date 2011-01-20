package andrei.bratuhin.frontend;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.abratuhi.bahnde.model.Station;

import andrei.bratuhin.model.StationProviderStub;

public class ReiseDaten extends JPanel {
	private final Frontend frontend;

	private JComboBox Start;
	private JTextField Ziel;
	private JTextField Abfahrtszeit;
	

	public ReiseDaten(Frontend frontend) {
		super();
		setVisible(true);
		setBorder(new TitledBorder("Reisedaten"));
		setSize(new Dimension(200, 200));
		setLayout(new GridLayout(3, 1));

		String[] items = {"Bochum","Essen","Muelheim/Ruhr","Duisburg", "Oberhausen",
				"Bottrop", "Gelsenkirchen",	"Hattingen", "Witten", "Dortmund", "Unna",
				"Schwerte", "Hagen", "Herne"};
			
		

		Start = new JComboBox(items);
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