package qa.guru.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.config.Local;
import qa.guru.helpers.AllureAttachments;
import qa.guru.helpers.DriverSettings;
import qa.guru.helpers.DriverUtils;

import static com.codeborne.selenide.Selenide.closeWebDriver;

@ExtendWith({AllureJunit5.class})
public class TestBase {

    @BeforeAll
    static void beforeAll() {
        DriverSettings.configure();
    }

    @BeforeEach
    public void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

//    @AfterEach
//    public void afterEach() {
//        String sessionId = DriverUtils.getSessionId();
//        AllureAttachments.addScreenshotAs("Last screenshot");
//        AllureAttachments.addPageSource();
//        AllureAttachments.addBrowserConsoleLogs();
//        Selenide.closeWebDriver();
//        if (Local.isVideoOn()) {
//            AllureAttachments.addVideo(sessionId);
//        }
//    }
@AfterEach
void afterEach() {
    AllureAttachments.screenshotAs("Last screenshot");
    AllureAttachments.pageSource();
    AllureAttachments.browserConsoleLogs();

        AllureAttachments.addVideo();

    closeWebDriver();
}
}
