package org.example.baitap.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeMovieResponse {
    private UUID movieId;
    private long likeCount;
}
