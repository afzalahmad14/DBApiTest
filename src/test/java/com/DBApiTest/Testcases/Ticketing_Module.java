package com.DBApiTest.Testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Ticketing_Module {
	
String Token;
String BaseURI="https://stage.oodleslab.com";
String ticketID;
	
@BeforeMethod
public void gettoken()
{
 RestAssured.baseURI="https://stage.oodleslab.com";
 RestAssured.basePath="/zuul/dashboard_spring/api/v1";
Token = 
 given().header("Content-Type", "application/json")
.body("{\"email\":\"dimpal.bhatia@oodlestechnologies.com\",\"password\":\"Dashboard@DG\"}")
.when().post(BaseURI+"/api/login")
.then().statusCode(200).extract().path("access_token");
}
@Test
public void officecodes()
{
 Response response=
	        given().header("Authorization", "Bearer "+ Token).
			when().get("/officecodes")
			.then()
			.statusCode(200).extract().response();
		String responsemessage=response.path("message");
		Assert.assertEquals("Office codes fetched", responsemessage);
		System.out.println("officecodesAPI Response time is-->"+response.getTimeIn(TimeUnit.SECONDS));
}

@Test
public void concernedteams()
{   Response response=
	given().header("Authorization", "Bearer "+ Token).
	when().get("/concernedTeams")
	.then()
	.statusCode(200).extract().response();
String responsemessage=response.path("message");

Assert.assertEquals("Concerned Team fetched successfully", responsemessage);
System.out.println("ConcernedTeamsAPI Response time is-->"+response.getTimeIn(TimeUnit.SECONDS));
}

@Test
public void categories()
{   Response response = given().header("Authorization", "Bearer "+ Token).
	when().get("/categories")
	.then().
	statusCode(200).extract().response();
String responsemessage=response.path("message");
Assert.assertEquals("Category fetched successfully", responsemessage);
System.out.println("CategoriesAPI Response time is-->"+response.getTimeIn(TimeUnit.SECONDS));
}

@Test
public void image()
{   Response response=
	given().header("Authorization", "Bearer "+ Token).
	when().get("/username/image").
	then().statusCode(200).extract().response();
	System.out.println("ImageAPI Response time is-->"+response.getTimeIn(TimeUnit.SECONDS));
}



@Test
public void raisedTicketbyUser()
{    Response response=
     given()
	.header("Authorization", "Bearer "+ Token )
	.param("targetTickets", "user")
	.when()
	.get("/ticket/raised")
	.then()
	.statusCode(200).extract().response();

System.out.println("RaisedTicketbyUser Response time is-->"+response.getTimeIn(TimeUnit.SECONDS));
}

@Test
public void indirectreportees()
{
	 {
		
	given()
	.header("Authorization", "Bearer "+ Token )
	.when()
	.get("/ticket/raised?targetTickets=indirectReportees").
	then().statusCode(200);
	}
	
}	

@Test
public void all_Tickets()
{
	 {
		
	Response response= given()
	.header("Authorization", "Bearer "+ Token )
	.when()
	.get("/ticket/raised?targetTickets=all").
	then().statusCode(200).extract().response();
	
	System.out.println(response.getTimeIn(TimeUnit.SECONDS));
	}
	
}	

@Test
public void directreportees()
{
	 given()
	.header("Authorization", "Bearer "+ Token ).
	 when()
	.get("/ticket/raised?targetTickets=directReportees")
	.then()
	.statusCode(200);
}

@Test
public void createTicket()
{
	String json = "{\"concernedTeamIds\":[\"1\"],\"fileDto\":{},\"priority\":\"HIGH\",\"projectName\":\"TimeForge\",\"categoryId\":\"4\",\"subject\":\"dsds dvsdvdsvxz\",\"description\":\"vzx xzcxzvzxv\"}";
	String repsonsejson = given()
	.header("Authorization", "Bearer "+ Token )
	.header("Content-Type", "application/json").
	
	//contentType(ContentType.JSON)*Can be used instead of .header("Content-Type", "application/json").
	body(json).
	 
	 when()
	.post("/ticket")
	.then()
	.statusCode(200).extract().asString();
	
	JsonPath jPath=new JsonPath(repsonsejson);
	Object test = jPath.get("data0.tickets[0].status");
	ticketID=jPath.get("data.tickets[0].ticketId");
	//System.out.println(ticketID);
	String message= jPath.get("message");
	Map<String, String> Map = jPath.getMap("data.tickets[0]");//* To get JSON value as map.
	
	Object a = Map.get("id");
	
	System.out.println(a);
	//System.out.println(message);
	//Asserts
	Assert.assertEquals("Ticket created successfully", message);
	 System.out.println(Map.get("Status"));
	Assert.assertEquals("APPROVED", Map.get("status"));
}

@Test
public void singleticktet()
{
	 given()
	.header("Authorization", "Bearer "+ Token ).param("id", ticketID).
	 when()
	.get("/singleTicket")
	.then()
	.statusCode(200);
	 
	 
}

//@Test
public void leaveTest()
{
	
	for (int i = 01; i <=30; i++) {
		
	
	String json = "{\"jsonData\":{\"type\":\"Work from Home\",\"startDate\":\""+i+"-03-2020\",\"endDate\":\""+i+"-03-2020\",\"reason\":\"cvcv c\"}}";
	Response response=given()
	.header("Authorization", "Bearer "+ Token)
	.header("Content-Type", "application/json")
	.body(json)
	.when().post("https://ms.oodleslab.com/leaveTracker/addLeaveApplication").then().statusCode(200).extract().response();
	String a = response.path("result");
	System.out.println(a);
	//Xls_Reader reader=new Xls_Reader("C:\\Users\\Divya Varnwal\\Downloads\\Leaves.xlsx");
	//reader.setCellData("Sheet1", "Day", i+1 , a);
	}
	}
}
