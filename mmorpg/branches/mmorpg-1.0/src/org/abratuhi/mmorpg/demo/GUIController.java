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

public class GUIController extends JPanel implements ActionListener{
	
	Server s;
	
	JButton jb_bkwd = new JButton("<");
	JButton jb_playpause = new JButton("=");
	JButton jb_frwd = new JButton(">");
	
	public GUIController(Server s){
		super();
		
		this.s = s;
		
		this.jb_bkwd.addActionListener(this);
		this.jb_playpause.addActionListener(this);
		this.jb_frwd.addActionListener(this);

		this.add(jb_bkwd);
		this.add(jb_playpause);
		this.add(jb_frwd);
		
		this.setVisible(true);
	}
	
	public JFrame createFrame(){
		JFrame jf = new JFrame("MMORPG Network Communication Demo Controller");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout());
		jf.getContentPane().add(this, BorderLayout.CENTER);
		jf.setLocation(new Point(900, 50));
		//jf.setPreferredSize(new Dimension(200, 100));
		jf.setVisible(true);
		jf.pack();
		return jf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jb_bkwd)){
			s.unstep();
			s.illustrationFrame.repaint();
		}
		if(e.getSource().equals(jb_playpause)){
			if(s.checkRunOK()) s.pause();
			else s.play();
		}
		if(e.getSource().equals(jb_frwd)){
			s.step();
			s.illustrationFrame.repaint();
		}
	}
}
