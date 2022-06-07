package model;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RecurringTask extends Task
{
	private int startDate;
	private int endDate;
	private int frequency;
	//the anti tasks for this recurring task. Date as key
	private HashMap<Integer, AntiTask> antiTasks=new HashMap<>();
	//all supported types
	public static final String types[]={"Class", "Study", "Sleep", "Exercise", "Work", "Meal"};

	public RecurringTask()
	{

	}

	public int getStartDate()
	{
		return startDate;
	}

	public int getEndDate()
	{
		return endDate;
	}

	public int getFrequency()
	{
		return frequency;
	}

	public String toString()
	{
		String content="";
		content=content+"Name: "+name+"\n";
		content=content+"Type: "+type+"\n";
		content=content+"Start date: "+startDate+"\n";
		content=content+"End date: "+endDate+"\n";
		content=content+"Start time: "+startTime+"\n";
		content=content+"Duration: "+duration+"\n";
		content=content+"Frequency: "+frequency;
		return content;
	}

	public void addAntiTask(AntiTask task)
	{
		antiTasks.put(task.getDate(), task);
	}

	public ArrayList<AntiTask> getAntiTasks()
	{
		ArrayList<AntiTask> tasks=new ArrayList<>();
		for(AntiTask task: antiTasks.values()) tasks.add(task);
		return tasks;
	}

	public RecurringTask(String name, String type, double startTime, double duration, int startDate, int endDate, int frequency)
	{
		super(name, type, startTime, duration);
		this.startDate = startDate;
		this.endDate = endDate;
		this.frequency = frequency;
	}

	public static boolean validType(String type)
	{
		for(String item: types)
		{
			if(item.equals(type)) return true;
		}
		return false;
	}

	public static int nextDay(int date)
	{
		int year=date/10000;
		int month=date%10000/100;
		int day=date%100;
		int monthDays;
		switch(month)
		{
		case 2 :
			if(year % 400 == 0||(year % 4 ==4 && year % 100 !=0)) monthDays=29;
			else monthDays = 28;
			break;
		case 1:  case 3: case 5: case 7: case 8:  case 10 : case 12:
			monthDays = 31;
			break;
		default:
			monthDays = 30;
			break;
		}
		day++;
		if (day > monthDays)
		{
			month++;
			day = 1;
		}
		if(month > 12)
		{
			month = 1;
			year++;
		}
		return year*10000+month*100+day;
	}

	private int nextDate(int date)
	{
		for(int i=0;i<frequency;i++) date=nextDay(date);
		return date;
	}

	public void setStartDate(int startDate)
	{
		this.startDate = startDate;
	}

	public void setEndDate(int endDate)
	{
		this.endDate = endDate;
	}

	public void setFrequency(int frequency)
	{
		this.frequency = frequency;
	}

	public ArrayList<Period> getPeriods()
	{
		ArrayList<Period> periods=new ArrayList<>();
		//get each date
		int date=startDate;
		while(date<=endDate)
		{
			//neglect this date by anti task
			if(antiTasks.containsKey(date)) continue;
			Period period=new Period(date, startTime, duration);
			periods.add(period);
			//get next date
			date=nextDate(date);
		}
		return periods;
	}

	public void saveToJson(JSONObject subJson)
	{
		subJson.put("Name", name);
		subJson.put("Type", type);
		subJson.put("StartTime", startTime);
		subJson.put("Duration", duration);
		subJson.put("StartDate", startDate);
		subJson.put("EndDate", endDate);
		subJson.put("Frequency", frequency);
	}

	public void loadFromJson(JSONObject subJson)
	{
		name=subJson.getString("Name");
		type=subJson.getString("Type");
		startTime=subJson.getDouble("StartTime");
		duration=subJson.getDouble("Duration");
		startDate=subJson.getInt("StartDate");
		endDate=subJson.getInt("EndDate");
		frequency=subJson.getInt("Frequency");
	}
}
