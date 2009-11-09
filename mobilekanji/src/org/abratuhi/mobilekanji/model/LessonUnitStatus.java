package org.abratuhi.mobilekanji.model;

public class LessonUnitStatus implements Comparable
{
	private String status;
	
	private LessonUnitStatus(String status)
	{
		this.status = status;
	}
	
	public static LessonUnitStatus GREEN_STATUS()
	{
		return new LessonUnitStatus("green");
	}
	
	public static LessonUnitStatus RED_STATUS()
	{
		return new LessonUnitStatus("red");
	}
	
	public boolean isGreen()
	{
		return status.equals("green");
	}
	
	public boolean isRed()
	{
		return status.equals("red");
	}

	public int compareTo(Object o) {
		return status.compareTo(((LessonUnitStatus)o).status);
	}
}
