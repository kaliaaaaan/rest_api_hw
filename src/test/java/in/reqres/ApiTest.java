package in.reqres;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ApiTest extends TestBase {
    @DisplayName("Проверка наличия определённого last_name из списка")
    @Test
    void checkExistingEmailInArrayTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.last_name", hasItem("Howell"));
    }

    @DisplayName("Проверка ошибки 404 при получении несуществующего пользователя")
    @Test
    void checkNonExistingUserTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/users/137")
                .then()
                .log().status()
                .statusCode(404);
    }

    @DisplayName("Проверка успешного создания")
    @Test

    void checkNewUserTest() {
        String bodyData = "{\n" +
                "    \"name\": \"neo\",\n" +
                "    \"job\": \"the chosen one\"\n" +
                "}";

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(bodyData)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("neo"))
                .body("job", is("the chosen one"));
    }

    @DisplayName("Проверка успешной регистрации")
    @Test
    void checkRegisterUserTest() {
        String bodyData = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(bodyData)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"))
                .body("id", notNullValue());
    }

    @DisplayName("Проверка получения определённого пользователя по его id")
    @Test
    void checkExistingUserTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/users/10")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(10))
                .body("data.first_name", is("Byron"));
    }

    @DisplayName("Проверка ошибки при создании пользователя")
    @Test
    void checkRegisterUserNegativeTest() {
        String bodyData = "{\n" +
                "    \"email\": \"1337@mail.in\",\n" +
                "    \"password\": \"password\"\n" +
                "}";

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(bodyData)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

}