package andrei.bratuhin.frontend;

import org.abratuhi.bahnde.db.DbUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class Frontend
{
	private final static Logger LOG = Logger.getLogger(Frontend.class);
	
	private final MainWindow window;
	
	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		
		String pathDb = System.getProperty("bahndb");
		if(pathDb != null && pathDb.length() > 0){
			DbUtil.DB_NAME = pathDb;
		}
		
		new Frontend();
	}
	
	public Frontend()
	{
		this.window = new MainWindow(this);
	}
	
	public MainWindow getWindow()
	{
		return this.window;
	}
	
	
}

