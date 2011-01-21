package andrei.bratuhin.frontend;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.abratuhi.bahnde.output.Route;

import andrei.bratuhin.model.StationProviderStub;

public class MainWindow extends JFrame implements ActionListener {
	private final Frontend frontend;

	private VisualizationPanel2 panel2 = null;
	
	private final Fahrplan fahrplan;

	public MainWindow(Frontend frontend) {
		super();
		setSize(800, 600);
		setTitle("Fahrtplanauskunft");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new GridLayout(1, 2));


		this.frontend = frontend;
		this.fahrplan = new Fahrplan(this.frontend);

		JPanel sub = new JPanel();
		sub.setLayout(new GridLayout(2, 1));

		add(sub);
		panel2 = new VisualizationPanel2(this.frontend);
		sub.add(new JScrollPane(new ReiseDaten(this.frontend)));
		sub.add(new JScrollPane(fahrplan));
		// add(new JScrollPane(new VisualizationPanel(this.frontend)));
		add(panel2);
		
		
		JMenuBar mbar = new JMenuBar();
		mbar.setVisible(true);
		
		setJMenuBar(mbar);
		
		JMenu mfile = new JMenu("File");
		JMenuItem miprint = new JMenuItem("Print");
		JMenuItem misave = new JMenuItem("Save");
		JMenuItem misql = new JMenuItem("SQL Console");
		
		mfile.add(misave);
		mfile.add(miprint);
		mfile.addSeparator();
		mfile.add(misql);
		
		mbar.add(mfile);
		
		miprint.addActionListener(this);
		misave.addActionListener(this);
		misql.addActionListener(this);
		
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (panel2 != null) {
			panel2.myrepaint();
		}
	}

	public Fahrplan getFahrplan() {
		return fahrplan;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("SQL Console")){
			new SQLConsole();
		}
		else if(ae.getActionCommand().equals("Print")){
			PrinterJob pj = PrinterJob.getPrinterJob();
			pj.setPrintable(fahrplan.getRoute());
			if (pj.printDialog()) {
				try {
					pj.print();
				} catch (PrinterException e) {
					System.out.println(e);
				}
			}
		}
		else if(ae.getActionCommand().equals("Save")){
			
		}
	}

}
