package com.app.spotick.repository.place.inquiry;

import com.app.spotick.domain.entity.place.PlaceInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceInquiryRepository extends JpaRepository<PlaceInquiry, Long>,PlaceInquiryQDSLRepository {

}
