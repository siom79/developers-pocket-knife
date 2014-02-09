package developers.pocket.knife.config;

import jb5n.api.MessageResource;

@MessageResource(resourceBundleName = "configuration")
public interface Configuration {

    String version();

    String timestamp();
}
