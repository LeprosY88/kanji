package org.abratuhi.mobilekanji.model;

public class LessonUnit
{	
	String original;
	String uid;
	String transcription;
	String meaning;
	
	public LessonUnit(String original, String uid, String transcription, String meaning)
	{
		this.original = original;
		this.uid  = uid;
		this.transcription = transcription;
		this.meaning = meaning;
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

}
