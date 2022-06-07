package controller;

import model.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import view.UserInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

//the operations on this schedule
public class ScheduleOperation
{
	//find a task by name
	private static Task findTask(ArrayList<Task> tasks, String name)
	{
		for (Task task : tasks)
		{
			if (task.getName().equalsIgnoreCase(name)) return task;
			//the anti tasks are stored in recurring task
			if (task instanceof RecurringTask)
			{
				RecurringTask rTask = (RecurringTask) task;
				for (AntiTask antiTask : rTask.getAntiTasks())
					if (antiTask.getName().equalsIgnoreCase(name)) return antiTask;
			}
		}
		return null;
	}

	//is the tasks have collision
	private static boolean hasCollision(ArrayList<Task> tasks)
	{
		for (int i = 0; i < tasks.size() - 1; i++)
		{
			Task a = tasks.get(i);
			for (int j = i + 1; j < tasks.size(); j++)
			{
				Task b = tasks.get(j);
				if (hasCollision(a, b)) return true;
			}
		}
		return false;
	}

	//is the task has collision with other tasks
	private static boolean hasCollision(ArrayList<Task> tasks, Task task)
	{
		for (Task a : tasks)
		{
			if (hasCollision(a, task)) return true;
		}
		return false;
	}

	//is two tasks have collision
	private static boolean hasCollision(Task a, Task b)
	{
		for (Period c : a.getPeriods())
		{
			for (Period d : b.getPeriods())
			{
				if (c.overlap(d)) return true;
			}
		}
		return false;
	}

	//is the anti task is suitable for the recurring task
	private static boolean hasMatch(RecurringTask a, AntiTask b)
	{
		for (Period c : a.getPeriods())
		{
			if (c.equals(b)) return true;
		}
		return false;
	}

	//add the anti task to the recurring task
	private static boolean addAntiTask(ArrayList<Task> tasks, AntiTask b)
	{
		for (Task task : tasks)
		{
			if (task instanceof RecurringTask)
			{
				RecurringTask rTask = (RecurringTask) task;
				if (hasMatch(rTask, b))
				{
					rTask.addAntiTask(b);
					return true;
				}
			}
		}
		return false;
	}

	//create one task
	public static void createTask(ArrayList<Task> tasks)
	{
		//input type
		System.out.println("Recurring Task types: " + Arrays.toString(RecurringTask.types));
		System.out.println("Anti Task types: " + Arrays.toString(AntiTask.types));
		System.out.println("Transient Task types: " + Arrays.toString(TransientTask.types));
		System.out.print("Input task type: ");
		String type = UserInterface.keyboard.nextLine();
		//check type
		if (!RecurringTask.validType(type) && !AntiTask.validType(type) && !TransientTask.validType(type))
		{
			System.out.println("Invalid task type.");
			return;
		}
		//input name
		System.out.print("Input task name: ");
		String name = UserInterface.keyboard.nextLine();
		if (findTask(tasks, name) != null)
		{
			System.out.println("Duplicated task name.");
			return;
		}
		//input start time and duration
		double startTime = UserInterface.inputTime("Input start time");
		double duration = UserInterface.inputTime("Input duration");
		//different tasks
		if (RecurringTask.validType(type))
		{
			int startDate = UserInterface.inputDate("Input start date");
			int endDate = UserInterface.inputDate("Input end date");
			int frequency = UserInterface.inputFrequency("Input frequency: ");
			RecurringTask task = new RecurringTask(name, type, startTime, duration, startDate, endDate, frequency);
			if (hasCollision(tasks, task)) System.out.println("Has overlap with existed tasks.");
			else tasks.add(task);
		}
		else if (AntiTask.validType(type))
		{
			int date = UserInterface.inputDate("Input date");
			AntiTask task = new AntiTask(name, type, startTime, duration, date);
			if (addAntiTask(tasks, task) == false) System.out.println("No match recurring task.");
		}
		else if (TransientTask.validType(type))
		{
			int date = UserInterface.inputDate("Input date");
			TransientTask task = new TransientTask(name, type, startTime, duration, date);
			if (hasCollision(tasks, task)) System.out.println("Has overlap with existed tasks.");
			else tasks.add(task);
		}
		//invalid type
		else
		{
			System.out.println("Invalid task type.");
		}
	}

	//view a task
	public static void viewTask(ArrayList<Task> tasks)
	{
		System.out.print("Input task name: ");
		String name = UserInterface.keyboard.nextLine();
		Task task = findTask(tasks, name);
		if (task == null) System.out.println("No task is matched.");
		else System.out.println(task);
	}

	//delete a task
	public static void deleteTask(ArrayList<Task> tasks)
	{
		System.out.print("Input task name: ");
		String name = UserInterface.keyboard.nextLine();
		Task task = findTask(tasks, name);
		//can't find it
		if (task == null)
		{
			System.out.println("No task is matched.");
			return;
		}
		//recurring task
		if (task instanceof RecurringTask || task instanceof TransientTask)
		{
			tasks.remove(task);
			return;
		}
		//anti task
		tasks.remove(task);
		//can't remove it
		if (hasCollision(tasks))
		{
			System.out.println("Remove this task will make collision.");
			//add it back
			addAntiTask(tasks, (AntiTask) task);
		}
	}

	//edit a task
	public static void editTask(ArrayList<Task> tasks)
	{
		System.out.print("Input task name: ");
		String name = UserInterface.keyboard.nextLine();
		Task task = findTask(tasks, name);
		//input name
		System.out.print("Input task name: ");
		name = UserInterface.keyboard.nextLine();
		if (findTask(tasks, name) != null)
		{
			System.out.println("Duplicated task name.");
			return;
		}
		//input start time and duration
		double startTime = UserInterface.inputTime("Input start time");
		double duration = UserInterface.inputTime("Input duration");
		//different tasks
		if (task instanceof RecurringTask)
		{
			RecurringTask rTask = (RecurringTask) task;
			int startDate = UserInterface.inputDate("Input start date");
			int endDate = UserInterface.inputDate("Input end date");
			int frequency = UserInterface.inputFrequency("Input frequency: ");
			//backup
			String originalName = rTask.getName();
			rTask.setName(name);
			double originalStartTime = rTask.getStartTime();
			rTask.setStartTime(startTime);
			double originalDuration = rTask.getDuration();
			rTask.setDuration(duration);
			int originalStartDate = rTask.getStartDate();
			rTask.setStartDate(startDate);
			int originalEndData = rTask.getEndDate();
			rTask.setEndDate(endDate);
			int originalFrequency = rTask.getFrequency();
			rTask.setFrequency(frequency);
			//has collision, restore
			if (hasCollision(tasks))
			{
				System.out.println("Modification will cause collision.");
				rTask.setName(originalName);
				rTask.setStartTime(originalStartTime);
				rTask.setDuration(originalDuration);
				rTask.setStartDate(originalStartDate);
				rTask.setEndDate(originalEndData);
				rTask.setFrequency(originalFrequency);
			}
		}
		else if (task instanceof AntiTask)
		{
			AntiTask aTask = (AntiTask) task;
			int date = UserInterface.inputDate("Input date");
			//backup
			String originalName = aTask.getName();
			aTask.setName(name);
			double originalStartTime = aTask.getStartTime();
			aTask.setStartTime(startTime);
			double originalDuration = aTask.getDuration();
			aTask.setDuration(duration);
			int originalDate = aTask.getDate();
			aTask.setDate(date);
			//has collision, restore
			if (hasCollision(tasks))
			{
				System.out.println("Modification will cause collision.");
				aTask.setName(originalName);
				aTask.setStartTime(originalStartTime);
				aTask.setDuration(originalDuration);
				aTask.setDate(originalDate);
			}
		}
		else if (task instanceof TransientTask)
		{
			TransientTask tTask = (TransientTask) task;
			int date = UserInterface.inputDate("Input date");
			//backup
			String originalName = tTask.getName();
			tTask.setName(name);
			double originalStartTime = tTask.getStartTime();
			tTask.setStartTime(startTime);
			double originalDuration = tTask.getDuration();
			tTask.setDuration(duration);
			int originalDate = tTask.getDate();
			tTask.setDate(date);
			//has collision, restore
			if (hasCollision(tasks))
			{
				System.out.println("Modification will cause collision.");
				tTask.setName(originalName);
				tTask.setStartTime(originalStartTime);
				tTask.setDuration(originalDuration);
				tTask.setDate(originalDate);
			}
		}
	}

	//save task to json file
	public static void saveTasks(ArrayList<Task> tasks)
	{
		System.out.print("Input file name: ");
		String fileName = UserInterface.keyboard.nextLine();

		try
		{
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
			JSONObject json = new JSONObject();
			for (Task task : tasks)
			{
				JSONObject subJson = new JSONObject();
				task.saveToJson(subJson);
				json.accumulate("tasks", subJson);
				if (task instanceof RecurringTask)
				{
					RecurringTask rTask = (RecurringTask) task;
					for (AntiTask antiTask : rTask.getAntiTasks())
					{
						subJson = new JSONObject();
						task.saveToJson(subJson);
						json.accumulate("tasks", subJson);
					}
				}
			}
			osw.write(json.toString());
			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println(e.getMessage());
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	//load tasks from json file
	public static void loadTasks(ArrayList<Task> tasks)
	{
		System.out.print("Input file name: ");
		String fileName = UserInterface.keyboard.nextLine();

		try
		{
			InputStreamReader input = new InputStreamReader(new FileInputStream(new File(fileName)),"UTF-8");
			char cbuf[] = new char[10000];
			int len =input.read(cbuf);
			input.close();
			String text =new String(cbuf,0,len);
			JSONObject obj = JSONObject.fromObject(text.substring(text.indexOf("{")));
			JSONArray arr = obj.getJSONArray("tasks");
			for(int i=0;i<arr.size();i++)
			{
				JSONObject subObj = arr.getJSONObject(i);
				String type=subObj.getString("Type");
				if(RecurringTask.validType(type))
				{
					RecurringTask task=new RecurringTask();
					task.loadFromJson(subObj);
					tasks.add(task);
				}
				else if(AntiTask.validType(type))
				{
					AntiTask task=new AntiTask();
					task.loadFromJson(subObj);
					addAntiTask(tasks, task);
				}
				else if(TransientTask.validType(type))
				{
					TransientTask task=new TransientTask();
					task.loadFromJson(subObj);
					tasks.add(task);
				}
				else
				{
					System.out.println("Wrong file form.");
					break;
				}
			}
			if(hasCollision(tasks)) System.out.println("Wrong file format.");
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println(e.getMessage());
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	//view a tasks in that day
	private static boolean viewTaskByDay(ArrayList<Task> tasks, int date)
	{
		System.out.println("Date " + date + ":\n-------");
		boolean hasOutput = false;
		for (Task task : tasks)
		{
			ArrayList<Period> periods = task.getPeriods();
			for (Period period : periods)
			{
				if (period.getDate() == date)
				{
					hasOutput = true;
					System.out.println(task);
					System.out.println("-------");
					break;
				}
			}
		}
		return hasOutput;
	}

	//view tasks in a given day
	public static void viewTaskByDay(ArrayList<Task> tasks)
	{
		int startDate = UserInterface.inputDate("Input start date");
		if (viewTaskByDay(tasks, startDate) == false) System.out.println("No record.");
	}

	//view tasks in a given week
	public static void viewTaskByWeek(ArrayList<Task> tasks)
	{
		boolean hasOutput = false;
		int startDate = UserInterface.inputDate("Input start date");
		for (int i = 0; i < 7; i++)
		{
			if (viewTaskByDay(tasks, startDate)) hasOutput = true;
			startDate = RecurringTask.nextDay(startDate);
		}
		if (hasOutput == false) System.out.println("No record.");
	}

	//view tasks in a given month
	public static void viewTaskByMonth(ArrayList<Task> tasks)
	{
		boolean hasOutput = false;
		int startDate = UserInterface.inputDate("Input start date");
		for (int i = 0; i < 30; i++)
		{
			if (viewTaskByDay(tasks, startDate)) hasOutput = true;
			startDate = RecurringTask.nextDay(startDate);
		}
		if (hasOutput == false) System.out.println("No record.");
	}
}
