package org.example.baitap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.baitap.model.enums.MovieType;

@Data
public class CreateMovieRequest {
    @NotBlank(message = "Movie Name is required")
    private String name;

    @NotNull(message = "Type Movie Name is required")
    private MovieType movieType;
}
