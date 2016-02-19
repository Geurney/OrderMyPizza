<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.EntityNotFoundException" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="ordermypizza.UserIDObscure" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="description" content="Order My Pizza Online! Select pizza shop and customize you pizza now!">
    <meta name="keywords" content="order my pizza, pizza online, pizza delivery, nearest pizza, pizza">
    <link rel="shortcut icon" href="./stylesheets/favicon.ico">
	
	<link type="text/css" rel="stylesheet" href="./stylesheets/main.css"/>
	<link href='https://fonts.googleapis.com/css?family=Raleway|Roboto+Slab|Indie+Flower|Poiret+One|Josefin+Sans|Varela+Round|Maven+Pro|Quicksand|Dancing+Script|Architects+Daughter|News+Cycle|Satisfy|Handlee' rel='stylesheet' type='text/css'>
	<script src="./stylesheets/jquery-1.12.0.min.js"></script>
	<title>Order My Pizza!</title>
</head>
<body id="body">
        <header>
            <div id="navigation">
                <ul>
                    <li>
                    <%
    					UserService userService = UserServiceFactory.getUserService();
   						User user = userService.getCurrentUser();
    					if (user != null) {
        					pageContext.setAttribute("user", user);
						%>
						${fn:escapeXml(user.nickname)}</li>
						<li><a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out</a></p>
					<%
					} else {
					%>
					<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
					<%
					    }
					%>
                </ul>  
            </div>
            <a href="/ordermypizza.jsp">
				<img id="logo" width="150" height="100" alt="Order My Pizza" src="./stylesheets/logo.gif">
			</a>
        </header>

        <div style="height:100px"></div>
         	<%
        	if (user == null) {
			%>
				<p>You must sign in before proceeding <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a></p>
			<%
			} else {
		        String hash_uid = UserIDObscure.obsecure(user.getUserId());
				DatastoreService datastore = DatastoreServiceFactory
						.getDatastoreService();
				Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
				Entity pizzaFactory = null;
				try {
					pizzaFactory = datastore.get(key);
					List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
							.getProperty("crust");
					List<EmbeddedEntity> cheeses = (List<EmbeddedEntity>) pizzaFactory
							.getProperty("cheese");
					List<EmbeddedEntity> sauces = (List<EmbeddedEntity>) pizzaFactory
							.getProperty("sauce");
					List<EmbeddedEntity> topping_meat = (List<EmbeddedEntity>) pizzaFactory
							.getProperty("topping_meat");
					List<EmbeddedEntity> topping_veg = (List<EmbeddedEntity>) pizzaFactory
							.getProperty("topping_veg");
				} catch (EntityNotFoundException e) {
					
				}
			}
      		%>
      		
    <form action="/pizzafactoryprofile" method="post">
      <input type="text" name="type">
      <input type="text" name="description">
      <input type="text" name="cost1">
      <input type="text" name="cost2">
      <input type="text" name="cost3">
      <input type="text" name="price1">
      <input type="text" name="price2">
      <input type="text" name="price3">
      <input type="submit">
    </form>
  </body>
</html>