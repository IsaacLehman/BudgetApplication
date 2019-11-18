package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import java.util.ArrayList;

public class BudgetCategory {

	private ArrayList<BudgetsubCategory> budgetsubCategories;
	private String category;
	private Money budgetAmount; // stored per month
	
	public BudgetCategory(String categoryName) {
		budgetsubCategories = new ArrayList<>();
		budgetAmount = new Money();
		this.category = categoryName;
	}
	
	public void addSubCategory(BudgetsubCategory budgetsubCategory) {
		budgetsubCategories.add(budgetsubCategory);
		budgetAmount = budgetAmount.add(budgetsubCategory.getBudgetAmount(1));
	}

	public void removeSubCategory(BudgetsubCategory budgetsubCategory) {
		budgetAmount.subtract(budgetsubCategory.getBudgetAmount(1));
		budgetsubCategories.remove(budgetsubCategory);
	}
	
	
	public ArrayList<BudgetsubCategory> getSubCategories() {
		return budgetsubCategories;
	}
	
	/**
	 * 1 gets month
	 * 2 gets year
	 * 3 gets all
	 * @param timeFrame
	 * @return
	 */
	public Money getBudgetAmount(int timeFrame) {
		if (timeFrame == 1) {
			return budgetAmount; // per month
		}	else {
			return budgetAmount.multiply(12); // per month
		}
		
	}
	
	/**
	 * 1 for month
	 * 2 for year
	 * 3+ for everything
	 * @param timeFrame
	 * @return
	 */
	public Money getTotalSpent(int timeFrame) {
		Money totalSpent = new Money();
		for (BudgetsubCategory budgetsubCategory : budgetsubCategories) {
			totalSpent = totalSpent.add(budgetsubCategory.getTotalSpentCurrent(timeFrame));
		}
		return totalSpent;
	}
	
	public Money getDifference(int timeframe) {
		return getBudgetAmount(timeframe).subtract(getTotalSpent(timeframe));
	}
	
	public void addExpence(Expence expence) {
		for (BudgetsubCategory budgetsubCategory : budgetsubCategories) {
			if (budgetsubCategory.getSubcategory().equals(expence.getSubCategory())) {
				budgetsubCategory.addExpence(expence);
				break;
			}
		}
	}
	
	public void removeExpence(Expence expence) {
		for (BudgetsubCategory budgetsubCategory : budgetsubCategories) {
			if (budgetsubCategory.getSubcategory().equals(expence.getSubCategory())) {
				budgetsubCategory.removeExpence(expence);
				break;
			}
		}
	}
	
	public String getCategory() {
		return category;
	}

	public String toString_w_Money() {
		StringBuilder sb = new StringBuilder();
		sb.append(category);
		sb.append(";");
		for (BudgetsubCategory budgetsubCategory : budgetsubCategories) {
			sb.append(budgetsubCategory.getSubcategory());
			sb.append("|");
			sb.append(budgetsubCategory.getBudgetAmount(1).toCSVFormatString()); // in terms of months
			sb.append(",");
		}
		return sb.toString();
	}
	
	public String toString_wo_Money() {
		StringBuilder sb = new StringBuilder();
		sb.append(category);
		sb.append(";");
		for (BudgetsubCategory budgetsubCategory : budgetsubCategories) {
			sb.append(budgetsubCategory.getSubcategory());
			sb.append(",");
		}
		return sb.toString();
	}

}
