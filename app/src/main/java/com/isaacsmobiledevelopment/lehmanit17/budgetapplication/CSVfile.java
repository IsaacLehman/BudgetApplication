package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVfile {

	FileTools fileTools;
	String deliminator;
	
	public CSVfile(String fileName, String deliminator, Context context) {
		this.deliminator = deliminator;
		fileTools = new FileTools(fileName, context);
	}

	public ArrayList<Expence> getCSVDataFromFile() throws IOException {
		Scanner scanner = fileTools.openFileScanner();
		ArrayList<Expence> data = new ArrayList<>();
		while (scanner.hasNextLine()) {
			data.add(parseCSVData(scanner.nextLine(), deliminator));
		}
		fileTools.closeFileScanner();
		return data;
	}
	
	public void appendExpences(ArrayList<Expence> expences) throws IOException {
		fileTools.clearFile();
		for (Expence expence : expences) {
			appendPurchace(expence);
		}
	}
	
	public void appendPurchace(Expence data) throws IOException{
		fileTools.AppendToNL(data.toCSVFormat(deliminator));	
	}
	
	public void deletePurchace(Expence expence) {
		fileTools.deleteLine(expence.toCSVFormat(deliminator));
	}
	
	public Expence parseCSVData(String data, String deliminator) {
		ArrayList<String> out = new ArrayList<>();
		Scanner scanner = new Scanner(data);
		scanner.useDelimiter(deliminator);
		while (scanner.hasNext()) {
			out.add(scanner.next());
		}
		scanner.close();
		return arrayListToPurchace(out);
	}
	
	public Expence arrayListToPurchace(ArrayList<String> data) {
		return new Expence(data);
	}
	
	public void clearFile() throws IOException {
		fileTools.clearFile();
	}

}
