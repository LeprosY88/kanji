package org.abratuhi.mobilekanji.model;

public class LessonUnit implements Comparable
{	
	String original;
	String uid;
	String transcription;
	String meaning;
	LessonUnitStatus status;
	
	public LessonUnitStatus getStatus() {
		return status;
	}
	
	public void setStatus(LessonUnitStatus status)
	{
		this.status = status;
	}

	public LessonUnit(String original, String uid, String transcription, String meaning)
	{
		this.original = original;
		this.uid  = uid;
		this.transcription = transcription;
		this.meaning = meaning;
		this.status = LessonUnitStatus.RED_STATUS();
	}
	
	public String getOriginal()
	{
		return this.original;
	}
	
	public String getUid()
	{
		return this.uid;
	}
	
	public String getTranscription()
	{
		return this.transcription;
	}
	
	public String getMeaning()
	{
		return this.meaning;
	}

	public int compareTo(Object o) {
		if(o instanceof LessonUnit)
		{
			LessonUnit lu = (LessonUnit) o;
			return this.getStatus().compareTo(lu.getStatus());
		}
		else
		{
			return 0;
		}
	}

}
