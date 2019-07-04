package app.micronaut.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.leangen.graphql.annotations.GraphQLQuery;

@Entity
@Table(name = "book")
public class Book {

    public Book() {
    }

    public Book(@NotNull String isbn, @NotNull String name, Genre genre) {
        this.author = isbn;
        this.name = name;
        this.genre = genre;
    }

    @Id
    @GraphQLQuery
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @GraphQLQuery
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @GraphQLQuery
    @Column(name = "author", nullable = false)
    private String author;

    @ManyToOne
    private Genre genre;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Book{");
        sb.append("id=");
        sb.append(id);
        sb.append(", name='");
        sb.append(name);
        sb.append("', isbn='");
        sb.append(author);
        sb.append("', genre='");
        sb.append(genre);
        sb.append("'}");
        return sb.toString();
    }
}
