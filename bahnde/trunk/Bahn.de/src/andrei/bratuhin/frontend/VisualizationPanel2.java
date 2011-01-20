package andrei.bratuhin.frontend;

import inf.v3d.obj.Line;
import inf.v3d.obj.Sphere;
import inf.v3d.obj.Text;
import inf.v3d.view.ViewerPanel;

import java.util.List;

import org.abratuhi.bahnde.model.EdgeRoute;
import org.abratuhi.bahnde.model.Station;

import andrei.bratuhin.model.StationProviderStub;

public class VisualizationPanel2 extends ViewerPanel {
	private final Frontend frontend;

	public VisualizationPanel2(Frontend frontend) {
		super();

		this.frontend = frontend;

		setVisible(true);

	}

	public void myrepaint() {
		clear();
		List<Station> stations = StationProviderStub.getStations();
		for (Station station : stations) {
			List<Double> coords = station.getCoordinates();
			Sphere sph = new Sphere(coords.get(0), coords.get(1), 0);
			sph.setRadius(0.5);
			sph.setColor(255, 0, 0);
			sph.register();

			addObject3D(sph);
		
			Text t = new Text(station.getName());
			t.setHeight(1);
			t.setOrigin(coords.get(0) + 0.005, coords.get(1)+ 0.005, 0);
			t.setColor(255, 0, 0);
			
			addObject3D(t);
		
					
			for(Station neighbour : station.getIncidentStations()){
					List<Double> coords1 = neighbour.getCoordinates();	
					Line line = new Line(coords.get(0), coords.get(1), 0, coords1.get(0), coords1.get(1), 0);	
					line.setLinewidth(0.5);
					line.setColor(255, 0, 0);
					
					line.register();	
					
					addObject3D(line);	
			}
			
			
			
			
		}
	}

}