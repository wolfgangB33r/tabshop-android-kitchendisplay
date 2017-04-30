//  Copyright (C) 2013 Wolfgang Beer. All Rights Reserved.
//  email wolfgang@smartlab.at
//  Schwalbenweg 17
//  4540 Bad Hall
//  AUSTRIA
//  This file is part of the TabShop Android Point of Sale software.
//
package at.smartlab.tshop.kdisplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * CrashLog logs all Exceptions into a local file that could be sent by the user.
 * @author wolfgang
 *
 */
public class CrashLog {

	private final static CrashLog instance = new CrashLog();
	
	private final static String FILENAME = "log.txt";
	
	private static String path;
	
	public static CrashLog getInstance() { 
    	return instance; 
    }
	
	protected CrashLog() {
	}
	
	public void setPath(String path) {
		CrashLog.path = path;
	}
	
	/**
	 * Append the stack trace of a given Exception into the crash log.
	 * @param e
	 */
	public void logException(Exception e) {
		synchronized (CrashLog.this) {
			if(path != null) {
				File file;
				PrintWriter pw = null;
				try {
					file = new File(path, FILENAME);
					FileOutputStream fos = new FileOutputStream(file, true);
					pw = new PrintWriter(fos);
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS\n", Locale.GERMAN).format(GregorianCalendar.getInstance().getTime());
					pw.write(date);
					e.printStackTrace(pw);
					pw.write("\n");
				} catch (Exception ex) {
					e.printStackTrace();
				}
				finally {
					try {
						pw.close();
					} catch (Exception ex) {
						
					}
				}
			}
		}
	}
	
	/**
	 * Append a string into the crash log.
	 * @param str
	 */
	public void logString(String str) {
		synchronized (CrashLog.this) {
			if(path != null) {
				File file;
				PrintWriter pw = null;
				try {
					file = new File(path, FILENAME);
					FileOutputStream fos = new FileOutputStream(file, true);
					pw = new PrintWriter(fos);
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS\n", Locale.GERMAN).format(GregorianCalendar.getInstance().getTime());
					pw.write(date);
					pw.write(str);
					pw.write("\n");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				finally {
					try {
						pw.close();
					} catch (Exception ex) {
						
					}
				}
			}
		}
	}
	
}
