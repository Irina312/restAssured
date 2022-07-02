package workspaceRestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

public class E2E_Project {

    public String path;
    String memberOf = "/workspaces/member-of";

// What is testNG annotation that allows to run a TestBefore each Test
    @BeforeTest
    public String setupLoginAndToken() {
        RestAssured.baseURI = "https://api.octoperf.com";
        path = "/public/users/login";

        Map<String, Object> map = new HashMap<>();
        map.put("password", "Redmops364");
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
    // Write a test for API endpoint member-of
    @Test
    public void verifyToken() {
        String memberOf = "/workspaces/member-of";
        Response response = RestAssured.given()
                .header("Authorization", setupLoginAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response();

        // The method verifies the Status Code
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));

        // TODO: add test for ID, userID, & Description
    }
}
