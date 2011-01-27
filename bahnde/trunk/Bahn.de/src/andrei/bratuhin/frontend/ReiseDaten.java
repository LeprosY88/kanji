package andrei.bratuhin.frontend;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
	private JCheckBox boxS;
	private JCheckBox boxRE;
	private JCheckBox boxIRE;
	private JCheckBox boxIC;
	private JCheckBox boxEC;
	private JCheckBox boxICE;

	public ReiseDaten(Frontend frontend) {
		super();

		this.frontend = frontend;

		setVisible(true);
		setBorder(new TitledBorder("Reisedaten"));
		setSize(new Dimension(200, 50));
		setPreferredSize(new Dimension(50, 50));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		List<Station> stations = DbDataGetter.getStations();
		List<String> stationNames = new Vector<String>();
		for (Station station : stations) {
			stationNames.add(station.getName());
		}
		String[] items = stationNames.toArray(new String[0]);

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
		boxS = new JCheckBox();
		boxRE = new JCheckBox();
		boxIRE = new JCheckBox();
		boxIC = new JCheckBox();
		boxEC = new JCheckBox();
		boxICE = new JCheckBox();
		box3.add(new JLabel("S"));
		box3.add(boxS);
		box3.add(new JLabel("RE"));
		box3.add(boxRE);
		box3.add(new JLabel("IRE"));
		box3.add(boxIRE);
		box3.add(new JLabel("IC"));
		box3.add(boxIC);
		box3.add(new JLabel("EC"));
		box3.add(boxEC);
		box3.add(new JLabel("ICE"));
		box3.add(boxICE);

		Box box4 = new Box(BoxLayout.X_AXIS);
		box4.add(new JLabel("Departure time (HH:mm)"));
		box4.add(hours);
		box4.add(new JLabel(":"));
		box4.add(minutes);

		Box box5 = new Box(BoxLayout.X_AXIS);
		box5.add(new JLabel("Departure date (yyyy-MM-dd)"));
		box5.add(year);
		box5.add(new JLabel("-"));
		box5.add(month);
		box5.add(new JLabel("-"));
		box5.add(day);

		Box box6 = new Box(BoxLayout.X_AXIS);
		box6.add(compute);
		box6.add(print);
		box6.add(export);

		add(box1);
		add(box2);
		add(box3);
		add(box4);
		add(box5);
		add(box6);

		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(compute)) {
			String from = (String) start.getSelectedItem();
			String to = (String) ziel.getSelectedItem();
			String time = year.getText() + "-" + month.getText() + "-"
					+ day.getText() + " " + hours.getText() + ":"
					+ minutes.getText();

			Station sfrom = DbDataGetter.getStation(from);
			Station sto = DbDataGetter.getStation(to);
			Date date = new Date();
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
			} catch (ParseException ex) {
			}

			Route route = new RouteComputer().getRoute(sfrom, sto, date, null,
					null);

			frontend.getWindow().getFahrplan().updateRoute(route);

			frontend.getWindow().getVisualizationPanel().repaint();
		} else if (e.getSource().equals(print)) {
			PrinterJob pj = PrinterJob.getPrinterJob();
			pj.setPrintable(frontend.getWindow().getFahrplan().getRoute());
			if (pj.printDialog()) {
				try {
					pj.print();
				} catch (PrinterException ex) {
					System.out.println(e);
				}
			}
		} else if (e.getSource().equals(export)) {
			Route route = frontend.getWindow().getFahrplan().getRoute();

			JFileChooser fc = new JFileChooser();
			int result = fc.showSaveDialog(this.frontend.getWindow());

			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(file)));
					bw.write(route.getText());
					bw.flush();
					bw.close();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

		}
	}
}
