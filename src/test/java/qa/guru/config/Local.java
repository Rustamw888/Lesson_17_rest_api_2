package qa.guru.config;

import org.aeonbits.owner.ConfigFactory;

public class Local {
    public static ILocalConfig config = ConfigFactory.create(ILocalConfig.class, System.getProperties());

    public static boolean isRemoteWebDriver() {
        return !config.remoteDriverUrl().equals("");
    }

    public static boolean isVideoOn() {
        return !config.videoStorage().equals("");
    }
}
