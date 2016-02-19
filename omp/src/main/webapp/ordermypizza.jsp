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
	<script type="text/javascript" src="./stylesheets/omp.js"></script>
	<script type="text/javascript" src="./stylesheets/jquery-1.12.0.min.js"></script>
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
			<form autocomplete="off">
				<div>
					<div class="data" style="float: left; margin: 10px 0 10px 0;">
						<input type="city" autocomplete="off" placeholder="City" style="width: 215px;">
					</div>

					<div class="data" style="float: left; margin: 10px 0 10px 10px;">
						<input id="statenames" placeholder="State" style="width: 54px;" autocomplete="off">
					</div>

					<div style="font-size: 16px; display: inline; float: left; margin-left: 10px; margin-top: 20px;">or</div>

					<div class="data" style="float: left; margin: 10px 0 10px 10px;">
						<input type="zip" autocomplete="off" style="width: 120px; margin-right: 0px;" placeholder="Zip" maxlength="5" data-placeholder="Zip">
					</div>
					<input type="submit" id="submit" value="Search">
				</div>
			</form>

			<hr>
			<div id="store-result"  style="float: left; width: 469px; margin-top: 92px">
				<p>
					<span>Find a Pizza Shop near you!</span><br> Enter your address above to find your local store!
				</p>
			</div>
			<div id="map"></div>
		</div>
	</div>

	<div id="botinfo">
		<p>
			Order My Pizza!<br>Geurney 2016
		</p>
	</div>

</body>
</html>