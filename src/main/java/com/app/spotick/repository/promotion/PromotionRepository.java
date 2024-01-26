package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionBoard, Long> {
//    행사 등록 (기본 save() 사용)
//    행사 상세보기
//    행사 리스트(페이징)
//    행사 수정
//    행사 삭제
    @Query("select new com.app.spotick.domain.dto.promotion.PromotionListDto(" +
            "p.id, p.title, p.subTitle, p.promotionCategory, f" +
            ") " +
            "from PromotionBoard p left join PromotionFile f on p.id = f.promotionBoard.id")
    Page<PromotionListDto> findListWithPage(Pageable pageable);
}
