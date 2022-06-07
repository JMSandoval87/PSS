package model;

import net.sf.json.JSONObject;

import java.util.ArrayList;

public class TransientTask extends Task
{
	private int date;
	//all supported types
	public final static String types[]={"Visit", "Shopping", "Appointment"};

	public TransientTask()
	{

	}

	public void setDate(int date)
	{
		this.date = date;
	}

	public String toString()
	{
		String content="";
		content=content+"Name: "+name+"\n";
		content=content+"Type: "+type+"\n";
		content=content+"Date: "+date+"\n";
		content=content+"Start time: "+startTime+"\n";
		content=content+"Duration: "+duration;
		return content;
	}

	public TransientTask(String name, String type, double startTime, double duration, int date)
	{
		super(name, type, startTime, duration);
		this.date=date;
	}

	public static boolean validType(String type)
	{
		for(String item: types)
		{
			if(item.equals(type)) return true;
		}
		return false;
	}

	public int getDate()
	{
		return date;
	}

	public ArrayList<Period> getPeriods()
	{
		ArrayList<Period> periods=new ArrayList<>();
		Period period=new Period(date, startTime, duration);
		periods.add(period);
		return periods;
	}

	public void saveToJson(JSONObject subJson)
	{
		subJson.put("Name", name);
		subJson.put("Type", type);
		subJson.put("StartTime", startTime);
		subJson.put("Duration", duration);
		subJson.put("Date", date);
	}

	public void loadFromJson(JSONObject subJson)
	{
		name=subJson.getString("Name");
		type=subJson.getString("Type");
		startTime=subJson.getDouble("StartTime");
		duration=subJson.getDouble("Duration");
		date=subJson.getInt("Date");
	}
}
