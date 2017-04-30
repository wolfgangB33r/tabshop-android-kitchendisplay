//  Copyright (C) 2013 Wolfgang Beer. All Rights Reserved.
//  email wolfgang@smartlab.at
//  Schwalbenweg 17
//  4540 Bad Hall
//  AUSTRIA
//  This file is part of the TabShop Android Point of Sale software.
//
package at.smartlab.tshop.kdisplay.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import at.smartlab.tshop.kdisplay.ClientMessageHandler;
import at.smartlab.tshop.kdisplay.DisplayService;
import at.smartlab.tshop.kdisplay.Settings;

public class Model {

	private final static Model instance = new Model();

	public static Model getInstance() {
		return instance;
	}

	private final List<Order> orders;

	private boolean open;
	
	private String serviceName;

	private DisplayService dService;

	private ServerSocket mServerSocket;

	private int mLocalPort;

	private NsdManager.RegistrationListener mRegistrationListener;

	private Model() {
		mLocalPort = 8090;
		orders = new ArrayList<Order>();
		open = false;
		serviceName = "TabShopKitchenDisplay";

		mRegistrationListener = new NsdManager.RegistrationListener() {
			@Override
			public void onServiceRegistered(NsdServiceInfo info) {
				Log.d(Settings.TAG,
						"TabShopKitchenDisplay service registered : "
								+ info);
				// save the real system generated service name
				// Save the service name. Android may have changed it in order
				// to
				// resolve a conflict, so update the name you initially
				// requested
				// with the name Android actually used.
				Model.getInstance().setServiceName(info.getServiceName());
			}

			@Override
			public void onRegistrationFailed(NsdServiceInfo serviceInfo,
					int errorCode) {
				Log.e(Settings.TAG,
						"TabShopKitchenDisplay service register failed : ");
			}

			@Override
			public void onServiceUnregistered(NsdServiceInfo arg0) {
				Log.d(Settings.TAG,
						"TabShopKitchenDisplay service unregistered : " + arg0);
				// Service has been unregistered. This only happens when you
				// call
				// NsdManager.unregisterService() and pass in this listener.
			}

			@Override
			public void onUnregistrationFailed(NsdServiceInfo serviceInfo,
					int errorCode) {
				Log.e(Settings.TAG,
						"TabShopKitchenDisplay service unregister failed : ");
			}
		};
	}

	public void openServerSocket(ClientMessageHandler h) {
		if (!open) {
			try {
				// Initialize a server socket on the next available port.
				mServerSocket = new ServerSocket(mLocalPort);

				dService = new DisplayService(mServerSocket);
				dService.start();
				open = true;
			} catch (IOException e) {
				e.printStackTrace();
				open = false;
			}
		}
		if (dService != null) {
			dService.setClientMessageHandler(h);
		}
	}

	public void closeServerSocket() {
		try {
			mServerSocket.close();
			dService.stop();
			open = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		mServerSocket = null;
	}

	
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<Order> getOrderList() {
		return orders;
	}

	public NsdManager.RegistrationListener getmRegistrationListener() {
		return mRegistrationListener;
	}

	public void add(int index, Order o) {
		orders.add(index, o);
	}

	public void remove(int index) {
		orders.remove(index);
	}

	public int getPort() {
		return mLocalPort;
	}

	public String getAddress() {
		if (mServerSocket != null) {
			return mServerSocket.getInetAddress().getHostAddress();
		}
		return "";
	}
}
