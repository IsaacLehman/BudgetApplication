package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * day is 1-31
 * month is 1-12
 * year is normal, ie. 1999
 * @author LEHMANIT17
 *
 */
public class Date {
	
	private int day, month, year;
	
	public Date(int month, int day, int year) {
		this.month = month;
		this.day = day;
		this.year = year;
	}
	
	/**
	 * in form month/day/year
	 * @param date
	 */
	public Date(String date) {
		Scanner parser = new Scanner(date);
		parser.useDelimiter("/");
		month = parser.nextInt();
		day = parser.nextInt();
		year = parser.nextInt();
		parser.close();
	}
	
	/**
	 * makes a new instance of date set to current date
	 */
	public Date() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY");  
		LocalDateTime now = LocalDateTime.now();  
		Scanner parser = new Scanner(dtf.format(now));
		parser.useDelimiter("/");
		month = parser.nextInt();
		day = parser.nextInt();
		year = parser.nextInt();
		parser.close();
	}
	
	/** 
	 * returns 0 if same
	 * -1 if before
	 * 1 if after
	 * @param date
	 * @return
	 */
	public int compareDate(Date date) {
		if (date.year > year) {
			return -1;
		} else if (date.year < year) {
			return 1;
		} else {
			if (date.month > month) {
				return -1;
			} else if (date.month < month) {
				return 1;
			} else {
				if (date.day > day) {
					return -1;
				} else if (date.day < day) {
					return 1;
				} else {
					return 0;
				}
			}
		}
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}
	
	public String toString() {
		return month + "/" + day + "/" + year; 
	}
	
	public Date getCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY");  
		LocalDateTime now = LocalDateTime.now();  
		return new Date(dtf.format(now));  
	}
}
