package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import java.util.ArrayList;

/**
 * subcategory has:
 * 	budget amount, stored per month
 * 	amount spent
 * 	purchace list
 * 	compute difference
 * 
 * @author LEHMANIT17
 *
 */
public class BudgetsubCategory extends ExpenceOperations{
	
	private ArrayList<Expence> expences;
	private String subCategory;
	private Money totalSpent;
	private Money budgetAmount;

	public BudgetsubCategory(String subCategoryName) {
		this.totalSpent = new Money();
		this.budgetAmount = new Money();
		expences = new ArrayList<>();
		this.subCategory = subCategoryName;
	}
	
	public BudgetsubCategory(String subCategoryName, Money budgetAmount) {
		this.totalSpent = new Money();
		this.budgetAmount = budgetAmount;
		this.subCategory = subCategoryName;
		expences = new ArrayList<>();
	}
	
	public BudgetsubCategory(String subCategoryName, Money budgetAmount, ArrayList<Expence> expences) {
		this.expences = expences;
		this.totalSpent = new Money();
		this.budgetAmount = budgetAmount;
		this.subCategory = subCategoryName;
		expences = new ArrayList<>();
	}
	
	public void addExpences(ArrayList<Expence> expences) {
		totalSpent = totalSpent.add(getPurchaceTotal(expences));
		this.expences.addAll(expences);
	}
	
	public void addExpence(Expence expence) {
		totalSpent = totalSpent.add(expence.getMoney());
		expences.add(expence);
	}
	
	public void removeExpence(Expence expence) {
		totalSpent = totalSpent.subtract(expence.getMoney());
		expences.remove(expence);
	}
	
	public ArrayList<Expence> getExpences() {
		return expences;
	}
	
	/**
	 * 1 for current month, 2 for current year, 3+ for everything
	 * @param timePeriod
	 * @return
	 */
	public Money getTotalSpentCurrent(int timePeriod) {
		Date current = new Date();
		if (timePeriod == 1) {
			return getPurchaceTotal(getData(expences, current.getMonth(), current.getYear()));
		} else if (timePeriod == 2) {
			return getPurchaceTotal(getData(expences, current.getYear()));
		} else {
			return getPurchaceTotal(expences);
		}


	}

	
	public String getSubcategory() {
		return subCategory;
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
	 * Stored per month
	 * @param budgetAmount
	 * @param timeFrame
	 */
	public void setBudgetAmount(Money budgetAmount, int timeFrame) {
		if (timeFrame == 1) {
			this.budgetAmount =  budgetAmount; // per month
		}	else {
			this.budgetAmount = budgetAmount.divide(12); // per year
		}
		
	}
	
}
	
	
	
	


