package org.abratuhi.mmorpg.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUIServer extends JPanel{
	
	Server s;
	
	public GUIServer(Server s){
		super();
		
		this.s = s;

		setPreferredSize(s.fieldDimension);		
		this.setLayout(new BorderLayout());
		this.setVisible(true);
	}
	
	public void paintComponent(Graphics gg){
		Graphics2D g = (Graphics2D) gg;
		g.setColor(Color.WHITE);
		g.fill(getBounds());
		g.setColor(Color.BLACK);
		for(int i=0; i<s.units.size(); i++){
			s.units.get(i).draw(g);
		}
	}
	
	public JFrame createFrame(){
		JFrame jf = new JFrame("MMORPG Network Communication Demo");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout());
		jf.getContentPane().add(this, BorderLayout.CENTER);
		jf.setLocation(this.s.fieldLocation);
		jf.setPreferredSize(new Dimension(this.s.fieldDimension.width+20, this.s.fieldDimension.height+50));
		jf.setVisible(true);
		jf.pack();
		return jf;
	}

}
