package model;

//the task event in some day
public class Period
{
	//starting date and time
	private double startDateTime;
	private double duration;

	public int getDate()
	{
		return (int)startDateTime;
	}

	public Period(double startDate, double startTime, double duration)
	{
		this.startDateTime=startDate+startTime/100;
		this.duration=duration;
	}

	private double start()
	{
		return startDateTime;
	}

	private double end()
	{
		return startDateTime+duration/100;
	}

	//is overlapping with another
	public boolean overlap(Period b)
	{
		if(b.start()>=start() && b.start()<end()) return true;
		if(b.end()>start() && b.end()<=end()) return true;
		if(start()>=b.start() && start()<b.end()) return true;
		if(end()>b.start() && end()<=b.end()) return true;
		return false;
	}

	//is same period
	public boolean equals(Period b)
	{
		if(startDateTime==b.startDateTime && duration==b.duration) return true;
		else return false;
	}
}
