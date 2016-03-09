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
<%@ page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@ page import="com.google.appengine.api.datastore.Query"%>
<%@ page import="com.google.appengine.api.datastore.EmbeddedEntity"%>
<%@ page import="com.google.appengine.api.datastore.Query.Filter"%>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator"%>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate"%>
<%@ page import="rest.PizzaFactoryResource"%>
<%@ page import="pizzashop.PizzaFactory"%>
<%@ page import="pizza.*"%>
<%@ page import="java.util.List" %>
<%@ page import="user.UserUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description"
	content="Order My Pizza Online! Select pizza shop and customize you pizza now!" />
<meta name="keywords"
	content="order my pizza, pizza online, pizza delivery, nearest pizza, pizza" />
<link rel="shortcut icon" href="./stylesheets/favicon.ico" />
<link type="text/css" rel="stylesheet" href="./stylesheets/main.css" />
<link
	href='https://fonts.googleapis.com/css?family=Raleway|Roboto+Slab|Indie+Flower|Poiret+One|Josefin+Sans|Varela+Round|Maven+Pro|Quicksand|Dancing+Script|Architects+Daughter|News+Cycle|Satisfy|Handlee'
	rel='stylesheet' type='text/css' />
<script type="text/javascript" src="./js/omp.js"></script>
<script type="text/javascript" src="./js/jquery-1.12.0.min.js"></script>
<title>Order My Pizza!</title>
</head>
<body>
	<header>
		<div id="navigation">
			<%
				UserService userService = UserServiceFactory.getUserService();
				User user = userService.getCurrentUser();
				if (user != null) {
					pageContext.setAttribute("user", user);
			%>
			<ul>
				<li><a href="/customerprofile.jsp">${fn:escapeXml(user.nickname)}</a></li>
				<li><a
					href="<%=userService.createLogoutURL(request.getRequestURI())%>">Sign
						out</a></li>
				<%
					} else {
				%>
				<li><a
					href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
						in</a></li>
				<%
					}
				%>
			</ul>
		</div>
		<a href="/ordermypizza.jsp"> <img id="logo" width="150"
			height="100" alt="Order My Pizza" src="./stylesheets/logo.gif"></a>
	</header>

	<div style="height: 100px"></div>
	<%
		if (user == null) {
	%>
	<p>
		You must sign in before proceeding <a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
			in</a>
	</p>
	<%
		} else {
			String pizzaShop = request.getParameter("pizzashop");
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Filter identifierFilter = new FilterPredicate("identifier", FilterOperator.EQUAL,
					pizzaShop);
			Query q = new Query("PizzaFactory").setFilter(identifierFilter);
			PreparedQuery pq = datastore.prepare(q);
			Entity entity = pq.asSingleEntity();
			if (entity == null) {
				response.sendRedirect("/ordermypizza.jsp");
			}
			PizzaFactory pizzaFactory = PizzaFactoryResource.entityToPizzaFactory(entity);
			List<PizzaCrust> crusts = pizzaFactory.getCrusts();
			List<PizzaCheese> cheeses = pizzaFactory.getCheeses();
			List<PizzaSauce> sauces = pizzaFactory.getSauces();
			List<PizzaToppingMeat> meats = pizzaFactory.getMeats();
			List<PizzaToppingVeg> vegs = pizzaFactory.getVegs();
	%>
	<p>Place Order</p>
	<form action="/OrderDatastoreServlet" method="post">
		<input type="radio" name="size" value="small" checked> Small<br />
		<input type="radio" name="size" value="medium"> Medium<br />
		<input type="radio" name="size" value="large"> Large<br />
		Select your crust: <select name="crust">
		<% 
		  for (int i = 0; i < crusts.size(); i++) {
			PizzaCrust crust = crusts.get(i);
			String tag = "crust"+ i;
		%>
			<option value="<%=tag%>"><%=crust.getIdentifier()%></option>
		<%
			}
		%>
		</select> <br /> <br /> 
		Select your cheese: <select name="cheese">
		<% 
		  for (int i = 0; i < cheeses.size(); i++) {
			PizzaCheese cheese = cheeses.get(i);
			String tag = "cheese"+ i;
		%>
			<option value="<%=tag%>"><%=cheese.getIdentifier()%></option>
		<%
			}
		%>
		</select> <br /> <br />
        Select your sauce: <select name="sauce">
		<% 
		  for (int i = 0; i < sauces.size(); i++) {
			PizzaSauce sauce = sauces.get(i);
			String tag = "sauce"+ i;
		%>
			<option value="<%=tag%>"><%=sauce.getIdentifier()%></option>
		<%
			}
		%>
		</select> <br /> <br />
		<div id="topping_meat">
			<div id="topping_meat1">
				Select your meat topping: <select name="meat1">
					<option value="no_meat">None</option>
					<% 
					  for (int i = 0; i < meats.size(); i++) {
						PizzaToppingMeat meat = meats.get(i);
						String tag = "meat"+ i;
					%>
						<option value="<%=tag%>"><%=meat.getIdentifier()%></option>
					<%
					}
					%>
				</select> <br />
			</div>
		</div>
		<br />
		<div id="topping_veg">
			<div id="topping_veg1">
				Select your vegetable topping: <select name="veg1">
					<option value="no_veg">None</option>
					<% 
					  for (int i = 0; i < vegs.size(); i++) {
						PizzaToppingVeg veg = vegs.get(i);
						String tag = "veg"+ i;
					%>
						<option value="<%=tag%>"><%=veg.getIdentifier()%></option>
					<%
					}
					%>
				</select> <br />
			</div>
		</div>
		<input type="submit">
	</form>
	<button value="Add another meat topping"
		onclick="addTopping('topping_meat');">Add another meat
		topping</button>
	<button value="Remove last meat topping"
		onclick="removeTopping('topping_meat');">Remove last meat
		topping</button>
	<br />
	<button value="Add another veg topping"
		onclick="addTopping('topping_veg');">Add another vegetable
		topping</button>
	<button value="Remove last veg topping"
		onclick="removeTopping('topping_veg');">Remove last vegetable
		topping</button>
	<br />
	<%
		}
	%>
	<script>
		var counter_meat = 1;
		var counter_veg = 1;
		var limit = 3;
		function addTopping(divName) {
			if (divName == 'topping_meat') {
				if (counter_meat >= limit) {
					alert("You have reached the limit of adding "
							+ counter_meat + " meat toppings!");
				} else {
					counter_meat++;
					var newDiv = document.createElement('div');
					newDiv.setAttribute("id", divName + counter_meat);
					var num = 'second';
					if (counter_meat == 3) {
						num = 'third';
					}
					newDiv.innerHTML = "Select your "
							+ num
							+ " meat topping:<select name='meat" + counter_meat + "'><option value='no_meat'>None</option><option value='meat_topping1'>Meat1</option><option value='meat_topping2'>Meat2</option><option value='meat_topping3'>Meat3</option></select><br />";
					document.getElementById(divName).appendChild(newDiv);
				}
			} else if (divName == 'topping_veg') {
				if (counter_veg >= limit) {
					alert("You have reached the limit of adding " + counter_veg
							+ " vegetable toppings!");
				} else {
					counter_veg++;
					var newDiv = document.createElement('div');
					newDiv.setAttribute("id", divName + counter_veg);
					var num = 'second';
					if (counter_veg == 3) {
						num = 'third';
					}
					newDiv.innerHTML = "Select your "
							+ num
							+ " vegetable topping:<select name='veg" + counter_veg + "'><option value='no_veg'>None</option><option value='veg_topping1'>Veg1</option><option value='veg_topping2'>Veg2</option><option value='veg_topping3'>Veg3</option></select><br />";
					document.getElementById(divName).appendChild(newDiv);
				}
			}
		}
		function removeTopping(divName) {
			if (divName == 'topping_meat') {
				if (counter_meat == 1) {
					alert("You can select None for meat toppings!");
				} else {
					var newDiv = document
							.getElementById(divName + counter_meat);
					newDiv.parentNode.removeChild(newDiv);
					counter_meat--;
				}
			} else if (divName == 'topping_veg') {
				if (counter_veg == 1) {
					alert("You can select None for vegetable toppings!");
				} else {
					var newDiv = document.getElementById(divName + counter_veg);
					newDiv.parentNode.removeChild(newDiv);
					counter_veg--;
				}
			}
		}
	</script>

</body>

</html>