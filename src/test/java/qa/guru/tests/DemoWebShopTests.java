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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static qa.guru.helpers.CustomApiListener.withCustomTemplates;

@Tag("demowebshop")
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
    @DisplayName("New Test 1 api lesson part 3")
    void newTest() {
        String body = "product_attribute_74_5_26=81" +
                "&product_attribute_74_6_27=83" +
                "&product_attribute_74_3_28=86" +
                "&addtocart_74.EnteredQuantity=1";
        String authCookieValue = "1625D415723F7B5F4F5574AA360892064D3739D8B35FD39A2E3C6C37047832" +
                "A2A86961112A53EE8AC9EAB004A768051A37EFC4F13FAAB8D76A4A8AC12F31ED747FDFF91EEAF042A494538C8D9084C38111" +
                "A6EBA20BC69C6765B98009E0A2690D234BF53DA3BA753B5A0A6A98AF0EAFB2A8503A84D3FBB3F32E519E519F6EA07B8697CB" +
                "BE6EFFB5803BEEE1CA431BFC1D";
        step("", () -> {
            String cartSize = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .body(body)
                    .cookie(authCookieName, authCookieValue)
                    .log().all()
                    .when()
                    .post("/addproducttocart/details/74/1")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("message", containsString("The product has been added to your"))
                    .body("message", is("The product has been added to your \u003ca " +
                            "href=\"/cart\"\u003eshopping cart\u003c/a\u003e"))
                    .body("updatetopcartsectionhtml", notNullValue())
                    .body("updateflyoutcartsectionhtml", notNullValue())
                    .extract().path("updatetopcartsectionhtml");

            Integer cardSizeInt = Integer.valueOf(cartSize.replaceAll("[()]", ""));

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
            step("Open main page", () ->
                    open("/"));

            step("Verify card size", () ->
                    $("#topcartlink .cart-qty").shouldHave(text(cartSize)));
        });
    }


    @Test
    @DisplayName("New Test 2 api lesson part 3")
    void newTestWithDynamicCookie() {

        step("", () -> {
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

            String body = "product_attribute_74_5_26=81" +
                    "&product_attribute_74_6_27=83" +
                    "&product_attribute_74_3_28=86" +
                    "&addtocart_74.EnteredQuantity=1";

            String cartSize = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .body(body)
                    .cookie(authCookieName, authCookieValue)
                    .log().all()
                    .when()
                    .post("/addproducttocart/details/74/1")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("message", containsString("The product has been added to your"))
                    .body("message", is("The product has been added to your \u003ca " +
                            "href=\"/cart\"\u003eshopping cart\u003c/a\u003e"))
                    .body("updatetopcartsectionhtml", notNullValue())
                    .body("updateflyoutcartsectionhtml", notNullValue())
                    .extract().path("updatetopcartsectionhtml");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to browser", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
            step("Open main page", () ->
                    open("/"));

            step("Verify card size", () ->
                    $("#topcartlink .cart-qty").shouldHave(text(cartSize)));
        });
    }

    @Test
    @DisplayName("New Test 3 api lesson part 3")
    void newTestAsNewUser() {

            String body = "product_attribute_74_5_26=81" +
                    "&product_attribute_74_6_27=83" +
                    "&product_attribute_74_3_28=86" +
                    "&addtocart_74.EnteredQuantity=1";

            given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .body(body)
                    .log().all()
                    .when()
                    .post("/addproducttocart/details/74/1")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("message", is("The product has been added to your " +
                            "\u003ca href=\"/cart\"\u003eshopping cart\u003c/a\u003e"))
                    .body("updateflyoutcartsectionhtml", notNullValue())
                    .body("updatetopcartsectionhtml", is("(1)"));
    }
}
