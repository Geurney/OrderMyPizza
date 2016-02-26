<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
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
	<script type="text/javascript" src="./js/pizzashopmap.js"></script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBB9p91mM16Xr-mvMQSwNBfkDZDD6SuJws&signed_in=true&libraries=places&callback=initMap"
		async defer></script>
	<title>Order My Pizza!</title>
</head>
<body id="body">
	<header>
		<div id="navigation">
			<ul>
				<%
					UserService userService = UserServiceFactory.getUserService();
					User user = userService.getCurrentUser();
					if (user != null) {
						pageContext.setAttribute("user", user);
				%>
				<li><a href="<%="/" + pageContext.getAttribute("user_type", PageContext.APPLICATION_SCOPE) + "profile.jsp"%>">${fn:escapeXml(user.nickname)}</a></li>
				<li><a href="<%=userService.createLogoutURL("/ordermypizza.jsp")%>">Sign out</a></li>
				<%
					} else {
				%>
				<li><a href="javascript:%20div_display('signin_select')">Sign in</a>
					<div id="signin_select" style="display: none">
						<a href="<%=userService.createLoginURL("/customerprofile.jsp")%>">Sign in as Customer</a><br> 
						<a href="<%=userService.createLoginURL("/pizzashopprofile.jsp")%>">Sign in as PizzaShop</a>
					</div></li>
				<%
					}
				%>
			</ul>
		</div>
		<a href="/ordermypizza.jsp"> <img id="logo" width="150" height="100" alt="Order My Pizza" src="./stylesheets/logo.gif">
		</a>
	</header>

	<div style="height: 100px"></div>

	<div id="store-locator">
		<h1>Find A Pizza Shop</h1>
		<div id="store-locator-body">
			<input id="citylocation" placeholder="Enter a city" type="text" />
			<input type="submit" id="submit" value="Search">
			<hr>
			<div id="resultlist">
				<div id="noresult">
					Find a Pizza Shop near you!<br> Enter your address above to find your local store!
				</div>
				<div id="resultsTableWrapper">
	  			<table>
	    			<tbody id="results"></tbody>
	  			</table>
	  			</div>
			</div>
			<div id="map"></div>
			<div style="display: none">
		  	<div id="info-content">
				<table>
			    	<tr id="iw-url-row" class="iw_table_row">
			        	<td id="iw-icon" class="iw_table_icon"></td>
		    	    	<td id="iw-url"></td>
		    		</tr>
		      		<tr id="iw-address-row" class="iw_table_row">
				        <td class="iw_attribute_name">Address:</td>
				        <td id="iw-address"></td>
				    </tr>
		    		<tr id="iw-phone-row" class="iw_table_row">
		        		<td class="iw_attribute_name">Telephone:</td>
		        		<td id="iw-phone"></td>
		      		</tr>
		      		<tr id="iw-rating-row" class="iw_table_row">
			        	<td class="iw_attribute_name">Rating:</td>
			        	<td id="iw-rating"></td>
			      	</tr>
		  	 	</table>
		  	</div>
			</div>
		</div>
	</div>

	<div id="botinfo">
		<p>
			Order My Pizza!<br>Geurney 2016
		</p>
	</div>

</body>
</html>