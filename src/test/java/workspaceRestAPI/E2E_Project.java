package workspaceRestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.checkerframework.checker.units.qual.A;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.HashMap;
import java.util.Map;
import static org.apache.http.HttpStatus.SC_OK;

public class E2E_Project {

    public String path;
    String memberOf = "/workspaces/member-of";
    Map<String, String> variables;
    String ID;
    String user_Id;
    Response response;
    String projectID;

    // What is testNG annotation that allows to run a TestBefore each Test
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

    // Write a test for API endpoint member-of
    @Test
    public void memberOf() {
        response = RestAssured.given()
                .header("Authorization", setupLoginAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response();

        // The method verifies the Status Code
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));

        // Save the ID in order to use it in other requests
        ID = response.jsonPath().get("id[0]");

        // Save the userID in order to use it in other requests
        user_Id = response.jsonPath().get("userId[0]");

        // What to use to store the data as Key & Value --> Map
        variables = new HashMap<>();
        variables.put("id", ID);
        variables.put("userID", user_Id);

    }

    @Test(dependsOnMethods = {"memberOf"})
    public void createProject() {
        String requestBody = "{\"id\":\"\",\"created\":\"2021-03-11T06:15:20.845Z\"," +
                "\"lastModified\":\"2021-03-11T06:15:20.845Z\"," +
                "\"userId\":\"" + variables.get("userID") + "\"," +
                "\"workspaceId\":\"" + variables.get("id") + "\"," +
                "\"name\":\"testing22\",\"description\":\"testing\"," +
                "\"type\":\"DESIGN\",\"tags\":[]}";
        response = RestAssured.given()
                .headers("Content-type", "application/json")
                .header("Authorization", setupLoginAndToken())
                .and()
                .body(requestBody)
                .when()
                .post("/design/projects")
                .then()
                .extract()
                .response();

        System.out.println(response.prettyPrint());

        // TASK Create TestNG Assertions Name, id, userId, workspaceID
        Assert.assertEquals("testing22", response.jsonPath().getString("name"));
        // TASK Assert Status code and Content-type

        // Using hamcrest Matchers validation
        assertThat(response.jsonPath().getString("name"), is("testing22"));

        // Store id in a variable for future use
        projectID = response.jsonPath().get("id");
        System.out.println("New id created when creating a project " + projectID);

//        Assert.assertEquals("id", response.jsonPath().getString("9smWxX0Bp7hMViDsqY5p"));
//        Assert.assertEquals("userId", response.jsonPath().getString("ZpKWxX0BtLxR4BgeAONt"));
//        Assert.assertEquals("description", response.jsonPath().getString("test  updated"));
    }
    @Test(dependsOnMethods = {"memberOf", "createProject"})
    public void updateProject(){
        String requestBody1 = "{\"created\":1615443320845,\"description\":\"TLAUpate\"," +
                "\"id\":\"" + projectID + "\",\"lastModified\":1629860121757," +
                "\"name\":\"tlaAccounting firm\",\"tags\":[],\"type\":\"DESIGN\"," +
                "\"userId\":\"" + variables.get("userID") + "\"," +
                "\"workspaceId\":\"" + variables.get("id") + "\"}";

        response = RestAssured.given()
                .headers("Content-type", "application/json")
                .header("Authorization", setupLoginAndToken())
                .and()
                .body(requestBody1)
                .when()
                .put("/design/projects/"+projectID)
                .then()
                .extract()
                .response();
        System.out.println(response.prettyPeek());

        // TODO:
        // add Assertions for id, name, userId, workspaceId, Status code, Content Type
    }

    @Test(dependsOnMethods = {"memberOf", "createProject", "updateProject"})
    public void deleteProject() {
        response = RestAssured.given()
                .header("Authorization", setupLoginAndToken())
                .when()
                .delete("/design/projects" + projectID)
                .then()
                .extract()
                .response();
        // TODO: validate the Status code

    }

    }


