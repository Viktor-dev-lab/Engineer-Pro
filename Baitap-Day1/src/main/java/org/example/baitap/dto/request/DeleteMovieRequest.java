package org.example.baitap.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteMovieRequest {
    @NotNull(message = "ID is required")
    private UUID id;
}
