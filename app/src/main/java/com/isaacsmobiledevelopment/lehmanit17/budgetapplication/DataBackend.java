package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Gets all the file data on open and writes all the file data upon close
 * @author LEHMANIT17
 *
 */
public class DataBackend extends ExpenceOperations{

	CSVfile csvFile;
	budgetFile budgetKeyFile;
	budgetFile userBudgetFile;
	
	public DataBackend(String csvFileName, String budgetKeyFileName, String userBudgetFileName, Context context) {
		csvFile = new CSVfile(csvFileName, ";", context);
		budgetKeyFile = new budgetFile(budgetKeyFileName, ";", ",", context);
		userBudgetFile = new budgetFile(userBudgetFileName, ";", ",", context);
	}
	
	public void resetUserBudgetFile() throws IOException{
		userBudgetFile.fileTools.clearFile();
		ArrayList<BudgetCategory> budgetKeys = budgetKeyFile.getbudgetDataFromFile();
		userBudgetFile.writebudgetDataToFile(budgetKeys);
	}
	
	public void writeBudget(ArrayList<BudgetCategory> budget) throws IOException {
		userBudgetFile.writebudgetDataToFile(budget);
	}
	
	public ArrayList<BudgetCategory> getBudget() throws IOException {
		return userBudgetFile.getbudgetDataFromFile();
	}

	public void writeExpences(ArrayList<Expence> expences) throws IOException {
		csvFile.appendExpences(expences);
	}
	
	public ArrayList<Expence> getExpences() throws IOException{
		return csvFile.getCSVDataFromFile();
	}
	
}
