package app.micronaut.controller.graphql.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.micronaut.spring.tx.annotation.Transactional;
import app.micronaut.controller.graphql.instrumentation.GraphQLService;
import app.micronaut.domain.Book;

/**
 * {@link Book} {@link GraphQLService}.
 *
 */
@SuppressWarnings("unchecked")
@Transactional @GraphQLService
public class BookGraphQLService {

        @PersistenceContext private EntityManager entityManager;

        @GraphQLQuery
        public List<Book> bookList() {
                return entityManager.createQuery("from Book").getResultList();
        }

        @GraphQLQuery
        public Optional<Book> book(@GraphQLArgument(name = "id") long id) {
                return Optional.ofNullable(entityManager.find(Book.class, id));
        }

}