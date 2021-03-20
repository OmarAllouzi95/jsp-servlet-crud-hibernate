package com.intilisic.control;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ApplicationConfig {

	public static final String JDBC_URL = "jdbcURL";
	public static final String JDBC_USER_NAME = "jdbcUsername";
	public static final String JDBC_PASSWORD = "jdbcPassword";
	public static final String JDBC_HIBERNATE_URL = "hiberJdbcURL";
	public static final String HIBERNATE_DRIVER = "hibernateDriver";
	public static final String HIBERNATE_DIALECT = "hibernateDialect";

	private static ApplicationConfig applicationConfig;

	private Properties properties;

	private ApplicationConfig() {
	}

	public static ApplicationConfig getInstance() {

		if (applicationConfig == null) {
			applicationConfig = new ApplicationConfig();
		}
		return applicationConfig;
	}

	public void init() {

		try {
			loadProperties();
		} catch (Exception e) {
			System.err.println("Failed to initialize application configurations" + e);
			throw new RuntimeException(e);
		}

	}

	private void loadProperties() throws Exception {

		properties = new Properties();
		properties.load(new FileInputStream(
				new File(new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()), "")
						.getAbsolutePath() + File.separator + "application.properties"));
	}

	public String getProperty(String propertyKey) {
		return properties.getProperty(propertyKey);
	}
}
