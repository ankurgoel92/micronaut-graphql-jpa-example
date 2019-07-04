package app.micronaut.controller.graphql.services;

import java.util.Collections;

import javax.inject.Inject;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.SecurityService;
import app.micronaut.controller.graphql.instrumentation.GraphQLService;

/**
 * {@link GraphQLService} returning information about currently logged-in user.
 *
 */
@GraphQLService
public class AuthGraphQLService {

        @Inject protected SecurityService securityService;

        @GraphQLQuery
        public Object authUser() {
                return securityService.getAuthentication().map(Authentication::getAttributes).orElse(Collections.emptyMap());
        }

}