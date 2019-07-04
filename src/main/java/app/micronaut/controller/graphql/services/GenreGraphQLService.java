package app.micronaut.controller.graphql.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import app.micronaut.controller.graphql.instrumentation.GraphQLService;
import app.micronaut.domain.Book;
import app.micronaut.domain.Genre;
import app.micronaut.security.AuthenticationProvider;
import app.micronaut.security.instrumentation.Secured;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.micronaut.spring.tx.annotation.Transactional;
import io.micronaut.validation.Validated;

/**
 * {@link Genre} {@link GraphQLService}
 */
@SuppressWarnings("unchecked")
@Transactional
@GraphQLService
@Validated
public class GenreGraphQLService {

    @PersistenceContext
    private EntityManager entityManager;

    @GraphQLQuery
    public List<Genre> genreList() {
        return entityManager.createQuery("from Genre").getResultList();
    }

    @GraphQLQuery
    public Optional<Genre> genre(@GraphQLArgument(name = "id") long id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @GraphQLQuery
    public List<Book> books(@GraphQLContext Genre genre, @GraphQLArgument(name = "limit") long limit) {
        return genre.getBooks().stream().limit(limit).collect(Collectors.toList());
    }

    @GraphQLMutation
    @Secured(AuthenticationProvider.ROLE_ADMIN)
    public Genre genreCreate(@NotNull @GraphQLArgument(name = "name") String name) {
        Genre genre = new Genre();
        genre.setName(name);
        entityManager.persist(genre);
        return genre;
    }

}