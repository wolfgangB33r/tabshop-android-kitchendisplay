//  Copyright (C) 2013 Wolfgang Beer. All Rights Reserved.
//  email wolfgang@smartlab.at
//  Schwalbenweg 17
//  4540 Bad Hall
//  AUSTRIA
//  This file is part of the TabShop Android Point of Sale software.
//
package at.smartlab.tshop.kdisplay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.smartlab.tshop.kdisplay.model.Model;
import at.smartlab.tshop.kdisplay.model.Order;
import at.smartlab.tshop.kdisplay.model.OrderPosition;


public class OrderListAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	
	private Model model;
	
	public OrderListAdapter(Context c, Model model) {
		inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		this.model = model;
	}
	
	// Gets the context so it can be used later
	public OrderListAdapter(Context c) {
		inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}
	
	// Total number of things contained within the adapter
	public int getCount() {
		return model.getOrderList().size();
	}

	// Require for structure, not really used in my code.
	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void addOrder(Order o) {
		model.add(0, o);
		notifyDataSetChanged();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		Order order = model.getOrderList().get(position);
		LinearLayout l;
		l = (LinearLayout)inflater.inflate(R.layout.orderlist, null);
		
		if(order != null) {
			TextView date = (TextView)l.findViewById(R.id.date);
			DateFormat formatter = new SimpleDateFormat("dd-MMM HH:mm");
			date.setText(formatter.format( order.getDate().getTime()));
			TextView tableName = (TextView)l.findViewById(R.id.tableName);
			tableName.setText(order.getTable());
			// add all order positions
			LinearLayout ll = (LinearLayout)l.findViewById(R.id.positions);
			for(OrderPosition op : order.getPositions()) {
				TextView t = new TextView(inflater.getContext());
				t.setText(op.getAmount().toPlainString());
				t.setTextSize(18);
				ll.addView(t);
				TextView t2 = new TextView(inflater.getContext());
				t2.setText(op.getProduct());
				t2.setTextSize(18);
				ll.addView(t2);
			}
			
			ImageButton delete = (ImageButton)l.findViewById(R.id.delete);
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					model.remove(position);
					notifyDataSetChanged();
				}
			});
		}
		return l;
	}
}



