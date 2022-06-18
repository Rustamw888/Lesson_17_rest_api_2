package qa.guru.config;

import org.aeonbits.owner.ConfigFactory;

public class Remote {

    public static IRemoteConfig config = ConfigFactory.create(IRemoteConfig.class, System.getProperties());
}
