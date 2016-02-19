<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="com.google.appengine.api.datastore.EntityNotFoundException"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="ordermypizza.UserIDObscure"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="description" content="Order My Pizza Online! Select pizza shop and customize you pizza now!"/>
	<meta name="keywords" content="order my pizza, pizza online, pizza delivery, nearest pizza, pizza"/>
	<link rel="shortcut icon" href="./stylesheets/favicon.ico"/>
	<link type="text/css" rel="stylesheet" href="./stylesheets/main.css"/>
	<link href='https://fonts.googleapis.com/css?family=Raleway|Roboto+Slab|Indie+Flower|Poiret+One|Josefin+Sans|Varela+Round|Maven+Pro|Quicksand|Dancing+Script|Architects+Daughter|News+Cycle|Satisfy|Handlee' rel='stylesheet' type='text/css'/>
	<script type="text/javascript" src="./stylesheets/omp.js"></script>
	<script type="text/javascript" src="./stylesheets/jquery-1.12.0.min.js"></script>
	<title>Order My Pizza!</title>
</head>
<body id="body">
	<header>
		<div id="navigation">
			<%
				UserService userService = UserServiceFactory.getUserService();
				User user = userService.getCurrentUser();
				if (user != null) {
					pageContext.setAttribute("user", user);
					pageContext.setAttribute("user_type", "customer", PageContext.APPLICATION_SCOPE);
			%>
			<ul>
				<li>${fn:escapeXml(user.nickname)}</li>
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
			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Key key = KeyFactory.createKey("Customer", hash_uid);
			Entity customer = null;
			String name = null;
			String address = null;
			String phone = null;
			try {
				customer = datastore.get(key);
				name = (String) customer.getProperty("name");
				address = (String) customer.getProperty("address");
				phone = (String) customer.getProperty("phone");
	%>
	<p>My Profile</p>
	<%
			} catch (EntityNotFoundException e) {
	%>
	<p>Please complete your information below</p>
	<%
			} finally {
	%>
	<form action="/customerprofile" method="post">
		<fieldset id="profile_fieldset" <%if (!(name == null || phone == null || address == null)) {%> disabled <%}%>>
			Name: <input type="text" name="name" <%if (name != null) {%> value="<%=name%>" <%}%>><br>
			Address: <input type="text" name="address" <%if (address != null) {%> value="<%=address%>" <%}%>><br>
			Phone: <input type="text" name="phone" <%if (phone != null) {%> value="<%=phone%>" <%}%>><br>
			<div id="submit_form" <%if (!(name == null || phone == null || address == null)) {%> style="display: none" <%}%>>
				<input type="submit" name="submit">
			</div>
		</fieldset>
	</form>
	<button onclick="fieldset_enable()">Edit</button>
	<button onclick="delete_req('/customerprofile', '/ordermypizza.jsp')">Delete</button>
	<%
			}
		}
	%>

	<div id="botinfo">
		<p>
			Order My Pizza!<br>Geurney 2016
		</p>
	</div>
		
</body>
</html>