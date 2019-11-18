package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class budgetFile {

	FileTools fileTools;
	String deliminatorC;
	String deliminatorSC;

	public budgetFile(String fileName, String deliminatorC, String deliminatorSC, Context context) {
		this.deliminatorC = deliminatorC;
		this.deliminatorSC = deliminatorSC;
		fileTools = new FileTools(fileName, context);
	}

	public void appendBudget(ArrayList<BudgetCategory> budgetCategory) throws IOException {
		for (BudgetCategory budgetCategory2 : budgetCategory) {
			fileTools.AppendToNL(budgetCategory2.toString_w_Money());
		}
	}
	
	public void deleteCategory(BudgetCategory bs) {
		fileTools.deleteLine(bs.toString_w_Money());
	}
	
//	public void deletesubCategory(BudgetsubCategory bs) {
//		String out = bs.getSubcategory() + "|" + bs.getBudgetAmount(1).toCSVFormatString() + ",";
//		fileTools.deleteLine(out);
//	}

	public ArrayList<BudgetCategory> getbudgetDataFromFile() throws IOException {
		Scanner scanner = fileTools.openFileScanner();
		ArrayList<BudgetCategory> categories = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String nl = scanner.nextLine();
			if (nl.contains("|")) {
				categories.add(parseBudgetData(nl));
			}
		}
		fileTools.closeFileScanner();
		return categories;
	}
	
	public void writebudgetDataToFile(ArrayList<BudgetCategory> categories) throws IOException {
		fileTools.clearFile();
		for (BudgetCategory budgetCategory : categories) {
			fileTools.AppendToNL(budgetCategory.toString_w_Money());
		}
	}

	public BudgetCategory parseBudgetData(String data) {
		Scanner scanner = new Scanner(data);
		scanner.useDelimiter(deliminatorC);
		BudgetCategory bCategory = new BudgetCategory(scanner.next());
		String left = scanner.next();
		scanner.close();

		String[] values = left.split(",");
		// System.out.println(Arrays.toString(values));

		for (String c : values) {
			if (c.contains("|")) {
				String subName = c.substring(0, c.indexOf("|"));
				Money budgetA = new Money(c.substring(c.indexOf("|") + 1));
				BudgetsubCategory bs = new BudgetsubCategory(subName, budgetA);
				bCategory.addSubCategory(bs);
			}
		}

		return bCategory;
	}

}
