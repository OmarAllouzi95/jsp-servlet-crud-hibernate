<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <html>
        
                <head>
            <title>User Management Application</title>
        </head>

        <body>


<form method="post" action="<%=request.getContextPath()%>/find">
      <table border="0" width="300" align="center" bgcolor="#995D5E">
        <tr><td colspan=2 style="font-size:12pt;" align="center">
        <h3>Search Details</h3></td></tr>
        <tr><td ><b>Employee ID:</b></td>
          <td>: <input  type="text" name="eId" id="eId">
        </td></tr>      
        <tr><td colspan=2 align="center">
        <input  type="submit" value="Search" ></td></tr>
      </table>
    </form>
    
    <button type="button" name="back" onclick="history.back()">back</button>
            <a href="<%=request.getContextPath()%>/list">Home Page</a>
    
            </body>

        </html>
