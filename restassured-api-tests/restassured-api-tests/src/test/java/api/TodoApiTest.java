package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoApiTest {

    static String baseUrl = "http://localhost:5000";
    static String itemId;

    @Test
    @Order(1)
    void testLoginSuccess() {
        JSONObject payload = new JSONObject();
        payload.put("username", "admin");
        payload.put("password", "admin");

        given()
            .contentType("application/json")
            .body(payload.toString())
        .when()
            .post(baseUrl + "/login")
        .then()
            .statusCode(200)
            .body("message", equalTo("Login successful"));
    }

    @Test
    @Order(2)
    void testLoginFailure() {
        JSONObject payload = new JSONObject();
        payload.put("username", "admin");
        payload.put("password", "wrong");

        given()
            .contentType("application/json")
            .body(payload.toString())
        .when()
            .post(baseUrl + "/login")
        .then()
            .statusCode(401)
            .body("message", equalTo("Invalid credentials"));
    }

    @Test
    @Order(3)
    void testAddItem() {
        JSONObject item = new JSONObject();
        item.put("text", "Write test cases");

        Response res = given()
            .contentType("application/json")
            .body(item.toString())
        .when()
            .post(baseUrl + "/items")
        .then()
            .statusCode(201)
            .body("text", equalTo("Write test cases"))
            .extract().response();

        itemId = res.jsonPath().getString("id");
    }

    @Test
    @Order(4)
    void testGetItems() {
        given()
        .when()
            .get(baseUrl + "/items")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    @Order(5)
    void testUpdateItem() {
        JSONObject update = new JSONObject();
        update.put("text", "Write automated tests");

        given()
            .contentType("application/json")
            .body(update.toString())
        .when()
            .put(baseUrl + "/items/" + itemId)
        .then()
            .statusCode(200)
            .body("text", equalTo("Write automated tests"));
    }

    @Test
    @Order(6)
    void testDeleteItem() {
        given()
        .when()
            .delete(baseUrl + "/items/" + itemId)
        .then()
            .statusCode(200)
            .body("message", equalTo("Item deleted"));
    }
}