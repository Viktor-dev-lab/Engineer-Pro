package org.example.baitap.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.baitap.model.enums.MovieType;

@Data
@Builder
public class MovieResponse {
    private String name;
    private MovieType movieType;
}
