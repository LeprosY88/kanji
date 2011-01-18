package andrei.bratuhin.frontend;

import andrei.bratuhin.frontend.Frontend;
import andrei.bratuhin.frontend.MainWindow;


public class Frontend
{
	private final MainWindow window;
	
	public static void main(String[] args)
	{
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

