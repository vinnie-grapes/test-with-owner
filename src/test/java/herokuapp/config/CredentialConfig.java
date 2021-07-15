package herokuapp.config;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadType.MERGE;

@Config.LoadPolicy(MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/сredentials.properties"
})

public interface CredentialConfig extends Config {

    @DefaultValue("selenoid.autotests.cloud")
    @Key("selenoid.domain")
    String getSelenoidURL();

    @DefaultValue("user1")
    @Key("selenoid.user")
    String remoteWebUser();

    @DefaultValue("1234")
    @Key("selenoid.password")
    String remoteWebPassword();
}
