package app.micronaut.repository;



import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import app.micronaut.domain.Genre;
import app.micronaut.dto.SortingAndOrderArguments;

public interface GenreRepository {

    Optional<Genre> findById(@NotNull Long id);

    Genre save(@NotBlank String name);

    void deleteById(@NotNull Long id);

    List<Genre> findAll(@NotNull SortingAndOrderArguments args);

    int update(@NotNull Long id, @NotBlank String name);
}
