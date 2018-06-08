[![Build Status](https://travis-ci.org/Paz1506/VotingSystem.svg?branch=master)](https://travis-ci.org/Paz1506/VotingSystem)



# Voting system
**Task:** Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend. Build a voting system for deciding where to have lunch.

**Conditions:**
 - 2 types of users: admin and regular users;
 -  Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a
   dish name and price);
 - Menu changes each day (admins do the updates);
 - Users can vote on which restaurant they want to have lunch atж
 - Only one vote counted per user If user votes again the same day;
	 - If it is before 11:00 we asume that he changed his mind;
     - If it is after 11:00 then it is too late, vote can't be changed;
  - Each restaurant provides new menu each day.

P.S.: Asume that your API will be used by a frontend developer to build frontend on top of that.

## Technologies used:

 - Java 8 
 - Spring 5: Core, WebMVC, Data JPA, Security, Test 
 - Hibernate 5 ORM 
 - Logback 
 - EHcache 3 
 - JUnit, AssertJ 
 - Jackson to work with JSON data
 -  HSQLDB database
 -  Maven for build & dependency management

The application was started and tested on **tomcat 8**


## Application API

Requests for API:  
- User-requests - allowed to the user with role USER  
 - Admin-requests - allowed only to the user with role of ADMIN
 > **Note:** Admin requests have prefix **/admin**
 
 > **Note:** The application provides compatibility with different versions. The version is specified in the query path: **/v1.0**

### User cURL requests:

<table width=100% align="center">
  <tr>
    <th width="10%">Type</th>
    <th width="50%">Request</th>
    <th>Description</th>
  </tr>
  <tr>
    <td bgcolor="#ccffcc" align="center" colspan="3">Restaurants</td>
  </tr>
 <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/restaurants --user user@yandex.ru:1234</td>
    <td>Get restaurants in which the menu is published for the current day. If the restaurant is not published menu for the current day, it is not returned.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/restaurants/1 --user user@yandex.ru:1234</td>
    <td>Get restaurant by id.</td>
  </tr>
  <tr>
    <td bgcolor="#ccffcc" align="center" colspan="3">Menus</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/restaurants/1/menus --user user@yandex.ru:1234</td>
    <td>Get the entire restaurant menu published for the current day.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/restaurants/1/menus/5 --user user@yandex.ru:1234</td>
    <td>Get menu by menu id & restaurant id.</td>
  </tr>
  <td bgcolor="#ccffcc" align="center" colspan="3">Dishes</td>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/menus/5/dishes --user user@yandex.ru:1234</td>
    <td>Get all dishes in the menu for the current day.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/menus/5/dishes/12 --user user@yandex.ru:1234</td>
    <td>Get dish by menu id & restaurant id.</td>
  </tr>
  <td bgcolor="#ccffcc" align="center" colspan="3">Votes</td>
   <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST  http://localhost:8080/v1.0/restaurants/1/votes --user user@yandex.ru:1234</td>
    <td>Voting of the user for the restaurant. Voting is strictly up to 11:00 AM current day.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/restaurants/1/votes/count --user user@yandex.ru:1234</td>
    <td>Get count of restaurant votes  for the current day, if the time is after 11:00 AM. If the time is before, returns -1. </td>
  </tr>
</table>

### Admin cURL requests:
<table width=100% align="center">
  <tr>
    <th width="10%">Type</th>
    <th width="50%">Request</th>
    <th>Description</th>
  </tr>
  <tr>
    <td bgcolor="#ccffff" align="center" colspan="3">Restaurants</td>
  </tr>
 <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/restaurants --user admin@gmail.com:1234</td>
    <td>Get all restaurants.</td>
  </tr>
   <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/restaurants/1 --user admin@gmail.com:1234</td>
    <td>Get restaurant by id.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"name\":\"rest_test\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants --user admin@gmail.com:1234</td>
    <td>Create new restaurant.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X DELETE -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants/2 --user admin@gmail.com:1234</td>
    <td>Delete restaurant 2.</td>
  </tr>
    <tr>
    <td><b><font color="red">DELETE</font></b></td>
    <td>curl -s -X POST -d "{\"id\":\"1\", \"name\":\"updated_1\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants --user admin@gmail.com:1234</td>
    <td>Update restaurant 1.</td>
  </tr>
  <tr>
    <td bgcolor="#ccffff" align="center" colspan="3">Menus</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/restaurants/1/menus --user admin@gmail.com:1234</td>
    <td>Get all menus by restaurant id.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/restaurants/1/menus/5 --user admin@gmail.com:1234</td>
    <td>Get menu 5 by restaurant 1 .</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/menus/5 --user admin@gmail.com:1234</td>
    <td>Get menu 5.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"name\": \"new_menu_restaurant\",\"date\": \"2018-05-22T10:01\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants/1/menus --user admin@gmail.com:1234</td>
    <td>Create new menu for restaurant 1.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"id\": \"5\", \"name\": \"updated_menu_5\",\"date\": \"2018-05-22T10:01\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants/1/menus --user admin@gmail.com:1234</td>
    <td>Update menu 5 for restaurant 1.</td>
  </tr>
  <tr>
    <td><b><font color="red">DELETE</font></b></td>
    <td>curl -s -X DELETE -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants/1/menus/5 --user admin@gmail.com:1234</td>
    <td>Delete menu by id.</td>
  </tr>
  <tr>
  <td bgcolor="#ccffff" align="center" colspan="3">Dishes</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/restaurants/1/menus/5/dishes --user admin@gmail.com:1234</td>
    <td>Get all dishes by menu 5 restaurant 1.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/menus/5/dishes --user admin@gmail.com:1234</td>
    <td>Get all dishes by menu 5.</td>
  </tr>
   <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/restaurants/1/menus/5/dishes/12 --user admin@gmail.com:1234</td>
    <td>Get dish 12 menu 5 restaurant 1.</td>
  </tr>
   <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/dishes/12 --user admin@gmail.com:1234</td>
    <td>Get dish 12.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"name\": \"new_dish\",\"price\": \"777\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants/1/menus/5/dishes --user admin@gmail.com:1234</td>
    <td>Create new dish for menu 5 & restaurant 1.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"name\": \"new_dish\",\"price\": \"777\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/menus/5/dishes --user admin@gmail.com:1234</td>
    <td>Create new dish for menu 5.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"id\":\"12\",\"name\": \"upd_dish\",\"price\": \"777\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/restaurants/1/menus/5/dishes --user admin@gmail.com:1234</td>
    <td>Update dish 12 for menu 5 & restaurant 1.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"id\":\"12\",\"name\": \"upd_dish\",\"price\": \"888\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/menus/5/dishes --user admin@gmail.com:1234</td>
    <td>Update dish 12 for menu 5.</td>
  </tr>
  <tr>
    <td><b><font color="red">DELETE</font></b></td>
    <td>curl -s -X DELETE -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/dishes/12 --user admin@gmail.com:1234</td>
    <td>Delete dish by id.</td>
  </tr>
  <tr>
  <td bgcolor="#ccffff" align="center" colspan="3">Users</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/users --user admin@gmail.com:1234</td>
    <td>Get all users.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/users/20 --user admin@gmail.com:1234</td>
    <td>Get user by id.</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"name\": \"newuser\", \"email\": \"user77@yandex.ru\", \"password\": \"12345\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/users --user admin@gmail.com:1234</td>
    <td>Create new user</td>
  </tr>
  <tr>
    <td><b><font color="orange">POST</font></b></td>
    <td>curl -s -X POST -d "{\"id\":\"19\", \"name\": \"updated19\", \"email\": \"user77@yandex.ru\", \"password\": \"123456\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/users --user admin@gmail.com:1234</td>
    <td>Update user</td>
  </tr>
  <tr>
    <td><b><font color="red">DELETE</font></b></td>
    <td>curl -s -X DELETE -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/v1.0/admin/users/22 --user admin@gmail.com:1234</td>
    <td>Delete user by id.</td>
  </tr>
  <tr>
  <td bgcolor="#ccffff" align="center" colspan="3">Votes</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/restaurants/1/votes --user admin@gmail.com:1234</td>
    <td>Get all restaurant votes.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/users/19/votes --user admin@gmail.com:1234</td>
    <td>Get all user votes.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  http://localhost:8080/v1.0/admin/votes/23 --user admin@gmail.com:1234</td>
    <td>Get vote by id.</td>
  </tr>
  <tr>
    <td><b><font color="green">GET</font></b></td>
    <td>curl -s -X GET  "http://localhost:8080/v1.0/admin/restaurants/1/votes/count?startDate=2000-01-01&startTime=00:00:00&endDate=2020-01-01&endTime=23:59:59" --user admin@gmail.com:1234</td>
    <td>Get count votes with time filtering. Params: startDate, endDate, startTime, endTime. If parameters not present, return count votes of current day.</td>
  </tr>
  </table>





