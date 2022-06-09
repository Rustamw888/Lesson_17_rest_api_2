package qa.guru.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import org.openqa.selenium.Cookie;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static qa.guru.helpers.CustomApiListener.withCustomTemplates;

public class DemoWebShopTests {

    static String login = "qaguru@qa.guru",
            password = "qaguru@qa.guru1",
            authCookieName = "NOPCOMMERCE.AUTH";

    @BeforeAll
    static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

    @AfterEach
    void afterEach() {
        closeWebDriver();
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("Successful authorization to some demowebshop (UI)")
    void loginTest() {
        step("Open login page", () ->
                open("/login"));
        step("Fill login form", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password)
                    .pressEnter();
        });
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("Successful authorization to some demowebshop (API + UI + Map)")
    void loginWithApiTest() {
        Map<String, String> cookies = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", login)
                .formParam("Password", password)
//                .body("Email=" + login + "&Password=" + password + "&RememberMe=true&RememberMe=false")
//                .body(format("Email=%s&Password=%s&RememberMe=true&RememberMe=false", login, password))
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract().cookies();
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("Successful authorization to some demowebshop (API + UI + String)")
    void loginWithApiWithStringTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);

            step("Open minimal content, because cookie can be set when site is opened", () -> {
                open("/Themes/DefaultClean/Content/images/logo.png");
            });
            step("Set cookie to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });
        step("Open main page", () -> {
            open("");
        });
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("Successful authorization to some demowebshop (API + UI + String + Allure)")
    void loginWithApiWithStringWithAllureTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .filter(new AllureRestAssured())
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);

            step("Open minimal content, because cookie can be set when site is opened", () -> {
                open("/Themes/DefaultClean/Content/images/logo.png");
            });
            step("Set cookie to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });
        step("Open main page", () -> {
            open("");
        });
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("Successful authorization to some demowebshop (API + UI + String + Allure + Custom)")
    void loginWithApiWithStringWithAllureCustomTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authCookieName);

            step("Open minimal content, because cookie can be set when site is opened", () -> {
                open("/Themes/DefaultClean/Content/images/logo.png");
            });
            step("Set cookie to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });
        step("Open main page", () -> {
            open("");
        });
    }
}
