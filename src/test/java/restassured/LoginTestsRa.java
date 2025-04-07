package restassured;

import dto.AuthRequestDto;
import dto.AuthResponseDto;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoginTestsRa {
    String endpoint="user/login/usernamepassword";
    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI="";
        RestAssured.basePath="v1";
    }

    @Test

    public void loginSuccess(){
        AuthRequestDto auth= AuthRequestDto.builder()
                .username("sonicboom@gmail.com")
                .password("Snow123456!")
                .build();
AuthResponseDto responseDto= given()
                .body(auth)
                .contentType("application/json")
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }
}
