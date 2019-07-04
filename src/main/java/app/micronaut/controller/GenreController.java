package app.micronaut.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import app.micronaut.domain.Genre;
import app.micronaut.dto.GenreSaveCommand;
import app.micronaut.dto.GenreUpdateCommand;
import app.micronaut.dto.SortingAndOrderArguments;
import app.micronaut.repository.GenreRepository;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;

@Validated
@Controller("/genres")
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Get("/{id}")
    public Genre show(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Put("/")
    public HttpResponse update(@Body @Valid GenreUpdateCommand command) {
        int numberOfEntitiesUpdated = genreRepository.update(command.getId(), command.getName());

        return HttpResponse.noContent().header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    @Get(value = "/list{?args*}")
    public List<Genre> list(@Valid SortingAndOrderArguments args) {
        return genreRepository.findAll(args);
    }

    @Post("/")
    public HttpResponse<Genre> save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.getName());

        return HttpResponse.created(genre).headers(headers -> headers.location(location(genre.getId())));
    }

    @Delete("/{id}")
    public HttpResponse delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    protected URI location(Long id) {
        return URI.create("/genres/" + id);
    }

    protected URI location(Genre genre) {
        return location(genre.getId());
    }
}
