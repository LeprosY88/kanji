package org.abratuhi.mmorpg.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.abratuhi.mmorpg.graphics.MMORPG_GraphicsEngine;
import org.abratuhi.mmorpg.model.MMORPG_Map;
import org.abratuhi.mmorpg.model.MMORPG_Terrain;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Interactive Editor for MMORPG maps.
 * Allows for placing all types of units and terrain available on the map.
 * Allows for storing and loading of maps.
 * @author Alexei Bratuhin
 *
 */
@SuppressWarnings("serial")
public class MapEditor extends JFrame implements WindowListener, ActionListener, MouseListener{
	
	public MMORPG_GraphicsEngine ge = new MMORPG_GraphicsEngine();
	public MMORPG_Map map = new MMORPG_Map();
	
	DefaultMutableTreeNode terraintreeRootnode = new DefaultMutableTreeNode(new String("Map Editor"));
	public JTree terrainTree = new JTree(terraintreeRootnode);
	public JPanel terrainMap = new JPanel();
	public HashMap<String, Image> type2image = new HashMap<String, Image>();
	
	JButton jbuttonSave = new JButton("Save");
	JButton jbuttonLoad = new JButton("Load");
	
	public MapEditor(){
		super("MMORPG_MapEditor");
		setPreferredSize(new Dimension(800, 400));
		setLocation(new Point(100, 100));
		setVisible(true);
		addWindowListener(this);
		
		terrainMap.addMouseListener(this);
		
		// load/save buttons
		JPanel jbuttons = new JPanel();
		jbuttons.add(jbuttonSave);
		jbuttons.add(jbuttonLoad);
		jbuttonSave.addActionListener(this);
		jbuttonLoad.addActionListener(this);
		
		// explorer tree of possible map elements
		JPanel left = new JPanel(new BorderLayout());
		left.add(new JScrollPane(terrainTree), BorderLayout.CENTER);
		left.add(jbuttons, BorderLayout.SOUTH);
		
		// map view
		JPanel right = new JPanel(new BorderLayout());
		right.add(new JScrollPane(terrainMap), BorderLayout.CENTER);
		terrainMap.setPreferredSize(new Dimension(MMORPG_Map.XSIZE, MMORPG_Map.YSIZE));
		
		JSplitPane all = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		all.setDoubleBuffered(true);
		all.setResizeWeight(0.2); 
		this.getContentPane().add(all);
		
		pack();
	}
	
	/**
	 * Add terrains to existing map object from existing map file.
	 * @param resource	filename (absolute path to file)
	 */
	@SuppressWarnings("unchecked")
	public void loadTerrainTree(String resource){
		SAXBuilder saxbuilder = new SAXBuilder();
		Document doc = null;
		try {
			doc = saxbuilder.build(new File(resource));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element docroot = doc.getRootElement();
		Element terrainroot = docroot.getChild("terrains");
		DefaultMutableTreeNode terraintreeRootnodeTerrain = new DefaultMutableTreeNode("Terrains");
		terraintreeRootnode.add(terraintreeRootnodeTerrain);
		List<Element> terrainElements = terrainroot.getChildren("terrain");
		for(int i=0; i<terrainElements.size(); i++){
			Element cterrainElement = terrainElements.get(i);
			String cterrainType = cterrainElement.getAttributeValue("type");
			terraintreeRootnodeTerrain.add(new DefaultMutableTreeNode(cterrainType));
			type2image.put(cterrainType, ge.loadImage(cterrainType));
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		ge.drawMap((Graphics2D)terrainMap.getGraphics(), 
				this, 
				map, 
				terrainMap.getBounds(), 
				new Point(terrainMap.getBounds().x+terrainMap.getBounds().width/2,
						terrainMap.getBounds().y+terrainMap.getBounds().height/2));
	}
	
	public static void main(String[] args){
		MapEditor me = new MapEditor();
		me.loadTerrainTree("terrain.xml");
		me.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		map.terrains.add(new MMORPG_Terrain(terrainTree.getSelectionPath().getLastPathComponent().toString(), e.getX(), e.getY()));
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.repaint();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {System.exit(0);}
	@Override
	public void windowClosing(WindowEvent arg0) {System.exit(0);}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jbuttonLoad)){
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(this);
			File selectedMap = fc.getSelectedFile();
			
			map = MMORPG_Map.loadMap(selectedMap.getAbsolutePath());
			
			repaint();
		}
		if(e.getSource().equals(jbuttonSave)){
			JFileChooser fc = new JFileChooser();
			fc.showSaveDialog(this);
			File selectedMap = fc.getSelectedFile();
			
			map.saveMap(selectedMap.getAbsolutePath());
		}
	}
}
