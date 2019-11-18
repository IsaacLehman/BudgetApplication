package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class UserInteraction {

	DataBackend dBackend;
	ArrayList<BudgetCategory> budgetCategories;
	ArrayList<Expence> expences;

	public UserInteraction(String csvFN, String backupBudgetFN, String userBudgetFn, Context context) throws IOException {
		dBackend = new DataBackend(csvFN, backupBudgetFN, userBudgetFn, context);
		budgetCategories = dBackend.getBudget();
		expences = dBackend.getExpences();
	}

	public void FillBudget_w_Expenses() {
		if (expences.size() == 0 || budgetCategories.size() == 0) {
			return;
		}
		//ui.dBackend.getPurchaceTotal(dBackend.getData(ui.expences,"food","eating out"));

		for (BudgetCategory budgetCategory : budgetCategories) {
			for (BudgetsubCategory bs : budgetCategory.getSubCategories()) {
				String category = budgetCategory.getCategory();
				String subCategory = bs.getSubcategory();
				bs.addExpences(dBackend.getData(expences, category, subCategory));
				//Log.e("MainActivity",budgetCategory.getCategory() + " - " + bs.getSubcategory() +  bs.getExpences().toString());
			}
		}
	}

	private static String capitalize(String str)
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public void addExpence(Expence expence) {
		expences.add(expence);
		for (BudgetCategory bc : budgetCategories) {
			if (bc.getCategory().equals(expence.getCategory())) {
				bc.addExpence(expence);
				break;
			}
		}
	}

	public void removeExpence(Expence expence) {
		for (BudgetCategory bc : budgetCategories) {
			if (bc.getCategory().equals(expence.getCategory())) {
				bc.removeExpence(expence);
				break;
			}
		}
	}

	public void setBudget(String category, String subCategory, int timeFrame, Money amount) {
		for (BudgetCategory budgetCategory : budgetCategories) {
			if (budgetCategory.getCategory().equals(category)) {
				for (BudgetsubCategory bs : budgetCategory.getSubCategories()) {
					if (bs.getSubcategory().equals(subCategory)) {
						bs.setBudgetAmount(amount, timeFrame);
						break;
					}
				}
				break;
			}
		}
	}

	public Money getTotalBudget(int timeFrame) {
		if (expences.size() == 0 || budgetCategories.size() == 0) {
			return new Money();
		}

		Money bMoney = new Money();
		for (BudgetCategory budgetCategory : budgetCategories) {
			bMoney.add(budgetCategory.getBudgetAmount(timeFrame));
		}
		return bMoney;
	}
	
	public Money getCurrentExpenses(int timeFrame) {
		if (expences.size() == 0 || budgetCategories.size() == 0) {
			return new Money();
		}

		Money bMoney = new Money();
		for (BudgetCategory budgetCategory : budgetCategories) {
			bMoney.add(budgetCategory.getTotalSpent(timeFrame));
		}
		return bMoney;
	}
	
	public Money difference(Money budget, Money expenes) {
		return budget.subtract(expenes);
	}

	public void updateFiles() throws IOException {
		dBackend.writeBudget(budgetCategories);
		dBackend.writeExpences(expences);
	}

	public String[] getCategoryNames() {
		String[] names = new String[budgetCategories.size()];
		for (int i = 0; i < budgetCategories.size(); i++) {
			names[i] = budgetCategories.get(i).getCategory();
		}
		return  names;
	}

	public String[] getSubcategoryNames(String category) {

		for (int i = 0; i < budgetCategories.size(); i++) {
			if (category.equals(budgetCategories.get(i).getCategory())) {
				String[] names = new String[budgetCategories.get(i).getSubCategories().size()];
				for (int j = 0; j <budgetCategories.get(i).getSubCategories().size() ; j++) {
					names[j] = budgetCategories.get(i).getSubCategories().get(j).getSubcategory();
				}
				return names;
			}
		}
		return  new String[]{};
	}

	public void sortExpences() {
		dBackend.sortData(expences);
	}
}
