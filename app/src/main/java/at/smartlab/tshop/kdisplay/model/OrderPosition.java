//  Copyright (C) 2013 Wolfgang Beer. All Rights Reserved.
//  email wolfgang@smartlab.at
//  Schwalbenweg 17
//  4540 Bad Hall
//  AUSTRIA
//  This file is part of the TabShop Android Point of Sale software.
//
package at.smartlab.tshop.kdisplay.model;

import java.math.BigDecimal;

public class OrderPosition {
	
	private String product;
	private BigDecimal amount;

	public OrderPosition(String product, BigDecimal amount) {
		this.product = product;
		this.amount = amount;
	}

	public String getProduct() {
		return product;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}