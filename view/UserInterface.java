package view;

import java.util.Scanner;

public class UserInterface
{
	public final static Scanner keyboard=new Scanner(System.in);

	public static int menu()
	{
		System.out.println("\n===Menu===");
		System.out.println("1 Create a task");
		System.out.println("2 View a task");
		System.out.println("3 Delete a task");
		System.out.println("4 Edit schedule");
		System.out.println("5 Save schedule to file");
		System.out.println("6 Load schedule from file");
		System.out.println("7 View schedule for one day");
		System.out.println("8 View schedule for one week");
		System.out.println("9 View schedule for one month");
		System.out.println("0 Exit");
		System.out.print("Your choice: ");
		int choice=keyboard.nextInt();
		keyboard.nextLine();
		return choice;
	}

	//input time
	public static double inputTime(String tip)
	{
		while(true)
		{
			System.out.print(tip+" (format: hour minute in 24-hour): ");
			int hour = UserInterface.keyboard.nextInt();
			int minute = UserInterface.keyboard.nextInt();
			if(hour<0 || hour>24 || minute<0 || minute>60) System.out.println("Invalid format, please input again.");
			else return minute/15*0.25+hour;
		}
	}

	//is the date valid
	public static boolean validDate(int year, int month, int day)
	{
		//days of each month
		int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		//leap year
		if ( ( year % 4 == 0 && year % 100 != 0 ) || ( year % 400 == 0 ) ) arr[1] = 29;
		else arr[1] = 28;
		if (month > 0 && month < 13)
		{
			if ( day <= arr[month - 1] && day > 0 ) return true;
		}
		return false;
	}

	//input date
	public static int inputDate(String tip)
	{
		while(true)
		{
			System.out.print(tip+" (format: year month day): ");
			int year = UserInterface.keyboard.nextInt();
			int month = UserInterface.keyboard.nextInt();
			int day=UserInterface.keyboard.nextInt();
			if(validDate(year, month, day)) return year*10000+month*100+day;
			else System.out.println("Invalid date, please input again.");
		}
	}

	//input frequency
	public static int inputFrequency(String tip)
	{
		while(true)
		{
			System.out.print(tip);
			int frequency = UserInterface.keyboard.nextInt();
			if(frequency==1 || frequency==7) return frequency;
			else System.out.println("Only 1 or 7 is allowed, please input again.");
		}
	}
}
