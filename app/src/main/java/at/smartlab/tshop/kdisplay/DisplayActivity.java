//  Copyright (C) 2013 Wolfgang Beer. All Rights Reserved.
//  email wolfgang@smartlab.at
//  Schwalbenweg 17
//  4540 Bad Hall
//  AUSTRIA
//  This file is part of the TabShop Android Point of Sale software.
//
package at.smartlab.tshop.kdisplay;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.smartlab.tshop.kdisplay.model.Model;
import at.smartlab.tshop.kdisplay.model.Order;
import at.smartlab.tshop.kdisplay.model.OrderPosition;


public class DisplayActivity extends ListActivity {

	private NsdManager mNsdManager;
	
	private AsyncTask<Void, Void, Void> task;

	private String[] addresses;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		OrderListAdapter adapter = new OrderListAdapter(this, Model.getInstance());
		this.getListView().setAdapter(adapter);
		
		Model.getInstance().openServerSocket(new ClientMessageHandler() {
			
			@Override
			public void handle(String msg) {
				handleClientMsg(msg);
			}
		});
		
		// keep window on forever
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			
		
		task = new AsyncTask<Void, Void, Void>() {
			
			protected Void doInBackground(Void... params) {
				
				return null;
			}
		};
		task.execute();
		
		addresses = getLocalIpAddress();
	}
	
	public String[] getLocalIpAddress() {          
	    ArrayList<String> addresses = new ArrayList<String>();
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	        	NetworkInterface intf = en.nextElement();
	        	if(!intf.isPointToPoint() && intf.isUp()) {
		            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
		                InetAddress inetAddress = enumIpAddr.nextElement();
		                if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
		                    addresses.add(inetAddress.getHostAddress());
		                }
		            }
	        	}
	         }
	     } catch (SocketException ex) {
	         CrashLog.getInstance().logException(ex);
	     }
	     return addresses.toArray(new String[0]);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void handleClientMsg(final String msg) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				try {
					JSONObject po = new JSONObject(msg);
					Order o = new Order(po.getString("table"));
					JSONArray arr = po.getJSONArray("positions");
					for(int i = 0; i < arr.length(); i++) {
						JSONObject pos = arr.getJSONObject(i);
						o.addPosition(new OrderPosition(pos.getString("product"), new BigDecimal(pos.getString("amount"))));
					}
					((OrderListAdapter)getListView().getAdapter()).addOrder(o);
				} catch (JSONException e) {
					CrashLog.getInstance().logException(e);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			try {
				// Use the Builder class for convenient dialog construction
		        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setMessage("KitchenDisplay Address")
		               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       
		                   }
		               });
		        // Create the AlertDialog object and return it
		        LinearLayout ll = new LinearLayout(this);
		        ll.setOrientation(LinearLayout.VERTICAL);
		      
		        for(String adr : addresses) {
		        	TextView tv = new TextView(this);
		        	tv.setText(adr);
		        	tv.setGravity(Gravity.CENTER_HORIZONTAL);
		        	tv.setTextSize(20);
		        	ll.addView(tv);
		        }
		        
		        builder.setView(ll);
		        builder.create().show();
			} catch (Exception e) {
				CrashLog.getInstance().logException(e);
			}
		}
		return true;
	}
}
