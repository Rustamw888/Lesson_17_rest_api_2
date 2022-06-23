package qa.guru.config;

import org.aeonbits.owner.ConfigFactory;

public class Local {

    public static ILocalConfig config = ConfigFactory.create(ILocalConfig.class, System.getProperties());
}
