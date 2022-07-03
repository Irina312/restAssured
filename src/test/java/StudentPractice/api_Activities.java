package StudentPractice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class api_Activities {
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
}
