package StudentPractice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

public class api_Activities {
    public String path;
    String memberOf = "/workspaces/member-of";
    Response response;

 @BeforeTest
    public String setupLoginAndToken() {
     RestAssured.baseURI = "https://api.octoperf.com";
     path = "/public/users/login";

     Map<String, Object> map = new HashMap<>();
     map.put("password", "test123");
     map.put("username", "iseclass@yahoo.com");

     return RestAssured.given()
             .queryParams(map)
             .contentType(ContentType.JSON)
             .accept(ContentType.JSON)
             .post(path)// send request to endpoint
             .then()
             .statusCode(SC_OK)  // the way to use interface to verify Status Code or .statusCode(200)
             .extract() // methot that extracts JSON data
             .body()  // Body extracts as JSON format
             // instead of body(), can be cookies(), or headers()
             .jsonPath() // navigates using jsonPath
             .get("token"); // gets value for key Token
 }
    // TODO:
    // Testing URI = https://fakerestapi.azurewebsites.net/api/v1/

    // Task 1: Using Rest Assured validate the status code for endpoint /Activities

    // Task 2: Using Rest Assured Validate Content-Type  is application/json; charset=utf-8; v=1.0
    // for endpoint /Activities

    // Task 3: Using Rest Assured validate the status code for endpoint /Activities/12

    // Task 4: Using Rest Assured Validate Content-Type  is application/json; charset=utf-8; v=1.0
    // for endpoint /Activities/12

    @Test
    public void verifyStatusCode() {
        RestAssured.given()// allows to build the request
                .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Activities")
                .then() // allows to do validation
                .assertThat()
                .statusCode(200);
    }
    @Test
    public void verifyContentType() {
        RestAssured.given()
                .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Activities/")
                .then()
                .assertThat()
                .contentType(ContentType.JSON);
    }
    @Test
    public void verifyContentTypeOption2() {
        RestAssured.given()
                .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Activities/")
                .then()
                .assertThat().header("Content-Type", "application/json; charset=utf-8; v=1.0");
    }

    @Test
    public void verifyStatusCode12() {
        RestAssured.given()
                .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Activities/12")
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    public void verifyContentType12() {
        RestAssured.given()
                .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Activities/12")
                .then()
                .assertThat()
                .contentType(ContentType.JSON);
    }
    @Test
    public void verifyID() {
        RestAssured.given()
                .when()
                .get();
    }
    // TODO: add test for ID, userID, & Description
    @Test
    public void verifyID1() {

        response = RestAssured.given()
                .header("Authorization", setupLoginAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response();
        Assert.assertEquals("[]", response.jsonPath().getString("id[]"));
        System.out.println(response.jsonPath().getString("id[0]"));
    }

    @Test
    public void verifyUserID() {
        response = RestAssured.given()
                .header("Authorization", setupLoginAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response();
        Assert.assertEquals("[]", response.jsonPath().getString("userId[]"));
        System.out.println(response.jsonPath().getString("userId[0]"));
    }
    @Test
    public void verifyDescription() {
        response = RestAssured.given()
                .header("Authorization", setupLoginAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response();
        Assert.assertEquals("[]", response.jsonPath().getString("description[]"));
        System.out.println(response.jsonPath().getString("description[1]")); // optional
    }

}
