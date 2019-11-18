package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import java.util.ArrayList;

public class Expence {
	
	
	private Date date;
	private String description, category, subCategory;
	private String amount; // 20.39
	private Money money;
	


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}
	
	public Money getMoney() {
		return money;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getSubCategory() {
		return subCategory;
	}

	
	public Expence(ArrayList<String> data) {
		date = new Date(data.get(0));
		category = data.get(1);
		subCategory = data.get(2);
		description = data.get(3);
		amount = data.get(4); 
		money = new Money(amount);
	}
	
	public Expence(String date, String category, String subCategory, String description, String amount) {
		this.date = new Date(date);
		this.category = category;
		this.subCategory = subCategory;
		this.description = description;
		this.amount = amount;
		money = new Money(amount);
	}

	
	public String toCSVFormat(String deliminator) {
		return date.toString() + ";" + category + ";" + subCategory + ";" + description + ";" + money.getDollars() + "." + money.getCents();
	}
	
	public String toString() {		
		return date.toString() + ", " + category + ", " + subCategory + ", " + description + ", " + money.toString();
	}

}
