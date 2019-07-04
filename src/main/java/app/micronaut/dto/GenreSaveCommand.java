package app.micronaut.dto;

import javax.validation.constraints.NotBlank;

public class GenreSaveCommand {

    @NotBlank
    private String name;

    public GenreSaveCommand() {
    }

    public GenreSaveCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}