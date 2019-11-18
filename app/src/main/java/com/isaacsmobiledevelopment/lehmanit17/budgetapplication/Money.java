package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import java.util.Scanner;

public class Money {
	
	

	public Money(int dollar, int cent) {
		m = dollar * 100 + cent;
	}
	
	private int m;

    public Money(int m) {
        this.m = m;
    }
    
    public Money() {
        this.m = 0;
    }

    public int getDollars() {
        return m / 100;
    }

    public int getCents() {
        return m % 100;
    }

    public int get() {
        return m;
    }
    
    public void set(int n) {
		m = n;
	}

    public Money add(Money other) {
        return new Money(m + other.get());
    }

    public Money subtract(Money other) {
        return new Money(m - other.get());
    }
    
    public Money multiply(int n) {
    	return new Money(m * n);
    }
    
    public Money divide(int n) {
    	return new Money(m / n);
    }
	
	public Money(String amount) {
		parseAmount(amount);
	}
	
	public void parseAmount(String amount) {
		Scanner parser = new Scanner(amount);
		parser.useDelimiter("\\.");
		m = Integer.parseInt(parser.next())*100;
		m += Integer.parseInt(parser.next());
		parser.close();
	}
	
	
	public String toCSVFormatString() {
		return getDollars() + "." + getCents();
	}
	
	public String toString() {
		if (m < 0 && getDollars() == 0) {
			return "$-" + getDollars() + "." + Math.abs(getCents());
		}
		if (Math.abs(getCents()) < 10) {
			return "$" + getDollars() + ".0" + Math.abs(getCents());
		}
		return "$" + getDollars() + "." + Math.abs(getCents());
	}
}
