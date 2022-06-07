package model;

import net.sf.json.JSONObject;

public class AntiTask extends Task
{
	private int date;
	//all supported types
	public final static String types[]={"Cancellation"};

	public AntiTask()
	{

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

	public AntiTask(String name, String type, double startTime, double duration, int date)
	{
		super(name, type, startTime, duration);
		this.date=date;
	}

	//is the type valid
	public static boolean validType(String type)
	{
		for(String item: types)
		{
			if(item.equals(type)) return true;
		}
		return false;
	}

	public void setDate(int date)
	{
		this.date = date;
	}

	public int getDate()
	{
		return date;
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
