package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceBookmark;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long>, PlaceBookmarkQDSLRepository {
    Optional<PlaceBookmark> findByPlaceAndUser(Place place, User user);
}
