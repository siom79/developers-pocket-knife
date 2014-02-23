package developers.pocket.knife.config;

import jb5n.api.JB5n;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ConfigurationFactory {

    public enum ConfigurationKey {
        DefaultDirectory, Version, BuildTimestamp, Producer
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface ConfigurationValue {
        @Nonbinding ConfigurationKey key();
    }

    @Produces
    @ConfigurationValue(key=ConfigurationKey.Producer)
    public String produceConfigurationValue(InjectionPoint injectionPoint) {
        Annotated annotated = injectionPoint.getAnnotated();
        ConfigurationValue annotation = annotated.getAnnotation(ConfigurationValue.class);
        if (annotation != null) {
            ConfigurationKey key = annotation.key();
            if (key != null) {
                switch (key) {
                    case DefaultDirectory:
                        return System.getProperty("user.dir");
                    case Version:
                        return JB5n.createInstance(Configuration.class).version();
                    case BuildTimestamp:
                        return JB5n.createInstance(Configuration.class).timestamp();
                }
            }
        }
        throw new IllegalStateException("No key for injection point: " + injectionPoint);
    }
}
