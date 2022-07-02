package API_Testing.API_Day1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class _01_introRestAssured {
// Rest Assured is a java library that is used to perform API tests
// It uses BDD Style such as given, when, then, and, etc.
// Rest Assured has methods to fetch data from the response body. When making a request.
// Rest Assured works with HTTP requests: CRUD (Create, Read, Update, Delete) (POST, GET, PUT, DELETE)
// As testers we mostly use 2 requests GET, POST among DELETE and PUT.

// NOTE: As testers we mostly work with JSON Payloads (BODY), we also experience XML files, text, pdf etc.

// Some of the most used methods in RestAssured are:
//  When making a request:
//      given() ---> used to prepare the request
//      when() ---> used to send the request
//      then() ---> used to verify the request
//  When we verify the response:
//      prettyPeek() ---> used to print the response headers, urls etc. in pretty format in the Console
//      prettyPrint() ---> used to print the response in pretty format
//      log() ---> logs(prints) all the response
//      asString() ---> allows us to print in String format
//      contentType() ---> used to verify the content type from the response body. When use POST request
//      accept() ---> used to verify body response from the header when making a GET request

    //      baseURI ---> used to save the base URL for all the resources
    public static String baseURI = "https://api.octoperf.com";

    //  When we make a request we only provide the path(endpoint) to a specific baseURI
    private String path = "public/users/login";

// FULL URL = https://api.octoperf.com/public/users/login

// What is an endpoint?
// An endpoint is a unique URL that represents and object or collections of Objects

// Example: http://www.google.com/search?source=JavaBook
//                  baseURI       Endpoint  ? parameter

// Base url = https://api.octoperf.com
// Full url = https://api.octoperf.com/public/users/login
// Full url with Query params =
//          https://api.octoperf.com/public/users/login?password=test12&username=tla.jiraone@gmail.com

// Task: Make a HTTP: POST request with given
// full url with query params and print the response

/*
When we verify the status codes we must pay attention to the following errors from the response body

1xx --> Information
2xx --> success ( 200 -> OK, 201 -> Created, 202 -> Accepted, 204 -> No content)
3xx --> Redirection
4xx --> Client error (400 -> Bad Request, 401 -> Unauthorized, 403 -> Forbidden, 404 -> Not Found)
5xx --> Server error (500 -> Internal Server Error)
 */

// Perform a Post request to log in API endpoint
// Verify Status code is 200 OK

    @Test
    public void printResponse() {
        RestAssured.given() //RestAssured is a class which contains methods
                .when()
                .post("https://api.octoperf.com/public/users/login?password=Redmops364&username=iseclass@yahoo.com")
                .prettyPeek(); // printsthe whole response body & headers
    }
    @Test
    public void prettyPrint() {
        RestAssured.given()
                .when()
                .post("https://api.octoperf.com/public/users/login?password=Redmops364&username=iseclass@yahoo.com")
                .prettyPrint();
    }

    // Peprform a Post request to log in tin API endpoint
    // Verify Status code is 200 OK
    @Test
    public void verifyStatusCode() {
        RestAssured.given()
                .when()
                .post("https://api.octoperf.com/public/users/login?password=Redmops364&username=iseclass@yahoo.com")
                .then()
                .assertThat()
                .statusCode(200);
        System.out.println("Test verified. Status code is 200"); // optional
    }

    // Peprform a Post request to log in tin API endpoint
    // Verify Content Type = JSON
    @Test
    public void verifyContentType() {
        RestAssured.given()
                .when()
                .post("https://api.octoperf.com/public/users/login?password=Redmops364&username=iseclass@yahoo.com")
                .then()
                .assertThat()
                .contentType(ContentType.JSON);
        System.out.println("Test verified. Content Type = JSON");
    }
}

