package org.example.baitap.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeMovieRequest {
    @NotNull(message = "Movie ID is required")
    private UUID movieId;

    @NotNull(message = "User ID is required")
    private UUID userId;
}
