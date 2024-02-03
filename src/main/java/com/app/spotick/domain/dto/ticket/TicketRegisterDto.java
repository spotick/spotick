package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import com.app.spotick.domain.type.ticket.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TicketRegisterDto {
    private Long ticketId;
    private Long userId;
    @NotBlank(message = "티켓팅 제목은 필수 입력 사항입니다")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String title;

    @NotBlank(message = "행사 부 제목은 필수 입력사항입니다")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String content;

    @NotBlank(message = "시작날짜는 필수 입력사항입니다")
    private LocalDate startDate;

    @NotBlank(message = "마지막날짜는 필수 입력사항입니다")
    private LocalDate endDate;

    @NotBlank(message = "은행이름을 설정해 주세요")
    private String bankName;
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String accountNumber; //계좌번호
    @NotBlank(message = "예금주를 정확히 입력해주세요")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String accountHolder; //예금주

    @NotBlank(message = "주소는 필수 입력사항입니다")
    private String placeAddress;

    @NotBlank(message = "주소 및 상세주소는 필수 입력사항입니다")
    private String placeAddressDetail;
    @NotNull(message = "카테고리를 선택해주세요")
    private TicketCategory category;
    @NotNull(message = "지도에 장소의 위치를 찍어주세요")
    private Double placeLat;
    @NotNull(message = "지도에 장소의 위치를 찍어주세요")
    private Double placeLng;

    private MultipartFile ticketFile;

//    @Size(min = 1, message = "티켓 등급은 한가지 이상 작성해주세요")
    private List<TicketGrade> ticketGrades = new ArrayList<>();


    //    조회수 제외됨
    public Ticket toEntity(){
        return Ticket.builder()
                .id(ticketId)
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .bankName(bankName)
                .accountNumber(accountNumber)
                .accountHolder(accountHolder)
                .ticketCategory(category)
                .ticketEventAddress(new PostAddress(placeAddress,placeAddressDetail))
                .lat(placeLat)
                .lng(placeLng)
                .build();
    }
}
