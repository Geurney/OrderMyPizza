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
<%@ page import="user.UserUtils"%>
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
	<script type="text/javascript" src="./js/omp.js"></script>
	<script type="text/javascript" src="./js/jquery-1.12.0.min.js"></script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCajB_AS8cdfBcCDP2qIhpKHP8FZiiq1e0"
		async defer></script>
	<script src="http://www.google.com/jsapi"></script>
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
	%>
	<p>My Profile</p>
	<form id="form">
		<fieldset id="profile_fieldset" disabled>
			Name: <input type="text" id="name" name="name" required><br>
			Phone: <input type="text" id="phone" name="phone" required><br>
			City: <input type="text" id="city" name="city" required><br>
			<div id="submit_form" style="display: none">
				<button type="button" onclick="findmylocation()">Locate</button>
				<button type="submit" name="submit" onclick="mysubmit();return false;">Submit</button>
			</div>
		</fieldset>
		<input type="hidden" id="latitude" name="latitude"><br>
		<input type="hidden" id="longitude" name="longitude"><br>
	</form>
	<button onclick="fieldset_enable()">Edit</button>
	<button onclick="delete_req('/customer/rest/customer', '/ordermypizza.jsp')">Delete</button>
	<div style="height: 100px"></div>
   <div id="orderList">My Orders:</div>
	<%
		}
	%>

	<div id="botinfo">
		<p>
			Order My Pizza!<br>Geurney 2016
		</p>
	</div>
	<script>
	var user = '<%=user%>';
	if (user != "null") {
	  $(document).ready(loadUser);
	  $(document).ready(loadOrder);
	}
	function loadUser() {
		$.ajax({
            dataType: "json",
            url: "/customer/rest/customer",
			method : 'GET',
            success: function(data) {
				document.getElementById('name').value = data.name;
				document.getElementById('phone').value = data.phone;
				document.getElementById('city').value = data.city;
				document.getElementById('latitude').value = data.latitude;
				document.getElementById('longitude').value = data.longitude;
			 },
			error: function(jqXHR, textStatus, errorThrown) {
				if (jqXHR.status == "404") {
					alert("Please complete your profile");
					fieldset_enable();
				}
			}
          });
	}
	function loadOrder() {
		$.ajax({
            dataType: "json",
            url: "/customer/rest/customer/order",
			method : 'GET',
            success: function(data) {
		        function makeUL(array) {
			    	var list = document.createElement('ul');
			   		 for(var i = 0; i < array.length; i++) {
						var item = document.createElement('li');
						var p = document.createElement('p');
						p.innerHTML = "<p>Num.: " + array[i].number +"</p>" + 
						"<p>Date: " + array[i].date +"</p>" +
						"<p>Crust: " + array[i].crust +"</p>" + 
						"<p>Cheese: " + array[i].cheese +"</p>" + 
						"<p>Sauce: " + array[i].sauce +"</p>" + 
						"<p>Meat: " + array[i].meats +"</p>" + 
						"<p>Veg: " + array[i].vegs +"</p>" + 
						"<p>Price: " + array[i].price +"</p>" +
						"<p>Status: " + array[i].status +"</p>";
						item.appendChild(p);
						list.appendChild(item);
					}
					return list;
		        }
		        document.getElementById('orderList').appendChild(makeUL(data));
				},
            error: function(jqXHR, textStatus, errorThrown) {
            }
        });
	}
	function findmylocation() {
		if(google.loader.ClientLocation) {
			document.getElementById('city').value = google.loader.ClientLocation.address.city;
			document.getElementById('latitude').value = google.loader.ClientLocation.latitude;
			document.getElementById('longitude').value = google.loader.ClientLocation.longitude;
	    } else {
			alert('Location not available');
		}
	}
	function mysubmit() {
		$.ajax({
            data: $('form').serializeArray(),
            url: "/customer/rest/customer",
			method : 'POST',
            success: function(data) {
		          alert("Successful!");
				  window.location.href = "/customerprofile.jsp";
			},
            error: function(jqXHR, textStatus, errorThrown) {
                alert(" " + jqXHR.status + " " + textStatus + " " +errorThrown);
            }
          });
	}
	</script>
</body>
</html>