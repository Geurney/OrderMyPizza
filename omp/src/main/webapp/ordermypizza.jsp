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
	<!--script type="text/javascript" src="./js/pizzashopmap.js"></script -->

	<script src="http://www.google.com/jsapi"></script>
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
			        	<td class="iw_attribute_name">Name:</td>
		    	    	<td id="iw-url"></td>
		    		</tr>
		    		<tr id="iw-phone-row" class="iw_table_row">
		        		<td class="iw_attribute_name">Telephone:</td>
		        		<td id="iw-phone"></td>
		      		</tr>
		      		<tr id="iw-email-row" class="iw_table_row">
			        	<td class="iw_attribute_name">Email:</td>
			        	<td id="iw-email"></td>
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
<script>
var map, infoWindow;
var markers = [];
var autocomplete;
var loc={};
var countryRestrict = {
  'country': 'us'
};
var MARKER_PATH = 'https://maps.gstatic.com/intl/en_us/mapfiles/marker_green';

var countries = {
  'us': {
    center: {
      lat: 37.1,
      lng: -95.7
    },
    zoom: 3
  }
};

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: countries['us'].zoom,
    center: countries['us'].center,
    mapTypeControl: false,
    panControl: false,
    zoomControl: false,
    streetViewControl: false
  });

  infoWindow = new google.maps.InfoWindow({
    content: document.getElementById('info-content')
  });

  autocomplete = new google.maps.places.Autocomplete(
    (
      document.getElementById('citylocation')), {
      types: ['(cities)'],
      componentRestrictions: countryRestrict
    });
  
  autocomplete.addListener('place_changed', onPlaceChanged);
  getLocation();
}

function getLocation() {
  findmylocation();
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition);
  } else {
    alert("Geolocation is not supported by this browser.");
  }
}
function showPosition(position) {
  var lat = position.coords.latitude;
  var lng = position.coords.longitude;
  map.setCenter(new google.maps.LatLng(lat, lng));
  map.setZoom(11);
  search_center();
  //search_center(lat, lng);
}

function onPlaceChanged() {
  var place = autocomplete.getPlace();
  if (place.geometry) {
    map.panTo(place.geometry.location);
    map.setZoom(15);
    loc.city = place.name;
    search_center();
   // search_center(place.geometry.location.lat(),place.geometry.location.lng());
    
  } else {
    document.getElementById('citylocation').placeholder = 'Enter a city';
  }
}

function search_center(){
	$.ajax({
        dataType: "json",
		url: "/pizzashop/rest/pizzashop/city/" + loc.city,
		method : 'GET',
		success: function(data) {
			clearResults();
            clearMarkers();
			document.getElementById('noresult').style.display = 'none';
            document.getElementById('resultsTableWrapper').style.display = 'block';
			for (var i = 0; i < data.length; i ++ ){
				var markerLetter = String.fromCharCode('A'.charCodeAt(0) + i);
				var markerIcon = MARKER_PATH + markerLetter + '.png';
                var latlng = {lat: parseFloat(data[i].latitude), lng: parseFloat(data[i].longitude)};
				markers[i] = new google.maps.Marker({
				  position: latlng,
				  animation: google.maps.Animation.DROP,
				  icon: markerIcon
				});
				attachWindow(markers[i], data[i]);
				setTimeout(dropMarker(i), i * 100);
				addResult(data[i], i);
            }
		},
		error: function(jqXHR, textStatus, errorThrown) {
			if (jqXHR.status == "404") {
					alert("No shop found!");
			}
		}
    });
}
function findmylocation() {
	if(google.loader.ClientLocation) {
		loc.city = google.loader.ClientLocation.address.city;
		loc.latitude = google.loader.ClientLocation.latitude;
		loc.longitude = google.loader.ClientLocation.longitude;
    } else {
			alert('Location not available');
	}
}
function attachWindow(marker, place) {
  marker.addListener('click', function() {
    infoWindow.open(map, marker);
	buildIWContent(place);
  });
}

function clearMarkers() {
  for (var i = 0; i < markers.length; i++) {
    if (markers[i]) {
      markers[i].setMap(null);
    }
  }
  markers = [];
}

function dropMarker(i) {
  return function() {
    markers[i].setMap(map);
  };
}

function addResult(result, i) {
  var results = document.getElementById('results');
  var markerLetter = String.fromCharCode('A'.charCodeAt(0) + i);
  var markerIcon = MARKER_PATH + markerLetter + '.png';

  var tr = document.createElement('tr');
  tr.style.backgroundColor = (i % 2 === 0 ? '#F0F0F0' : '#FFFFFF');
  tr.onclick = function() {
    window.location.href = "/pizzashop.jsp?identifier=" + result.identifier;
  };

  var iconTd = document.createElement('td');
  var nameTd = document.createElement('td');
  var icon = document.createElement('img');
  icon.src = markerIcon;
  icon.setAttribute('class', 'placeIcon');
  icon.setAttribute('className', 'placeIcon');
  var name = document.createTextNode(result.name);
  iconTd.appendChild(icon);
  nameTd.appendChild(name);
  tr.appendChild(iconTd);
  tr.appendChild(nameTd);
  results.appendChild(tr);
}

function clearResults() {
  var results = document.getElementById('results');
  while (results.childNodes[0]) {
    results.removeChild(results.childNodes[0]);
  }
}


function buildIWContent(place) {
  document.getElementById('iw-url').innerHTML = '<b><a href="/pizzashop.jsp?identifier=' + place.identifier +
    '">' + place.name + '</a></b>';
  document.getElementById('iw-email').textContent = place.email;
  document.getElementById('iw-phone').textContent = place.phone;

  /*
  if (place.rating) {
    var ratingHtml = '';
    for (var i = 0; i < 5; i++) {
      if (place.rating < (i + 0.5)) {
        ratingHtml += '&#10025;';
      } else {
        ratingHtml += '&#10029;';
      }
      document.getElementById('iw-rating-row').style.display = '';
      document.getElementById('iw-rating').innerHTML = ratingHtml;
    }
  } else {
    document.getElementById('iw-rating-row').style.display = 'none';
  }
*/
}

</script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCajB_AS8cdfBcCDP2qIhpKHP8FZiiq1e0&signed_in=true&libraries=geometry,places&callback=initMap"
		async defer></script>
</body>
</html>