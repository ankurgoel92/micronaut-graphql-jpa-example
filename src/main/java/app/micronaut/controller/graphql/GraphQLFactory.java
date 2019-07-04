package app.micronaut.controller.graphql;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.micronaut.controller.graphql.instrumentation.GraphQLService;
import app.micronaut.controller.graphql.instrumentation.GraphQLTransactionInstrumentation;
import graphql.GraphQL;
import graphql.GraphQL.Builder;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.inject.qualifiers.Qualifiers;

/**
 * Builds {@link GraphQL} instance with the schema.
 *
 */
@Factory
public class GraphQLFactory {

    public static final Logger logger = LoggerFactory.getLogger(GraphQLFactory.class);

    @Inject
    protected BeanContext beanContext;
    @Inject
    protected GraphQLTransactionInstrumentation graphQLTransactionInstrumentation;
    @Inject
    protected GraphQLInclusionStrategy graphQLInclusionStrategy;

    @Bean
    @Singleton
    public GraphQL graphQL() {
        GraphQLSchemaGenerator schemaGenerator = new GraphQLSchemaGenerator().withInclusionStrategy(graphQLInclusionStrategy);

        Collection graphQLServices = beanContext.getBeansOfType(Object.class, Qualifiers.byStereotype(GraphQLService.class));

        if (graphQLServices.isEmpty()) {
            logger.debug("No GraphQL services found, returning empty schema");
            return new Builder(GraphQLSchema.newSchema().build()).build();
        } else {
            for (Object graphQLService : graphQLServices) {
                // if the service is micronaut AOP proxy, we need to unwrap the real service class for the schema generator
                // so that it can scan it for graphql annotations and generate the schema
                Class graphQLServiceClass = graphQLService.getClass();
                if (graphQLServiceClass.getSimpleName().contains("$Intercepted"))
                    graphQLServiceClass = graphQLServiceClass.getSuperclass();

                logger.debug("Registering GraphQL service: {}", graphQLServiceClass.getSimpleName());
                schemaGenerator.withOperationsFromSingleton(graphQLService, graphQLServiceClass);
            }
        }

        return new GraphQL.Builder(schemaGenerator.generate()).instrumentation(graphQLTransactionInstrumentation).build();
    }

}