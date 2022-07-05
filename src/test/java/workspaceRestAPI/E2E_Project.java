package workspaceRestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.checkerframework.checker.units.qual.A;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
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
                .body()  // Body extracts as JSON format; instead of body(), can be cookies(), or headers()
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
        // The method to verify the name
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));

        // Save the ID in order to use it in other requests
        ID = response.jsonPath().get("id[0]");

        // Save the userID in order to use it in other requests
        user_Id = response.jsonPath().get("userId[0]");

        // What to use to store the data as Key & Value --> Map
        variables = new HashMap<>();
        variables.put("id", ID);

        // TODO verify ID, userID, description
        Assert.assertEquals("9smWxX0Bp7hMViDsqY5p", response.jsonPath().getString("id[0]"));
        Assert.assertEquals("ZpKWxX0BtLxR4BgeAONt", response.jsonPath().getString("userId[0]"));
        Assert.assertEquals("", response.jsonPath().getString("description[0]"));
    }

    @Test(dependsOnMethods = {"memberOf"}, description = "we need this method to be able to create the project")
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

        Assert.assertEquals(ID, response.jsonPath().getString("workspaceId"));
        Assert.assertEquals(user_Id, response.jsonPath().getString("userId"));

        // Store id in a variable for future use
        projectID = response.jsonPath().get("id");
        System.out.println("New id created when creating a project " + projectID);
   }

    @Test(dependsOnMethods = {"memberOf", "createProject"})
        public void updateProject(){

        String requestBody1 = "{\"created\":1615443320845,\"description\":\"NewUpate\"," +
                "\"id\":\"" + projectID + "\",\"lastModified\":1629860121757," +
                "\"name\":\"TEST\",\"tags\":[],\"type\":\"DESIGN\"," +
                "\"userId\":\"" + variables.get("userID") + "\"," +
                "\"workspaceId\":\"" + variables.get("id") + "\"}";

        // Get response
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

//       Another way to do it --> Create JSON body
//        JSONObject body = new JSONObject();
//        body.put("created", 1615443320845L);
//        body.put("description", "TLAupdate");
//        body.put("id", projectID);
//        body.put("lastModified", 1656902579223L);
//        body.put("name", "TLA accounting");
//        body.put("type", "DESIGN");
//        body.put("userId", variables.get("userId"));
//        body.put("workspaceId", variables.get("id"));

        System.out.println(response.prettyPeek());

        // TODO: add Assertions for id, name, userId, workspaceId, Status code, Content Type
        Assert.assertEquals("TEST", response.jsonPath().getString("name"));
        Assert.assertEquals("ZpKWxX0BtLxR4BgeAONt", response.jsonPath().getString("userId"));
        Assert.assertEquals("9smWxX0Bp7hMViDsqY5p", response.jsonPath().getString("workspaceId"));
        Assert.assertEquals(SC_OK, response.statusCode());

        assertThat(response.jsonPath().getString("id"), is(projectID));
        //assertThat(response.jsonPath().getString("name"), is(.get("name")));
        //assertThat(response.jsonPath().getString("type"), is(body.get("type")));
        assertThat(response.jsonPath().getString("userId"), is(user_Id));
        assertThat(response.jsonPath().getString("workspaceId"), is(ID));
        assertThat(response.statusCode(), is(SC_OK)); // Assert.assertEquals(SC_OK,200);
        assertThat(response.contentType(), is(ContentType.JSON.toString()));

    }

    @Test(dependsOnMethods = {"memberOf", "createProject", "updateProject"})
    public void deleteProject() {
        response = RestAssured.given()
                .header("Authorization", setupLoginAndToken())
                .when()
                .delete("/design/projects/" + projectID)
                .then()
                .log().all()
                .extract()
                .response();
        // TODO: validate the Status code
        Assert.assertEquals(SC_NO_CONTENT,204);
        // or assertThat(response.statusCode(), is(204));
        }
    }


