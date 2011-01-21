package andrei.bratuhin.frontend;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.abratuhi.bahnde.db.DbDataGetter;
import org.abratuhi.bahnde.model.RouteComputer;
import org.abratuhi.bahnde.model.Station;
import org.abratuhi.bahnde.output.Route;

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
	private JButton print;
	private JButton export;

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
		print = new JButton("Print");
		export = new JButton("Export");

		compute.addActionListener(this);
		print.addActionListener(this);
		export.addActionListener(this);
		
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
		
		Box box5 = new Box(BoxLayout.X_AXIS);
		box5.add(compute);
		box5.add(print);
		box5.add(export);
		
		add(box1);
		add(box2);
		add(box3);
		add(box4);
		add(box5);

		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(compute)) {
			String from = (String) start.getSelectedItem();
			String to = (String) ziel.getSelectedItem();
			String time = year.getText() + "-" + month.getText() + "-" + day.getText() + " " + hours.getText() + ":" + minutes.getText();
			
			Station sfrom = DbDataGetter.getStation(from);
			Station sto = DbDataGetter.getStation(to);
			Date date = new Date();
			try{date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);}catch(ParseException ex){}
			
			Route route = new RouteComputer().getRoute(sfrom, sto, date, null, null);
			
			frontend.getWindow().getFahrplan().updateRoute(route);
		}
		else if (e.getSource().equals(print)) {
			PrinterJob pj = PrinterJob.getPrinterJob();
			pj.setPrintable(frontend.getWindow().getFahrplan().getRoute());
			if (pj.printDialog()) {
				try {
					pj.print();
				} catch (PrinterException ex) {
					System.out.println(e);
				}
			}
		}
	}
}
