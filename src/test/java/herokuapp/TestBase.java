package herokuapp;

import com.codeborne.selenide.Configuration;
import herokuapp.config.CredentialConfig;
import herokuapp.config.OwnerConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
import static herokuapp.helpers.AttachmentHelper.*;

public class TestBase {

    static OwnerConfig ownerConfig = ConfigFactory.create(OwnerConfig.class, System.getProperties());
    static CredentialConfig credentialConfig = ConfigFactory.create(CredentialConfig.class);

    @BeforeAll
    static void setup() {
        addListener("AllureSelenide", new AllureSelenide());
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
        Configuration.browser = ownerConfig.getWebDriverBrowser();
        Configuration.browserVersion = ownerConfig.getWebDriverBrowserVersion();

        if (Boolean.parseBoolean(System.getProperty("web.maximized", "false"))) {
            Configuration.startMaximized = ownerConfig.isMaximized();
        }

        if(ownerConfig.isRemote()) {
            Configuration.remote = String.format("https://%s:%s@%s/wd/hub/",
                    credentialConfig.remoteWebUser(),
                    credentialConfig.remoteWebPassword(),
                    credentialConfig.getSelenoidURL());
        }
    }

    @AfterEach
    void afterEach() {
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());
        if(System.getProperty("video.storage") != null)
            attachVideo();
        closeWebDriver();
    }
}