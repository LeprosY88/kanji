package gpl.java.abratuhi.src.gui.blocks;

import gpl.java.abratuhi.src.model.User;
import gpl.java.abratuhi.src.net.message.prototype.Message;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class StatusWindow extends JPanel{
	
	public DefaultTableModel model = new DefaultTableModel();
	public JTable table = new JTable(model);
	public final String[] columns = {"User", "Status:General", "Status:Chat", "Status:Brainstorm"};
	//public String[][] users_and_stats = {	{"coiouhkc", "logged in"},
	//										{"Krola", "submitted theme"}	};
	//public in[] stats;
	
	public StatusWindow(){
		//
		for(int i=0; i<columns.length; i++){
			model.addColumn(columns[i]);
		}
		//table.setEnabled(false);
		add(new JScrollPane(table));
		//
		/*table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    TableColumn col = table.getColumnModel().getColumn(0);
	    col.setPreferredWidth(100);*/
	    //
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    setBorder(new TitledBorder("Status"));
		
	}
	
	public void refresh(ArrayList<User> users){
		/* clear table */
		int rows = model.getRowCount();
		for(int i=0; i<rows; i++){
			model.removeRow(0);
		}
		/* fill table */
		for(int i=0; i<users.size(); i++){
			model.addRow(new Object[]{users.get(i).nickname, 
										users.get(i).getStatus(Message.MSG_HEAD_STATUS_GENERAL),
										users.get(i).getStatus(Message.MSG_HEAD_STATUS_CHAT),
										users.get(i).getStatus(Message.MSG_HEAD_STATUS_BRAINSTORM)});
		}
		/* repaint */
		//repaint();
		/* to stdout */
		//printTable();
	}
	
	public void sortUsers(){//TODO
		
	}
	
	public void printTable(){
		for(int i=0; i<model.getRowCount(); i++){
			for(int j=0; j<model.getColumnCount(); j++){
				System.out.print(model.getValueAt(i, j) + "\t"+"|"+"\t");
			}
			System.out.println();
		}
	}
}
