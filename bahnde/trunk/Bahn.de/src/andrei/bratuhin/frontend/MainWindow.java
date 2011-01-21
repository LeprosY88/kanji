package andrei.bratuhin.frontend;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import org.abratuhi.bahnde.output.PrintableRoute;

import andrei.bratuhin.model.StationProviderStub;


public class MainWindow extends JFrame implements KeyListener{
	private final Frontend frontend;

	VisualizationPanel2 panel2 = null;

	public MainWindow(Frontend frontend) {
		super();
		setSize(800, 600);
		setTitle("Fahrtplanauskunft");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout (new GridLayout(1,2));	
				
		addKeyListener(this);

		this.frontend = frontend;

		JPanel sub = new JPanel();
		sub.setLayout(new GridLayout(2,1));

		add(sub);
		panel2 = new VisualizationPanel2(this.frontend);
		sub.add(new JScrollPane(new ReiseDaten(this.frontend)));
		sub.add(new JScrollPane(new Fahrplan(this.frontend)));
		// add(new JScrollPane(new VisualizationPanel(this.frontend)));
		add(panel2);
		
		sub.addKeyListener(this);
		panel2.addKeyListener(this);

		repaint();
		
	}	
		private void putConstraint() {
		// TODO Auto-generated method stub
		
	}
		public void paint(Graphics g) {
			super.paint(g);

			if (panel2 != null) {
				panel2.myrepaint();
			}
		}
		
	

	@Override
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyChar() == '!'){
			System.out.println("Bring up the sql window");
			new SQLConsole();
		}
		else if(ke.getKeyChar() == 'p'){
			System.out.println("Bring up the print window");
			PrinterJob pj = PrinterJob.getPrinterJob();
		    pj.setPrintable(new PrintableRoute(StationProviderStub.getStations()));
		    if (pj.printDialog()) {
		      try {
		        pj.print();
		      } catch (PrinterException e) {
		        System.out.println(e);
		      }
		    }
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	
}
