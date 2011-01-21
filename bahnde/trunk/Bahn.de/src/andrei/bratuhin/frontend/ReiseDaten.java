package andrei.bratuhin.frontend;

import inf.v3d.obj.Sphere;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.abratuhi.bahnde.model.Station;

public class ReiseDaten extends JPanel implements ActionListener {
	private final Frontend frontend;

	private JComboBox start;
	private JComboBox ziel;
	private JTextArea abfahrtszeitstunden;
	private JTextField abfahrtszeitminuten;
	private JTextField abfahrtszeitdatumtag;
	private JTextField abfahrtszeitdatummonat;
	private JTextField abfahrtszeitdatumjahr;
	private JButton berechnen;

	public ReiseDaten(Frontend frontend) {
		super();
		setVisible(true);
		setBorder(new TitledBorder("Reisedaten"));
		setSize(new Dimension(200, 50));
		setPreferredSize(new Dimension(50, 50));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		String[] items = { "", "Bochum", "Essen", "Muelheim/Ruhr", "Duisburg",
				"Oberhausen", "Bottrop", "Gelsenkirchen", "Hattingen",
				"Witten", "Dortmund", "Unna", "Schwerte", "Hagen", "Herne" };

		JPanel panel1 = new JPanel();

		JPanel panel2 = new JPanel();

		JPanel panel3 = new JPanel();

		JPanel panel4 = new JPanel();

		JPanel panel5 = new JPanel();
		panel5.setLayout(new BoxLayout(panel5, BoxLayout.LINE_AXIS));

		start = new JComboBox(items);
		ziel = new JComboBox(items);
		abfahrtszeitstunden = new JTextArea("         ");
		abfahrtszeitminuten = new JTextField("         ");
		abfahrtszeitdatumtag = new JTextField("         ");
		abfahrtszeitdatummonat = new JTextField("         ");
		abfahrtszeitdatumjahr = new JTextField("         ");
		berechnen = new JButton("Berechnen");

		panel1.add(new JLabel("                                            "));
		panel1.add(new JLabel("Start"));
		panel1.add(new JLabel("                                     "));
		panel1.add(start);
		panel1.add(new JLabel("                                     "));
		panel2.add(new JLabel("                                            "));
		panel2.add(new JLabel("Ziel"));
		panel2.add(new JLabel("                                        "));
		panel2.add(ziel);
		panel2.add(new JLabel("                                     "));
		panel3.add(new JLabel("                                            "));
		panel3.add(new JLabel("Abfahrtszeit (HH:MM)"));
		panel3.add(new JLabel("    "));
		panel3.add(abfahrtszeitstunden);
		panel3.add(new JLabel("  :  "));
		panel3.add(abfahrtszeitminuten);
		panel3.add(new JLabel("                                     "));
		panel4.add(new JLabel("                                            "));
		panel4.add(new JLabel("Datum (TT:MM:JJJJ)"));
		panel4.add(new JLabel("              "));
		panel4.add(abfahrtszeitdatumtag);
		panel4.add(new JLabel("     :     "));
		panel4.add(abfahrtszeitdatummonat);
		panel4.add(new JLabel("     :     "));
		panel4.add(abfahrtszeitdatumjahr);
		panel4.add(new JLabel("                                     "));
		panel5.add(berechnen);

		this.frontend = frontend;

		add(panel1);
		add(panel2);
		add(panel3);
		add(panel4);
		add(panel5);
		repaint();

		berechnen.addActionListener(this);
		start.addActionListener(this);
		ziel.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(start)) {
			String startUp = (String) start.getSelectedItem();
			System.out.println(startUp);
		} else if (e.getSource().equals(ziel)) {
			String zielUp = (String) ziel.getSelectedItem();
			System.out.println(zielUp);
		} else if (e.getSource().equals(abfahrtszeitstunden)) {
			String stundenUp = abfahrtszeitstunden.getText();
			System.out.println(stundenUp);
		}
	}
}
