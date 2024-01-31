package com.app.spotick.repository.place.Review;

import com.app.spotick.domain.entity.place.PlaceReview;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class PlaceReviewRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlaceReviewRepository placeReviewRepository;
    @Autowired
    private UserRepository userRepository;

    User user;
    PlaceReview review;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .build();
        userRepository.save(user);

        review = PlaceReview.builder()
                .score(4)
                .content("내용")
                .user(user)
                .build();
        placeReviewRepository.save(review);
    }

    @Test
    void findByIdAndUser() {
        PlaceReview placeReview = placeReviewRepository.findByIdAndUser(review.getId(), user).orElseThrow(
                NoSuchElementException::new
        );

        System.out.println("placeReview = " + placeReview);
    }
}