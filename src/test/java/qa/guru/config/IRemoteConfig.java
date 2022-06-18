package qa.guru.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:app/cred.properties"
})
public interface IRemoteConfig extends Config {

    String webUrl();
    String apiUrl();
    String userLogin();
    String userPassword();
}
