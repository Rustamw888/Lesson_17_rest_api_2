package qa.guru.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Cookie;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.guru.config.Remote;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.HTML;
import static qa.guru.helpers.CustomApiListener.withCustomTemplates;

public class DemoWebShopTests extends TestBase {

    static String login,
            password,
            authCookieName = "NOPCOMMERCE.AUTH",
            itemAdd;

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = Remote.config.apiUrl();
        Configuration.baseUrl = Remote.config.webUrl();
        login = Remote.config.userLogin();
        password = Remote.config.userPassword();
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
            open("/");
        });
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("Shopping card is not empty")
    void itemAddedToCartTest() {
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

            step("Item added to cart",()->{
                itemAdd = String.valueOf(given()
                        .filter(withCustomTemplates())
                        .cookie(authCookieName, authCookieValue)
                        .log().all()
                        .when()
                        .get("/cart")
                        .then().log().all()
                        .statusCode(200)
                        .contentType(HTML)
                        .extract().toString().contains("io.restassured.internal.RestAssuredResponseImpl@"));
            });

            step("Open minimal content, because cookie can be set when site is opened", () -> {
                open("/Themes/DefaultClean/Content/images/logo.png");
            });
            step("Set cookie to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });
        step("Open cart page", () -> {
            open("/cart");
        });
        step("Item successful added to cart", () ->
                $(".tbody .product-name").find(byText("$100 Physical Gift Card")));
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("New Test api lesson part 3")
    void newTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .body(String.format())
                    .log().all()
                    .when()
                    .post("/addproducttocart/details/74/1")
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
            open("/");
        });
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }
}
