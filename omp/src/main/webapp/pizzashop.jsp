<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
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
	<title>Order My Pizza!</title>
</head>
<body id="body">
	<header>
		<div id="navigation">
			<%
				UserService userService = UserServiceFactory.getUserService();
				User user = userService.getCurrentUser();
				String identifier = request.getParameter("identifier");
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
			if (identifier == null) {
	%>
				<p>
					Please find a Pizza Shop on the map.
					<a href="/ordermypizza.jsp">"Find a Pizza Shop</a>
				</p>	
	<%
			} else {
				
	%>
	  <div style="height: 100px"></div>
	  <form id="form">
	  Select Your Pizza Size:<br />
		<input id="small" type="radio" name="size" value="small" onchange="getSize(this)" checked> Small<br />
		<input id="medium" type="radio" name="size" value="medium" onchange="getSize(this)"> Medium<br />
		<input id="large" type="radio" name="size" value="large" onchange="getSize(this)"> Large<br />
	  Select Your Pizza Crust:
	  <select id=crusts name="crust" onchange="getPrice(this, 'crust')"></select><br/>
	  Select Your Pizza Cheese:
	  <select id=cheeses name="cheese" onchange="getPrice(this, 'cheese')"></select><br/>
	  Select Your Pizza Sauce:
	  <select id=sauces name="sauce" onchange="getPrice(this, 'sauce')"></select><br/><br/>
	  <div id="topping_meat">
			<div id="topping_meat1">
				Select your meat topping: <select id=tm1 name="meat1" onchange="getPrice(this, 'meat')">
				</select> <br />
			</div>
	  </div>
	  <button type="button" value="Add another meat topping" onclick="addTopping('topping_meat');">Add another meat topping</button>
	  <button type="button" value="Remove last meat topping" onclick="removeTopping('topping_meat');">Remove last meat topping</button>
	<br />
		<br />
		<div id="topping_veg">
			<div id="topping_veg1">
				Select your vegetable topping: <select id=tv1 name="veg1" onchange="getPrice(this, 'veg')">
				</select> <br />
			</div>
		</div>
		<button type="button" value="Add another veg topping" onclick="addTopping('topping_veg');">Add another vegetable topping</button>
     	<button type="button" value="Remove last veg topping" onclick="removeTopping('topping_veg');">Remove last vegetable topping</button>
     	<br />
		<input type="hidden" name="pizzashop" value="<%=identifier%>"></input><br/>
      <input type="submit" onclick="mysubmit(); return false;">
    </form>
    <br />
    <div id="selected_size"></div>
    <div id="selected_crust"></div>
    <div id="selected_cheese"></div>
    <div id="selected_sauce"></div>
    <div id="selected_meat1"></div>
    <div id="selected_meat2"></div>
    <div id="selected_meat3"></div>
    <div id="selected_veg1"></div>
    <div id="selected_veg2"></div>
    <div id="selected_veg3"></div>
    <div id="total_price"></div>
	<br />
	<%
			}
		}
	%>
	<div id="botinfo">
		<p>
			Order My Pizza!<br>Geurney 2016
		</p>
	</div>
	<script>
	    var identifier = "<%=identifier%>";
	    var selected_size = 0;
	    var selected_price =[0,0,0,0,0,0,0,0,0];
	    var crusts = [];
	    var cheeses = [];
	    var sauces = [];
	    var meats = [];
	    var vegs = [];
	    if (identifier != "null") {
	    	$(document).ready(loadShop);
	    }

	    function loadShop() {
	    	$.ajax({
	            dataType: "json",
	            url: "/pizzafactory/rest/pizzafactory/findbyidentifier/" + identifier,
				method : 'GET',
	            success: function(data) {
	            	crusts = data['crusts'];
	            	cheeses = data['cheeses'];
	            	sauces =  data['sauces'];
	            	meats = data['meats'];
	            	vegs = data['vegs'];
	            	makeSelect(crusts, 'crusts', 'crust', false);
	            	makeSelect(cheeses, 'cheeses', 'cheese', false);
	            	makeSelect(sauces, 'sauces', 'sauce', false);
	            	makeSelect(meats, 'tm1', 'meat',true);
	            	makeSelect(vegs, 'tv1', 'veg',true);
	            	getSize(document.getElementById('small'));
				 },
	            error: function(jqXHR, textStatus, errorThrown) {
				   	if (jqXHR.status == "404") {
						alert("Please complete your profile");
						fieldset_enable();
					} else {
						 alert(" " + jqXHR.status + " " + textStatus + " " +errorThrown);
					}
	            }
	        });
	    	
	    }
	    function mysubmit() {
			$.ajax({
				data: $('form').serializeArray(),
				url: "/order/rest/enqueue",
				method : 'POST',
				success: function(data) {
					  alert("Successful!");
					  window.location.href = "/ordermypizza.jsp";
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert(" " + jqXHR.status + " " + textStatus + " " +errorThrown);
				}
			});
	    }
		
	    function makeSelect(array, parent, type, hasNone) {
	    	var select = document.getElementById(parent);
			if (hasNone == true) {
	    	  var nopt = document.createElement('option');
	    	  nopt.value='None';
	    	  nopt.innerHTML = 'None';
	    	  select.appendChild(nopt);
			}
	    	for (var i = 0; i < array.length; i++) {
	    		var opt = document.createElement('option');
	    		opt.id = '';
		        opt.value = array[i].identifier; 
				opt.name = type+i;
		        opt.innerHTML = array[i].description; 
		        select.appendChild(opt);
	    	}
	    }
	    function buildPrice(prefix, array, index) {
	    	return prefix + ':    ' +array[index].identifier + ": " + array[index].description + " (" + array[index].prices[selected_size] + ")";	
	    }
	    function getPrice(sel, type) {
	    	switch(type) {
	    	case 'crust':
	    		var div = document.getElementById('selected_crust');
	    		div.innerHTML = buildPrice('Crust', crusts, sel.selectedIndex);
	    		selected_price[0] = crusts[sel.selectedIndex].prices[selected_size];
	    		break;
	    	case 'cheese':
	    		var div = document.getElementById('selected_cheese');
	    		div.innerHTML = buildPrice('Cheese', cheeses, sel.selectedIndex);
	    		selected_price[1] = cheeses[sel.selectedIndex].prices[selected_size];
	    		break;
	    	case 'sauce':
	    		var div = document.getElementById('selected_sauce');
	    		div.innerHTML = buildPrice('Sauce', sauces, sel.selectedIndex);
	    		selected_price[2] = sauces[sel.selectedIndex].prices[selected_size];
	    		break;
	    	case 'meat':
	    		var div;
	    		switch(sel.id) {
	    		case 'tm1' :  
	    			div = document.getElementById('selected_meat1');
	    			if (sel.selectedIndex == 0) {
	    				div.innerHTML='';
		    			selected_price[3] = 0;
	    			} else {
	    				div.innerHTML = buildPrice('Meat1', meats, sel.selectedIndex - 1);
	    				selected_price[3] = meats[sel.selectedIndex - 1].prices[selected_size];
	    			}
	    			break;
	    		case 'tm2' :  
	    			div = document.getElementById('selected_meat2');
	    			if (sel.selectedIndex == 0) {
	    				div.innerHTML='';
		    			selected_price[4] = 0;
	    			} else {
	    				div.innerHTML = buildPrice('Meat2', meats, sel.selectedIndex - 1);
	    				selected_price[4] = meats[sel.selectedIndex - 1].prices[selected_size];
	    			}
	    			break;
	    		case 'tm3' :  
	    			div = document.getElementById('selected_meat3');
	    			if (sel.selectedIndex == 0) {
	    				div.innerHTML='';
		    			selected_price[5] = 0;
	    			} else {
	    				div.innerHTML = buildPrice('Meat3', meats, sel.selectedIndex - 1);
	    				selected_price[5] = meats[sel.selectedIndex - 1].prices[selected_size];
	    			}
	    			break;
	    		}
    			break;
	    	case 'veg':
	    		var div;
	    		switch(sel.id) {
	    		case 'tv1' :  
	    			div = document.getElementById('selected_veg1');
	    			if (sel.selectedIndex == 0) {
	    				div.innerHTML='';
	    				selected_price[6] = 0;
	    			} else {
	    				div.innerHTML = buildPrice('Veg1', vegs, sel.selectedIndex - 1);
	    				selected_price[6] = vegs[sel.selectedIndex - 1].prices[selected_size];
	    			}
	    			break;
	    		case 'tv2' :  
	    			div = document.getElementById('selected_veg2');
	    			if (sel.selectedIndex == 0) {
	    				div.innerHTML='';
	    				selected_price[7] = 0;
	    			} else {
	    				div.innerHTML = buildPrice('Veg2', vegs, sel.selectedIndex - 1);
	    				selected_price[7] = vegs[sel.selectedIndex - 1].prices[selected_size];
	    			}
	    			break;
	    		case 'tv3' :  
	    			div = document.getElementById('selected_veg3');
	    			if (sel.selectedIndex == 0) {
	    				div.innerHTML='';
	    				selected_price[8] = 0;
	    			} else {
	    				div.innerHTML = buildPrice('Veg3', vegs, sel.selectedIndex - 1);
	    				selected_price[8] = vegs[sel.selectedIndex - 1].prices[selected_size];
	    			}
	    			break;
	    		}
    			break;
	    	}
	    	var div = document.getElementById('total_price');
	    	var total = 0;
	    	for (var i = 0; i < 9; i++) {
	    		total += selected_price[i];
	    	}
	    	div.innerHTML = "Total: " + total;
	    }
	    
	    function getSize(sel) {
	    	switch(sel.value) {
	    	case 'small': selected_size = 0;break;
	    	case 'medium': selected_size = 1;break;
	    	case 'large': selected_size = 2;break;
	    	}
	    	getPrice(document.getElementById('crusts'), 'crust');
	    	getPrice(document.getElementById('cheeses'),'cheese');
	    	getPrice(document.getElementById('sauces'), 'sauce');
	    	getPrice(document.getElementById('tm1'), 'meat');
	    	getPrice(document.getElementById('tv1'), 'veg');
	    	if (document.getElementById('tm2') != null) {
	    		getPrice(document.getElementById('tm2'), 'meat');
	    	}
	    	if (document.getElementById('tm3') != null) {
	    		getPrice(document.getElementById('tm3'), 'meat');
	    	}
	    	if (document.getElementById('tv2') != null) {
	    		getPrice(document.getElementById('tv2'), 'veg');
	    	}
	    	if (document.getElementById('tv3') != null) {
	    		getPrice(document.getElementById('tv3'), 'veg');
	    	}
	    }
	    
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
							+ " meat topping:";
					var newSelect = document.createElement('select');
					newSelect.setAttribute('id', 'tm' + counter_meat);
					newSelect.setAttribute('name', 'meat' + counter_meat);
					newSelect.setAttribute('onchange', "getPrice(this, 'meat')");
					newDiv.appendChild(newSelect);
					document.getElementById(divName).appendChild(newDiv);
					makeSelect(meats, 'tm' + counter_meat, 'meat', true);
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
						+ " vegetable topping:";
				    var newSelect = document.createElement('select');
				    newSelect.setAttribute('id', 'tv' + counter_veg);
				    newSelect.setAttribute('name', 'veg' + counter_veg);
				    newSelect.setAttribute('onchange', "getPrice(this, 'veg')");
				    newDiv.appendChild(newSelect);
				    document.getElementById(divName).appendChild(newDiv);
				    makeSelect(vegs, 'tv' + counter_veg, 'veg', true);
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