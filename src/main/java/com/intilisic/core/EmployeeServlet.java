package com.intilisic.core;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.intilisic.core.dal.DaoFactory;
import com.intilisic.core.dal.HibernateDaoImplementation;
import com.intilisic.core.dal.JDBCDaoImplementation;
import com.intilisic.core.exception.EmployeeNotFoundException;
import com.intilisic.dto.Employee;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/")
public class EmployeeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertUser(request, response);
				break;
			case "/delete":
				deleteUser(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateUser(request, response);
				break;
			case "/search":
				showSearchPage(request, response);
				break;
			case "/find":
				findEmployee(request, response);
				break;
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException | EmployeeNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Employee> listUser = DaoFactory.getDao(DaoFactory.HIBERNATE).getAll();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}

	@SuppressWarnings("unchecked")
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException, EmployeeNotFoundException {
		long id = Long.parseLong(request.getParameter("id"));
		Optional<Employee> employee = DaoFactory.getDao(DaoFactory.HIBERNATE).get(id);
		if (employee.isPresent()) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
			request.setAttribute("user", employee.get());
			dispatcher.forward(request, response);
		} else {
			throw new EmployeeNotFoundException(String.format("No Record With Id : %s Found", id));
		}
	}

	@SuppressWarnings("unchecked")
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		Employee newEmployee = new Employee(name, email, country);
		DaoFactory.getDao(DaoFactory.HIBERNATE).save(newEmployee);
		response.sendRedirect("list");
	}

	@SuppressWarnings("unchecked")
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		Employee employee = new Employee(id, name, email, country);
		DaoFactory.getDao(DaoFactory.HIBERNATE).update(employee);
		response.sendRedirect("list");
	}

	private void showSearchPage(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
		dispatcher.forward(request, response);
	}

	private void findEmployee(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException, EmployeeNotFoundException {

		long id = Long.parseLong(request.getParameter("eId"));
		populateEmployeesList(request, response, id, "view.jsp");
	}

	@SuppressWarnings("unchecked")
	private void populateEmployeesList(HttpServletRequest request, HttpServletResponse response, long id,
			String viewPage) throws IOException, ServletException, EmployeeNotFoundException {
		Optional<Employee> employee = DaoFactory.getDao(DaoFactory.HIBERNATE).get(id);
		if (employee.isPresent()) {
			List<Employee> result = new ArrayList<>();
			result.add(employee.get());
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			request.setAttribute("result", result);
			dispatcher.forward(request, response);
		} else {
			throw new EmployeeNotFoundException(String.format("No Record With Id : %s Found", id));
		}
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		DaoFactory.getDao(DaoFactory.HIBERNATE).delete(id);
		response.sendRedirect("list");

	}

}
