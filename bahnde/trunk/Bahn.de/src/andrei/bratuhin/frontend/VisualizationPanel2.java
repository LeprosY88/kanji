package andrei.bratuhin.frontend;

import inf.v3d.obj.Line;
import inf.v3d.obj.Sphere;
import inf.v3d.view.ViewerPanel;

import java.util.Iterator;
import java.util.List;




public class VisualizationPanel2 extends ViewerPanel {
	private final Frontend frontend;

	public VisualizationPanel2(Frontend frontend) {
		super();

		this.frontend = frontend;

		setVisible(true);
	}

	
}
