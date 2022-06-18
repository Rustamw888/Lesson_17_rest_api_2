package qa.guru.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:app/remote.properties"
})
public interface IRemoteConfig extends Config {

    String webUrl();
    String apiUrl();
    String userLogin();
    String userPassword();
    @DefaultValue("chrome")
    String browser();
    @DefaultValue("101.0")
    String browserVersion();
    @DefaultValue("1920x1080")
    String browserSize();
    @DefaultValue("https://user1:1234@selenoid.autotests.cloud/wd/hub/")
    String remoteDriverUrl();
    String videoStorage();
}
