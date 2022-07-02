package API_Day2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class _02_LoginTypes {
    /*
    Log in with Fll URL with query params
    and verify status code and Content-type is equal to JSON
     */

    @Test
    public void testUsingQueryParams() {
        RestAssured.given()
                .when()
                .post("https://api.octoperf.com/public/users/login?password=Redmops364&username=iseclass@yahoo.com")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat().contentType(ContentType.JSON);
    }
    /*
    Log in usingMap to verify Content Type (using query params)
    Map stores Key and Value
    HashMap implements Map. We can store different datat type of Object
     */

    @Test
    public void LoginWithMap() {
        RestAssured.baseURI="https://api.octoperf.com";
        String path = "/public/users/login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "iseclass@yahoo.com");
        map.put("password", "Redmops364");

        RestAssured.given()
                .queryParams(map)
                .when()
                .post(path)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .and()
                .assertThat()
                .statusCode(200);
    }
    // Using query param
    @Test
    public void LoginWithQueryParam() {
        RestAssured.baseURI="https://api.octoperf.com";
        String path = "/public/users/login";
         RestAssured.given()
                .queryParam("username", "iseclass@yahoo.com")
                .queryParam("password", "Redmops364")
                .when()
                .post(path)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .and()
                .assertThat()
                .statusCode(200);
    }
}
