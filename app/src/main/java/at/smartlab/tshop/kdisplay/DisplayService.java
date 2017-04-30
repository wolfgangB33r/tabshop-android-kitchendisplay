//  Copyright (C) 2013 Wolfgang Beer. All Rights Reserved.
//  email wolfgang@smartlab.at
//  Schwalbenweg 17
//  4540 Bad Hall
//  AUSTRIA
//  This file is part of the TabShop Android Point of Sale software.
//
package at.smartlab.tshop.kdisplay;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class DisplayService {

	private ServerSocket s;
	
	private Thread t;
	
	private boolean running;
	
	private ClientMessageHandler msgHandler;
	
	public DisplayService(ServerSocket s) {
		this.s = s;
		this.running = false;
	}
	
	public void setClientMessageHandler(ClientMessageHandler h) {
		msgHandler = h;
	}
	
	public void start() {
		running = true;
		t = new Thread() {
			@Override
			public void run() {
				while(running) {
					// accepting clients
					try {
						handleClientMsg(s.accept());
					} catch (IOException e) {
						CrashLog.getInstance().logException(e);
						running = false;
					}
				}
			}
		};
		t.start();
	}
	
	public void stop() {
		running = false;
		try {
			t.join();
		} catch (InterruptedException e) {
			CrashLog.getInstance().logException(e);
		}
	}
	
	public void handleClientMsg(final Socket cSock) {
		Log.d(Settings.TAG, "Accepting Client");
		t = new Thread() {
			@Override
			public void run() {
				// accepting clients
				try {
					BufferedInputStream in = new BufferedInputStream(cSock.getInputStream());
					StringBuffer msg = new StringBuffer();
					int b = in.read();
					while(b != -1) {
						msg.append((char)b);
						b = in.read();
					}
					Log.d(Settings.TAG, msg.toString());
					if(msgHandler != null) {
						msgHandler.handle(msg.toString());
					}
				} catch (IOException e) {
					CrashLog.getInstance().logException(e);
				}
			}
		};
		t.start();
	}
	
}
