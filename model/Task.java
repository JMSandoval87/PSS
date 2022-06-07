package model;

import net.sf.json.JSONObject;

import java.util.ArrayList;

//the base class for task
public class Task
{
	protected String name;
	protected String type;
	protected double startTime;
	protected double duration;

	public Task()
	{

	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setStartTime(double startTime)
	{
		this.startTime = startTime;
	}

	public void setDuration(double duration)
	{
		this.duration = duration;
	}

	public Task(String name, String type, double startTime, double duration)
	{
		this.name=name;
		this.type=type;
		this.startTime=startTime;
		this.duration=duration;
	}

	public String getName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}

	public double getStartTime()
	{
		return startTime;
	}

	public double getDuration()
	{
		return duration;
	}

	//get all events in each day
	public ArrayList<Period> getPeriods()
	{
		return new ArrayList<>();
	}

	//not implemented here
	public void saveToJson(JSONObject subJson)
	{

	}
}
