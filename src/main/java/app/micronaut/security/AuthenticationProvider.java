package app.micronaut.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import org.reactivestreams.Publisher;

import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Flowable;

/**
 * Provides dummy users with passwords and authorities for Basic HTTP Auth.
 *
 */
@Singleton
public class AuthenticationProvider implements io.micronaut.security.authentication.AuthenticationProvider {

        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";

        public static final String[] USERS = new String[] {
                "user", "userpass", ROLE_USER,
                "admin", "adminpass", ROLE_ADMIN,
        };

        @Override
        public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
                for (int i=0; i<USERS.length; i+=3)
                        if (authenticationRequest.getIdentity().equals(USERS[i]) && authenticationRequest.getSecret().equals(USERS[i+1]))
                                return Flowable.just(new UserDetails("user", Collections.singletonList(USERS[i+2])));

                return Flowable.just(new AuthenticationFailed());
        }
        
}