package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.dto.ticket.grade.TicketGradeRegisterDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.type.ticket.TicketCategory;
import com.app.spotick.domain.type.ticket.TicketRatingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TicketRegisterDto {

    private Long ticketId;
    private Long userId;
    @NotBlank(message = "티켓팅 제목은 필수 입력 사항입니다.")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력사항입니다.")
    @Size(max = 2000, message = "입력한 글자수가 너무 많습니다.")
    private String content;

    @NotNull(message = "날짜를 정확히 선택해주세요.")
    private LocalDate startDate;

    @NotNull(message = "날짜를 정확히 선택해주세요.")
    private LocalDate endDate;

    private String bankName;
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다.")
    private String accountNumber; //계좌번호
    @NotBlank(message = "예금주를 정확히 입력해주세요.")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다.")
    private String accountHolder; //예금주

    @NotBlank(message = "주소는 필수 입력사항입니다.")
    private String placeAddress;
    @NotBlank(message = "주소 및 상세주소는 필수 입력사항입니다.")
    private String placeAddressDetail;

    private TicketRatingType ticketRatingType;

    private TicketCategory category;
    @NotNull(message = "지도에 장소의 위치를 찍어주세요.")
    private Double placeLat;
    @NotNull(message = "지도에 장소의 위치를 찍어주세요.")
    private Double placeLng;

    private MultipartFile ticketFile;

    private List<TicketGradeRegisterDto> ticketGrades = new ArrayList<>();


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
                .ticketRatingType(ticketRatingType)
                .ticketCategory(category)
                .ticketEventAddress(new PostAddress(placeAddress,placeAddressDetail))
                .lat(placeLat)
                .lng(placeLng)
                .build();
    }
}
