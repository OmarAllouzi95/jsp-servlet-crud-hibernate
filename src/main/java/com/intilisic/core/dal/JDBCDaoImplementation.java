package com.intilisic.core.dal;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.intilisic.control.ApplicationConfig;
import com.intilisic.dto.Employee;

public class JDBCDaoImplementation implements Dao<Employee> {

	private static final String INSERT_EMPLOYEES_SQL = "INSERT INTO users" + "  (name, email, country) VALUES "
			+ " (?, ?, ?);";

	private static final String SELECT_EMPLOYEE_BY_ID = "select id,name,email,country from users where id =?";
	private static final String SELECT_ALL_EMPLOYEES = "select * from users";
	private static final String DELETE_EMPLOYEES_SQL = "delete from users where id = ?;";
	private static final String UPDATE_EMPLOYEES_SQL = "update users set name = ?,email= ?, country =? where id = ?;";

	protected Connection getConnection() throws IOException {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
					ApplicationConfig.getInstance().getProperty(ApplicationConfig.JDBC_URL),
					ApplicationConfig.getInstance().getProperty(ApplicationConfig.JDBC_USER_NAME),
					ApplicationConfig.getInstance().getProperty(ApplicationConfig.JDBC_PASSWORD));
		} catch (SQLException e) {
			printSQLException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public Optional<Employee> get(long id) throws IOException {

		Employee employee = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID);) {
			preparedStatement.setLong(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				employee = new Employee(id, name, email, country);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		if (employee == null) {
			return Optional.empty();
		}

		return Optional.of(employee);
	}

	@Override
	public List<Employee> getAll() throws IOException {

		List<Employee> employees = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EMPLOYEES);) {
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				employees.add(new Employee(id, name, email, country));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}

		return employees;
	}

	@Override
	public void save(Employee employee) throws IOException {

		System.out.println(INSERT_EMPLOYEES_SQL);
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEES_SQL)) {
			preparedStatement.setString(1, employee.getName());
			preparedStatement.setString(2, employee.getEmail());
			preparedStatement.setString(3, employee.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}

	}

	@Override
	public void update(Employee employee) throws IOException {

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEES_SQL);) {
			statement.setString(1, employee.getName());
			statement.setString(2, employee.getEmail());
			statement.setString(3, employee.getCountry());
			statement.setLong(4, employee.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	@Override
	public void delete(long id) throws IOException {

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEES_SQL);) {
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
