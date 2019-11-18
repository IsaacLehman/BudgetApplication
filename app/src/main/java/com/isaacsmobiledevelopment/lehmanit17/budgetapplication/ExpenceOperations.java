package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenceOperations {
	
	//public ExpenceOperations() {}

	// data operations/manipulations
	public ArrayList<Expence> getData(ArrayList<Expence> data, int year) {
		List<Expence> yearData = data.stream()
				.filter(c -> 
				c.getDate().getYear() == year)
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}

	public ArrayList<Expence> getData(ArrayList<Expence> data, int month, int year) {
		List<Expence> yearData = data.stream()
				.filter(c -> 
				c.getDate().getYear() == year && 
				c.getDate().getMonth() == month)
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}
	
	public ArrayList<Expence> getData(ArrayList<Expence> data,int day, int month, int year) {
		List<Expence> yearData = data.stream()
				.filter(c -> 
				c.getDate().getYear() == year && 
				c.getDate().getMonth() == month)
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}
	
	public ArrayList<Expence> getData(ArrayList<Expence> data, String category,int month, int year) {
		List<Expence> yearData = data.stream()
				.filter(c -> 
				c.getCategory().equals(category) &&
				c.getDate().getYear() == year && 
				c.getDate().getMonth() == month)
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}
	
	
	public ArrayList<Expence> getData(ArrayList<Expence> data, String category, String subCategory, int month, int year) {
		List<Expence> yearData = data.stream()
				.filter(c -> 
				c.getCategory().equals(category) &&
				c.getSubCategory().equals(subCategory) &&
				c.getDate().getYear() == year && 
				c.getDate().getMonth() == month)
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}

	public ArrayList<Expence> getData(ArrayList<Expence> data, String category, String subCategory,  int year) {
		List<Expence> yearData = data.stream()
				.filter(c ->
						c.getCategory().equals(category) &&
								c.getSubCategory().equals(subCategory) &&
								c.getDate().getYear() == year)
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}
	
	public ArrayList<Expence> getData(ArrayList<Expence> data, String category) {
		List<Expence> yearData = data.stream()
				.filter(c -> 
				c.getCategory().equals(category))
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}
	
	public ArrayList<Expence> getData(ArrayList<Expence> data, String category, String subCategory) {
		List<Expence> yearData = data.stream()
				.filter(c -> 
				c.getCategory().equals(category) &&
				c.getSubCategory().equals(subCategory))
				.collect(Collectors.toList());
		return new ArrayList<Expence>(yearData);
	}

	/**
	 * Sorts Expenses by date
	 * @param data
	 */
	public void sortData(ArrayList<Expence> data) {
		if (data != null && data.size() > 0) {
			Collections.sort(data, new Comparator<Expence>() {
				public int compare(Expence e1, Expence e2) {
					return e1.getDate().compareDate(e2.getDate());
				}
			});
		}
	}

	public Money getPurchaceTotal(ArrayList<Expence> data) {
		Money money = new Money();
		for (Expence expence : data) {
			money = money.add(expence.getMoney());
		}
		return money;
	}

	public ArrayList<Expence> getDataTimeframe(ArrayList<Expence> data, String cat, String subCat, int timeFrame) {
		Date dateC = new Date();
		if (timeFrame == 1) {
			return getData(data, cat, subCat, dateC.getMonth(), dateC.getYear());
		} else if (timeFrame == 2) {
			return getData(data, cat, subCat, dateC.getYear());
		} else {
			return getData(data, cat, subCat);
		}
	}

}
