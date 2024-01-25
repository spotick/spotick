package com.app.spotick.repository.place;

import com.app.spotick.domain.entity.place.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

//    @Query("""
//        select new com.app.spotick.domain.dto.place.PlaceReviewListDto(
//            pr.id,pr.content,pr.score,pr.place.id,u.id,u.nickName,pr.createdDate
//        ) from PlaceReview pr join fetch pr.user u
//        where pr.place.id =:placeId
//    """)
//    Slice<PlaceReview> findReviewListByPlaceId(@Param("placeId") Long placeId, Pageable pageable);

}












