package com.intilisic.core.dal;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.intilisic.control.ApplicationConfig;
import com.intilisic.dto.Employee;

public class DatabaseConnectionManager {

	private static SessionFactory sessionFactory;
	private static Configuration configuration;

	private DatabaseConnectionManager() {
	}

	public static void init() {

		configuration = new Configuration();
		Properties settings = new Properties();
		settings.put(Environment.DRIVER,
				ApplicationConfig.getInstance().getProperty(ApplicationConfig.HIBERNATE_DRIVER));
		settings.put(Environment.URL,
				ApplicationConfig.getInstance().getProperty(ApplicationConfig.JDBC_HIBERNATE_URL));
		settings.put(Environment.USER, ApplicationConfig.getInstance().getProperty(ApplicationConfig.JDBC_USER_NAME));
		settings.put(Environment.PASS, ApplicationConfig.getInstance().getProperty(ApplicationConfig.JDBC_PASSWORD));
		settings.put(Environment.DIALECT,
				ApplicationConfig.getInstance().getProperty(ApplicationConfig.HIBERNATE_DIALECT));

		settings.put(Environment.SHOW_SQL, "true");

		settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

		settings.put(Environment.HBM2DDL_AUTO, "create-drop");

		configuration.setProperties(settings);
		configuration.addAnnotatedClass(Employee.class);

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		System.out.println("Hibernate Java Config serviceRegistry created");
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public static void shutdown() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

}
