import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

// mandatory import for rest assured
import static io.restassured.RestAssured.*;

public class APITestCases {

    private static final String BASE_URI = "http://localhost:7081/api/books/";
    private static final String ADMIN_USERNAME = "admin";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final String ADMIN_PASSWORD = "password";

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void testAdminCreateNewBookSuccessfully() {


        // Define the request body with valid book data
        String requestBody = "{\"title\": \"Harry Porter\", \"author\": \"J.K.Rowling\"}";


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post();
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.jsonPath().get("title"), "Harry Porter");
        Assert.assertEquals(response.jsonPath().get("author"), "J.K.Rowling");
    }

    @Test
    public void testUserCreateNewBookSuccessfully() {


        // Define the request body with valid book data
        String requestBody = "{\"title\": \"The Mother\", \"author\": \"Maxim Gorky\"}";


        Response response = given()
                .auth().preemptive().basic(USER_USERNAME, USER_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post();
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.jsonPath().get("title"), "The Mother");
        Assert.assertEquals(response.jsonPath().get("author"), "Maxim Gorky");
    }

    @Test
    public void testAddExistingBookNameAndAuthor() {


        // Define the request body with valid book data
        String requestBody = "{\"title\": \"Harry Porter\", \"author\": \"J.K.Rowling\"}";

        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 208);
    }

    //Expected-400,Return-201
    //This is a bug
    @Test
    public void testCreateBookOnlyWithBookTitle() {


        // Define the request body with valid book data
        String requestBody = "{\"title\":\"Harry Potter\"}";


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("title"), "Harry Porter");
    }

    //Expected-400,Return-201
    @Test
    public void testCreateBookOnlyWithAuthor() {


        // Define the request body with valid book data
        String requestBody = "{\"author\": \"Joseph Henry\"}";


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post();
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().get("author"), "Joseph Henry");

    }

    //Expected-400,Return-201
    @Test
    public void testValidateInputData() {


        // Define the request body with valid book data
        String requestBody = "{\"title\": 1, \"author\": \"J.K.Rowling\"}";


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post();
        Assert.assertEquals(response.getStatusCode(), 400);

    }

}