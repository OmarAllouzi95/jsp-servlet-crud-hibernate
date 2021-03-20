package com.intilisic.core.dal;

public class DaoFactory {

	public static final String JDBC = "jdbc";
	public static final String HIBERNATE = "hibernate";

	private DaoFactory() {

	}

	private static Dao dao = null;

	public static Dao getDao(String dbVendor) {
		if (dao == null && dbVendor.equals(JDBC)) {
			dao = new JDBCDaoImplementation();
		} else if (dao == null && dbVendor.equals(HIBERNATE)) {
			dao = new HibernateDaoImplementation();
		}
		return dao;
	}

}
