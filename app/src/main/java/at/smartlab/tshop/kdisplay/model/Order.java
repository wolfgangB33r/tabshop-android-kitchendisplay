//  Copyright (C) 2013 Wolfgang Beer. All Rights Reserved.
//  email wolfgang@smartlab.at
//  Schwalbenweg 17
//  4540 Bad Hall
//  AUSTRIA
//  This file is part of the TabShop Android Point of Sale software.
//
package at.smartlab.tshop.kdisplay.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Order {

	private String table;
	
	private Calendar date;
	
	private final List<OrderPosition> orders = new ArrayList<OrderPosition>();
	

	public Order(String table) {
		this.table = table;
		this.date = GregorianCalendar.getInstance();
	}


	public String getTable() {
		return table;
	}


	public Calendar getDate() {
		return date;
	}
	
	public void addPosition(OrderPosition p) {
		orders.add(p);
	}
	
	public List<OrderPosition> getPositions() {
		return orders;
	}
	
}
