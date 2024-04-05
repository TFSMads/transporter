package ml.volder.unikapi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedClient {
    String clientBrand();
    String otherVersion() default "*";
    String[] minecraftVersion();
}
