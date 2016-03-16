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
		}
    %>
    <div style="height: 100px"></div>
	<br>
    <div id="factoryList"></div>
    <br>
    <a href="/factory.jsp?type=crust">Add a Pizza Crust</a><br>
    <a href="/factory.jsp?type=cheese">Add a Pizza Cheese</a><br>
    <a href="/factory.jsp?type=sauce">Add a Pizza Sauce</a><br>
    <a href="/factory.jsp?type=meat">Add a Pizza Meat Topping</a><br>
    <a href="/factory.jsp?type=veg">Add a Pizza Vegetable Topping</a>	<br>
  </body>
  <script>
   var user = '<%=user%>';
	if (user != "null") {
	  $(document).ready(loadFactory);
	}
  function loadFactory() {
	  $.ajax({
          dataType: "json",
          url: "/pizzafactory/rest/pizzafactory/",
		  method : 'GET',
          success: function(data) {
			 var div = document.getElementById('factoryList');
		      function makeUL(array) {
			  var list = document.createElement('ul');
			  for(var i = 0; i < array.length; i++) {
				var item = document.createElement('li');
				var s = array[i].identifier +": " +array[i].description + " " + array[i].costs + " " + array[i].prices;
				item.appendChild(document.createTextNode(s));
				list.appendChild(item);
			}
			return list;
		}
		document.getElementById('factoryList').appendChild(document.createTextNode("Pizza Crusts:"));
	    document.getElementById('factoryList').appendChild(makeUL(data['crusts']));
		
		document.getElementById('factoryList').appendChild(document.createTextNode("Pizza Cheeses: "));
	    document.getElementById('factoryList').appendChild(makeUL(data['cheeses']));
		
		document.getElementById('factoryList').appendChild(document.createTextNode("Pizza Sauces: "));
	    document.getElementById('factoryList').appendChild(makeUL(data['sauces']));
		
		document.getElementById('factoryList').appendChild(document.createTextNode("Pizza Topping Meats: "));
	    document.getElementById('factoryList').appendChild(makeUL(data['meats']));
		
		document.getElementById('factoryList').appendChild(document.createTextNode("Pizza Topping Vegetables: "));
	    document.getElementById('factoryList').appendChild(makeUL(data['vegs']));
			 
          },
          error: function(jqXHR, textStatus, errorThrown) {
			   	if (jqXHR.status == "404") {
					alert("Please complete your profile");
				} else {
					 alert(" " + jqXHR.status + " " + textStatus + " " +errorThrown);
				}
          }
      });
  }
  </script>
</html>