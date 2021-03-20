package com.intilisic.control.listener;

import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.intilisic.control.ApplicationConfig;
import com.intilisic.core.dal.DatabaseConnectionManager;

@WebListener
public class ApplicationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		System.out.println("Starting application");
		ApplicationConfig.getInstance().init();
		DatabaseConnectionManager.init();
		System.out.println("Application started successfully");
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {

		try {
			DatabaseConnectionManager.shutdown();
		} catch (Throwable t) {
		}
		// This manually deregisters JDBC driver, which prevents Tomcat 7 from
		// complaining about memory leaks
		Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			java.sql.Driver driver = drivers.nextElement();
			try {
				java.sql.DriverManager.deregisterDriver(driver);
			} catch (Throwable t) {
			}
		}
		try {
			Thread.sleep(2000L);
		} catch (Exception e) {
		}
	}

}