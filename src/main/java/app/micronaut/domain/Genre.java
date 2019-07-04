package app.micronaut.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.leangen.graphql.annotations.GraphQLQuery;

@Entity
@Table(name = "genre")
public class Genre {

    public Genre() {}

    public Genre(@NotNull String name) {
        this.name = name;
    }

    @Id @GraphQLQuery
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull @GraphQLQuery
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "genre")
    private Set<Book> books = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Genre [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", books=");
        builder.append(books);
        builder.append("]");
        return builder.toString();
    }
}
