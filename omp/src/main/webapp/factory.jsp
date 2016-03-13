<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="user.UserUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="description" content="Order My Pizza Online! Select pizza shop and customize you pizza now!"/>
	<meta name="keywords" content="order my pizza, pizza online, pizza delivery, nearest pizza, pizza"/>
	<link rel="shortcut icon" href="./stylesheets/favicon.ico"/>
	<link type="text/css" rel="stylesheet" href="./stylesheets/main.css"/>
	<link href='https://fonts.googleapis.com/css?family=Raleway|Roboto+Slab|Indie+Flower|Poiret+One|Josefin+Sans|Varela+Round|Maven+Pro|Quicksand|Dancing+Script|Architects+Daughter|News+Cycle|Satisfy|Handlee' rel='stylesheet' type='text/css'/>
	<script type="text/javascript" src="./js/omp.js"></script>
	<script type="text/javascript" src="./js/jquery-1.12.0.min.js"></script>
	<title>Order My Pizza!</title>
</head>
<body id="body">
	<header>
		<div id="navigation">
			<%
				UserService userService = UserServiceFactory.getUserService();
				User user = userService.getCurrentUser();
                String type = request.getParameter("type");
				if (user != null) {
					pageContext.setAttribute("user", user);
					pageContext.setAttribute("user_type", "pizzashop", PageContext.APPLICATION_SCOPE);
			%>
			<ul>
				<li><a href="/pizzashopprofile">${fn:escapeXml(user.nickname)}</a></li>
				<li><a href="<%=userService.createLogoutURL(request.getRequestURI())%>">Sign out</a></li>
				<%
					} else {
				%>
				<li><a href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign in</a></li>
				<%
					}
				%>
			</ul>
		</div>
		<a href="/ordermypizza.jsp"> <img id="logo" width="150" height="100" alt="Order My Pizza" src="./stylesheets/logo.gif"></a>
	</header>

	<div style="height: 100px"></div>
	<%
		if (user == null) {
	%>
	<p>
		You must sign in before proceeding
		<a href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign in</a>
	</p>
	<%
		} else {

    %>
   <br>
 <form action="<%="/pizzafactory/rest/pizzafactory/" + type%>" method="post">
	  Type: <%=type%> <br >
	  Identifier:
      <input type="text" name="identifier" required="required" minlength ="1" size = "40"><br/>
      Description:
      <input type="text" name="description" required="required" minlength ="5" size = "40"><br/>
	  Small Size Cost:
      <input type="number" step="0.01" min="0.00" required="required" name="cost1">
	  Medium Size Cost:
      <input type="number" step="0.01" min="0.00" required="required" name="cost2">
	  Large Size Cost:
      <input type="number" step="0.01" min="0.00" required="required" name="cost3"><br/>
	  Small Size Price:
      <input type="number" step="0.01" min="0.00" required="required" name="price1">
	  Medium Size Price:
      <input type="number" step="0.01" min="0.00" required="required" name="price2">
	  Large Size Price:
      <input type="number" step="0.01" min="0.00" required="required" name="price3"><br/>
      <input type="submit">
    </form>
    
    <%
		}
    %>
  </body>
</html>


   
	