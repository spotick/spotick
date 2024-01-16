package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.entity.place.PlaceBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long>, PlaceBookmarkQDSLRepository {

}
