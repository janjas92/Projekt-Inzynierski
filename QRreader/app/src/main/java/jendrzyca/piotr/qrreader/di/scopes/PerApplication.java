package jendrzyca.piotr.qrreader.di.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Piotr Jendrzyca on 09.12.2016.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerApplication {
}
