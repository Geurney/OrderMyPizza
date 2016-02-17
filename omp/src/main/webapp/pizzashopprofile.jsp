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
				Key key = KeyFactory.createKey("PizzaShop", hash_uid);
				Entity pizzaShop = null;
				String name = null;
				String address = null;
				String phone = null;
				try {
					pizzaShop = datastore.get(key);
					name = (String)pizzaShop.getProperty("name");
					address = (String)pizzaShop.getProperty("address");
					phone = (String)pizzaShop.getProperty("phone");
					%>
					 <p>My Profile</p>
					 <%
				} catch (EntityNotFoundException e) {
					%>
					<p>Please complete your information below</p>
					<%
				} finally {
				%>
			    <form action="/pizzashopprofile" method="post">
			    <fieldset id="fieldset" <% if (!(name == null || phone == null || address == null)) {%> disabled <% } %>>
			    <style>
			    #fieldset {
			    	  width: 40%;
			    }
			    input[type="text"] {
			    	  display: block;
			    	  margin: 0;
			    	  width: 90%;
			    	  font-family: sans-serif;
			    	  font-size: 18px;
			    	  appearance: none;
			    	  box-shadow: none;
			    	  border-radius: none;
			    	}
			    	input[type="text"]:focus {
			    	  outline: none;
			    	}
			    #fieldset input[type="text"] {
			    	  padding: 10px;
			    	  border: solid 1px #dcdcdc;
			    	  transition: box-shadow 0.3s, border 0.3s;
			    	}
			     #fieldset input[type="text"]:focus{
			    	  border: solid 1px #707070;
			    	  box-shadow: 0 0 5px 1px #969696;
			    	}
			    </style>
			      Name: <input type="text" name="name" <% if (name != null) { %> value="<%=name%>" <% } %>><br>
			      Address: <input type="text" name="address" <% if (address != null) { %> value="<%=address%>" <% } %>><br>
			      Phone: <input type="text" name="phone" <% if (phone != null) { %> value="<%=phone%>" <% } %>><br>
			      <div id="submit_form" <% if (!(name == null || phone == null || address == null)) {%> style="display: none" <% } %> >
			      	<input type="submit" name="submit">
			      </div>
			      </fieldset>
			    </form>
			    <button onclick="editable()">Edit</button>
			    <button onclick="deleteMethod()">Delete</button>
			    <script type="text/javascript">
			    function editable() {
			    	document.getElementById('fieldset').disabled = false;
			    	document.getElementById('submit_form').style.display = "block";
			    }
			    function deleteMethod() {
			    	if (confirm('Are you sure to delete?')) {
			    		$.ajax({
			    			url: '/pizzashopprofile',
			    			method: 'DELETE',
			    			success:function(data){
			    				window.location.replace("./ordermypizza.jsp");
					          }, 
			    			error: function(data){
			    				alert("Error while deleting.");
			    			}
			    		});
			    	}
			    }
			    </script>
			    <%
				}
			}
			%>
			
    <div id="botinfo">
        <p>Order My Pizza!<br>Geurney 2016</p>
    </div> 
    

</body>
</html>