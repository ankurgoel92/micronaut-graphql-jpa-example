package app.micronaut.controller.graphql.instrumentation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Singleton;

/**
 * All beans with this annotation represent the GraphQL service which handles the GraphQL fields.
 *
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Singleton
public @interface GraphQLService {
}