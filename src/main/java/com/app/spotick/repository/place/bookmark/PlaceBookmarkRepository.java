package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceBookmark;
import com.app.spotick.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long>, PlaceBookmarkQDSLRepository {
    Optional<PlaceBookmark> findByPlaceAndUser(Place place, User user);

    @Query("select count(b) from PlaceBookmark b where b.user = :user")
    int getCountOfBookmarkedPlaceByUserId(@Param("user") User user);

    @Modifying
    @Query("UPDATE PlaceBookmark pb SET pb.place = :changedPlace WHERE pb.place = :originalPlace")
    void bulkUpdateBookmarkPlace(@Param("originalPlace")Place originalPlace,@Param("changedPlace")Place changedPlace);

    @Modifying
    @Query("DELETE FROM PlaceBookmark b WHERE b.place = :place and b.user = :user")
    void deleteByPlaceAndUser(@Param("place") Place place, @Param("user") User user);

}
