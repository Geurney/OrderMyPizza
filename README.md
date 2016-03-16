# OrderMyPizza
Online Pizza Order web application for CS264 Runtime System Project

# Application
http://ordermypizza-y.appspot.com/

#Test

For pizzashop, pizzafactory and customer, most operations require user authenticiation (login with google account). So I tested using "Advanced REST client", a Chrome plug in to send request after signing in google account using browser.

For API test, use the file under test folder.


Get all pizzashops:


curl -H "Accept: application/json" -X GET http://ordermypizza-y.appspot.com/pizzashop/rest/pizzashop/all

Find pizzashops in Goleta:


curl -H "Accept: application/json" -X GET http://ordermypizza-y.appspot.com/pizzashop/rest/pizzashop/city/Goleta

Find pizzashop by its identifier:


curl -H "Accept: application/json" -X GET http://ordermypizza-y.appspot.com/pizzashop/rest/pizzashop/findbyidentifier/SPS

Find pizza factory by its identifier:


curl -H "Accept: application/json" -X GET http://ordermypizza-y.appspot.com/pizzafactory/rest/pizzafactory/findbyidentifier/SPS

Get pizzashop profile authorized with token:

curl -H "Accept: application/json" -X GET http://ordermypizza-y.appspot.com/pizzashop/rest/pizzashop/authorize/e284ff361a12fc5a0c1bb59c4bedf811fc13df22d6159d40559f295bf659620e


After login in as a customer in web browser use Rest Client plug in for Chrome:


Get user profile:


GET http://ordermypizza-y.appspot.com/customer/rest/customer/


"Accept: application/json" 

Update user profile:


POST http://ordermypizza-y.appspot.com/customer/rest/customer


"Content-Type: application/json" '{"name":"New Name","phone":"987654321"}'

