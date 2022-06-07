package model;

import controller.ScheduleOperation;
import view.UserInterface;

import java.util.ArrayList;

public class Schedule
{
	//all tasks. The anti task is not stored in this list
	private ArrayList<Task> tasks;

	public Schedule()
	{
		tasks=new ArrayList<>();
	}

	public void run()
	{
		while(true)
		{
			int choice= UserInterface.menu();
			if(choice==1) ScheduleOperation.createTask(tasks);
			else if(choice==2) ScheduleOperation.viewTask(tasks);
			else if(choice==3) ScheduleOperation.deleteTask(tasks);
			else if(choice==4) ScheduleOperation.editTask(tasks);
			else if(choice==5) ScheduleOperation.saveTasks(tasks);
			else if(choice==6) ScheduleOperation.loadTasks(tasks);
			else if(choice==7) ScheduleOperation.viewTaskByDay(tasks);
			else if(choice==8) ScheduleOperation.viewTaskByWeek(tasks);
			else if(choice==9) ScheduleOperation.viewTaskByMonth(tasks);
			else if(choice==0) break;
			else System.out.println("Invalid choice.");
		}
	}
}
