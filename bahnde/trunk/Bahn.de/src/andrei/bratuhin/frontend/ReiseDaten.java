package andrei.bratuhin.frontend;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ReiseDaten extends JPanel implements ActionListener {
	private final Frontend frontend;

	private JComboBox start;
	private JComboBox ziel;
	private JTextField hours;
	private JTextField minutes;
	private JTextField day;
	private JTextField month;
	private JTextField year;
	private JButton compute;

	public ReiseDaten(Frontend frontend) {
		super();

		this.frontend = frontend;
		
		setVisible(true);
		setBorder(new TitledBorder("Reisedaten"));
		setSize(new Dimension(200, 50));
		setPreferredSize(new Dimension(50, 50));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String[] items = { "", "Bochum", "Essen", "Muelheim/Ruhr", "Duisburg",
				"Oberhausen", "Bottrop", "Gelsenkirchen", "Hattingen",
				"Witten", "Dortmund", "Unna", "Schwerte", "Hagen", "Herne" };


		start = new JComboBox(items);
		ziel = new JComboBox(items);
		hours = new JTextField();
		minutes = new JTextField();
		day = new JTextField();
		month = new JTextField();
		year = new JTextField();
		compute = new JButton("Compute");

		compute.addActionListener(this);
		
		Box box1 = new Box(BoxLayout.X_AXIS);
		box1.add(new JLabel("From"));
		box1.add(start);
		
		Box box2 = new Box(BoxLayout.X_AXIS);
		box2.add(new JLabel("To"));
		box2.add(ziel);
		
		Box box3 = new Box(BoxLayout.X_AXIS);
		box3.add(new JLabel("Departure time (HH:mm)"));
		box3.add(hours);
		box3.add(new JLabel(":"));
		box3.add(minutes);
		
		Box box4 = new Box(BoxLayout.X_AXIS);
		box4.add(new JLabel("Departure date (yyyy-MM-dd)"));
		box4.add(year);
		box4.add(new JLabel("-"));
		box4.add(month);
		box4.add(new JLabel("-"));
		box4.add(day);
		
		add(box1);
		add(box2);
		add(box3);
		add(box4);

		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(compute)) {
			String from = (String) start.getSelectedItem();
			String to = (String) ziel.getSelectedItem();
			String time = year.getText() + "-" + month.getText() + "-" + day.getText() + " " + hours.getText() + ":" + minutes.getText();
			System.out.println(from);
			System.out.println(to);
			System.out.println(time);
		}
	}
}
