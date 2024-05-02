package com.app.spotick.repository.place.Review;

import com.app.spotick.domain.dto.place.review.PlaceReviewListDto;
import com.app.spotick.domain.entity.place.PlaceReview;
import com.app.spotick.domain.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long>, PlaceReviewQDSLRepository {
    Optional<PlaceReview> findByIdAndUser(Long id, User user);

    @Query("""
            select new com.app.spotick.domain.dto.place.review.PlaceReviewListDto(
               r.id, r.content, r.score, pr.place.id,u.id, u.nickName, r.createdDate
            )
            from PlaceReservation pr
            join pr.placeReview r
            join r.user u
            where pr.place.id = :placeId
            order by r.id desc
            """)
    Slice<PlaceReviewListDto> findReviewSliceByPlaceId(@Param("placeId") Long placeId, Pageable pageable);

}












