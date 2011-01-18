package andrei.bratuhin.frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.abratuhi.bahnde.db.DbUtil;

public class SQLConsole extends JFrame implements ActionListener{
	
	private JTextArea input;
	private JButton submit;
	
	
	public SQLConsole(){
		super();
		setSize(800, 600);
		setTitle("Console");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		input = new JTextArea();
		input.setColumns(60);
		input.setRows(20);
		input.setLineWrap(true);
		input.setWrapStyleWord(true);
		
		submit = new JButton("Submit");
		submit.addActionListener(this);
		
		JPanel panelc = new JPanel();
		panelc.setLayout(new FlowLayout());
		panelc.add(new JScrollPane(input));
		
		JPanel panels = new JPanel();
		panels.setLayout(new FlowLayout());
		panels.add(submit);
		
		add(panelc, BorderLayout.CENTER);
		add(panels, BorderLayout.SOUTH);
		
		repaint();
	}


	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(submit)){
			System.out.println("Submitting");
			try{
				String sql = input.getText();
				String[] sqls = sql.split(";");
				input.setText("");
				
				Connection connection = DbUtil.getConnection();
				Statement stmt = connection.createStatement();
				for(int i=0; i<sqls.length; i++){
					stmt.execute(sqls[i]);
				}
				
				connection.close();
				DbUtil.shutdown();
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			
			
		}
	}

}
