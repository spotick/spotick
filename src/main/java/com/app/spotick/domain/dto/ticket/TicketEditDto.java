package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.dto.ticket.grade.TicketGradeRegisterDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
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
public class TicketEditDto {

    private Long ticketId;
    private Long userId;
    @NotBlank(message = "티켓팅 제목은 필수 입력 사항입니다.")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력사항입니다.")
    @Size(max = 2000, message = "입력한 글자수가 너무 많습니다.")
    private String content;

    private LocalDate startDate;

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

    // 전송용 필드
    private Long ticketFileId;
    private String fileName;
    private String uuid;
    private String uploadPath;

    // 새로 추가될 사진이 있을 경우 사용
    private MultipartFile newTicketFile;

    // readOnly, 수정 불가능 필드.
    private List<TicketGradeRegisterDto> ticketGrades = new ArrayList<>();

    // QueryDSL 생성자
    public TicketEditDto(Long ticketId, Long userId, String title, String content, LocalDate startDate, LocalDate endDate, String bankName, String accountNumber, String accountHolder, String placeAddress, String placeAddressDetail, TicketRatingType ticketRatingType, TicketCategory category, Double placeLat, Double placeLng, Long ticketFileId, String fileName, String uuid, String uploadPath, List<TicketGradeRegisterDto> ticketGrades) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.placeAddress = placeAddress;
        this.placeAddressDetail = placeAddressDetail;
        this.ticketRatingType = ticketRatingType;
        this.category = category;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
        this.ticketFileId = ticketFileId;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.ticketGrades = ticketGrades;
    }

    // 티켓 파일만 따로 받아 저장
    public Ticket toEntity(TicketFile ticketFile) {
        return Ticket.builder()
                .title(title)
                .content(content)
                .bankName(bankName)
                .accountNumber(accountNumber)
                .accountHolder(accountHolder)
                .ticketEventAddress(new PostAddress(placeAddress, placeAddressDetail))
                .ticketRatingType(ticketRatingType)
                .ticketCategory(category)
                .lat(placeLat)
                .lng(placeLng)
                .ticketFile(ticketFile)
                .ticketEventStatus(PostStatus.MODIFICATION_REQUESTED)
                .build();
    }

    public Ticket toEntity(User user, TicketFile ticketFile, LocalDate startDate, LocalDate endDate) {
        return Ticket.builder()
                .title(title)
                .content(content)
                .bankName(bankName)
                .accountNumber(accountNumber)
                .accountHolder(accountHolder)
                .ticketEventAddress(new PostAddress(placeAddress, placeAddressDetail))
                .ticketRatingType(ticketRatingType)
                .ticketCategory(category)
                .lat(placeLat)
                .lng(placeLng)
                .ticketFile(ticketFile)
                .user(user)
                .startDate(startDate)
                .endDate(endDate)
                .ticketEventStatus(PostStatus.MODIFICATION_REQUESTED)
                .build();
    }
}
