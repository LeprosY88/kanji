package andrei.bratuhin.frontend;

import org.apache.log4j.BasicConfigurator;

import andrei.bratuhin.frontend.Frontend;
import andrei.bratuhin.frontend.MainWindow;


public class Frontend
{
	private final MainWindow window;
	
	public static void main(String[] args)
	{
		BasicConfigurator.configure();
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

