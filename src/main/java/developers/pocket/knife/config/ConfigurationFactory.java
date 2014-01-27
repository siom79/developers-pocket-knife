package developers.pocket.knife.config;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ConfigurationFactory {

    public enum ConfigurationKey {
        DefaultDirectory
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Configuration {
        ConfigurationKey key();
    }

    @Produces
    public String produceConfigurationValue(InjectionPoint injectionPoint) {
        Annotated annotated = injectionPoint.getAnnotated();
        Configuration annotation = annotated.getAnnotation(Configuration.class);
        if (annotation != null) {
            ConfigurationKey key = annotation.key();
            if (key != null) {
                switch (key) {
                    case DefaultDirectory:
                        return System.getProperty("user.dir");
                }
            }
        }
        throw new IllegalStateException("No key for injection point: " + injectionPoint);
    }
}
