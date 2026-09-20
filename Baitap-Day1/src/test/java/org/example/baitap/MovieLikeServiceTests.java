package org.example.baitap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.example.baitap.dto.response.LikeMovieResponse;
import org.example.baitap.entity.MovieEntity;
import org.example.baitap.entity.UserEntity;
import org.example.baitap.model.enums.MovieStatus;
import org.example.baitap.model.enums.MovieType;
import org.example.baitap.repository.jpa.MovieLikeRepositoryJpa;
import org.example.baitap.repository.jpa.MovieRepositoryJpa;
import org.example.baitap.repository.jpa.UserRepositoryJpa;
import org.example.baitap.service.MovieLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

@SpringBootTest
class MovieLikeServiceTests {
    @Autowired MovieLikeService movieLikeService;
    @Autowired MovieRepositoryJpa movieRepository;
    @Autowired MovieLikeRepositoryJpa movieLikeRepository;
    @Autowired UserRepositoryJpa userRepository;

    @BeforeEach
    void cleanDatabase() {
        movieLikeRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void twoUsersLikeSameMovieWithPessimisticLock() throws Exception {
        UUID movieId = newMovie().getId();
        UUID firstUserId = newUser("first").getId();
        UUID secondUserId = newUser("second").getId();
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService workers = Executors.newFixedThreadPool(2);

        try {
            Future<LikeMovieResponse> first = workers.submit(() -> {
                start.await();
                return movieLikeService.likePessimistic(movieId, firstUserId);
            });
            Future<LikeMovieResponse> second = workers.submit(() -> {
                start.await();
                return movieLikeService.likePessimistic(movieId, secondUserId);
            });

            start.countDown();
            assertThat(List.of(
                    first.get(10, TimeUnit.SECONDS).getLikeCount(),
                    second.get(10, TimeUnit.SECONDS).getLikeCount()
            )).containsExactlyInAnyOrder(1L, 2L);
            assertThat(movieRepository.findById(movieId).orElseThrow().getLikeCount()).isEqualTo(2L);
            assertThat(movieRepository.findById(movieId).orElseThrow().getVersion()).isEqualTo(2L);
            assertThat(movieLikeRepository.count()).isEqualTo(2L);
        } finally {
            workers.shutdownNow();
        }
    }

    @Test
    void twoUsersLikeSameMovieWithOptimisticLock() {
        UUID movieId = newMovie().getId();
        UUID firstUserId = newUser("optimisticFirst").getId();
        UUID secondUserId = newUser("optimisticSecond").getId();

        movieLikeService.likeOptimistic(movieId, firstUserId);
        LikeMovieResponse second = movieLikeService.likeOptimistic(movieId, secondUserId);

        assertThat(second.getLikeCount()).isEqualTo(2L);
        assertThat(movieRepository.findById(movieId).orElseThrow().getVersion()).isEqualTo(2L);
        assertThat(movieLikeRepository.count()).isEqualTo(2L);
    }

    @Test
    void sameUserCannotLikeTwice() {
        UUID movieId = newMovie().getId();
        UUID userId = newUser("once").getId();

        movieLikeService.likePessimistic(movieId, userId);
        LikeMovieResponse repeated = movieLikeService.likePessimistic(movieId, userId);

        assertThat(repeated.getLikeCount()).isEqualTo(1L);
        assertThat(movieLikeRepository.count()).isEqualTo(1L);
        assertThat(movieRepository.findById(movieId).orElseThrow().getVersion()).isEqualTo(1L);
    }

    @Test
    void optimisticLockRejectsStaleVersion() {
        UUID movieId = newMovie().getId();
        UUID userId = newUser("optimistic").getId();
        MovieEntity staleMovie = movieRepository.findById(movieId).orElseThrow();

        movieLikeService.likeOptimistic(movieId, userId);
        staleMovie.setLikeCount(2L);

        assertThatThrownBy(() -> movieRepository.saveAndFlush(staleMovie))
                .isInstanceOf(OptimisticLockingFailureException.class);
        assertThat(movieRepository.findById(movieId).orElseThrow().getLikeCount()).isEqualTo(1L);
        assertThat(movieLikeRepository.count()).isEqualTo(1L);
    }

    private MovieEntity newMovie() {
        return movieRepository.saveAndFlush(MovieEntity.builder()
                .name("Movie for like test")
                .movieStatus(MovieStatus.ACTIVE)
                .movieType(MovieType.MOVIE)
                .build());
    }

    private UserEntity newUser(String name) {
        return userRepository.saveAndFlush(UserEntity.builder()
                .name(name)
                .email(name + "@example.test")
                .build());
    }
}
