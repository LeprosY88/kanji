package andrei.bratuhin.frontend;

import inf.v3d.obj.Line;
import inf.v3d.obj.Sphere;
import inf.v3d.view.ViewerPanel;

import java.util.Iterator;
import java.util.List;

import org.abratuhi.bahnde.model.Station;

import andrei.bratuhin.model.StationProviderStub;




public class VisualizationPanel2 extends ViewerPanel {
	private final Frontend frontend;
	

	public VisualizationPanel2(Frontend frontend) {
		super();

		this.frontend = frontend;

		setVisible(true);
		
	}

	

public void myrepaint(){
	clear();
	List<Station> station = StationProviderStub.getStations();
	for (Station i : station) {
		List<Double> coords = i.getCoordinates();
		Sphere sph = new Sphere( coords.get(0), coords.get(1),  0);
		sph.setRadius(0.1);
		sph.setColor(255, 0, 0);
		sph.register();	
		
		addObject3D(sph);
	}
}

}