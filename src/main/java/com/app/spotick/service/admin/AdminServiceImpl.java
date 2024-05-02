package com.app.spotick.service.admin;

import com.app.spotick.api.dto.admin.AdminPostApproveDto;
import com.app.spotick.api.dto.admin.AdminPostSearchDto;
import com.app.spotick.api.dto.admin.AdminUserAuthorityConfigDto;
import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.api.dto.user.UserStatusDto;
import com.app.spotick.domain.dto.admin.AdminPostListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceModifyRequest;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketModifyRequest;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.type.post.PostModifyStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.AuthorityType;
import com.app.spotick.repository.admin.place.AdminPlaceRepository;
import com.app.spotick.repository.admin.ticket.AdminTicketRepository;
import com.app.spotick.repository.admin.user.AdminUserRepository;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.place.inquiry.PlaceInquiryRepository;
import com.app.spotick.repository.place.modifyRequest.PlaceModifyReqRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.ticket.grade.TicketGradeRepository;
import com.app.spotick.repository.ticket.inquiry.TicketInquiryRepository;
import com.app.spotick.repository.ticket.like.TicketLikeRepository;
import com.app.spotick.repository.ticket.modifyRequest.TicketModifyReqRepository;
import com.app.spotick.repository.ticket.order.TicketOrderRepository;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminUserRepository adminUserRepository;
    private final AdminTicketRepository adminTicketRepository;
    private final AdminPlaceRepository adminPlaceRepository;
    private final PlaceModifyReqRepository placeModifyReqRepository;
    private final TicketModifyReqRepository ticketModifyReqRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceInquiryRepository placeInquiryRepository;
    private final PlaceReservationRepository placeReservationRepository;
    private final UserAuthorityRepository userAuthorityRepository;
//    private final TicketInquiryRepository ticketInquiryRepository;
//    private final TicketLikeRepository ticketLikeRepository;
//    private final TicketOrderRepository ticketOrderRepository;
//    private final TicketGradeRepository ticketGradeRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    @Override
    @Transactional(readOnly = true)
    public Slice<AdminUserListDto> findAdminUserList(Pageable pageable, AdminUserSearchDto userSearchDto){
        return adminUserRepository.findAdminUserList(pageable,userSearchDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<AdminPostListDto> findAdminPlaceList(Pageable pageable, AdminPostSearchDto placeSearchDto) {
        return adminPlaceRepository.findAdminPlaceList(pageable,placeSearchDto);
    }

    @Override
    public void updateUsersStatus(List<UserStatusDto> userStatusDtoList) {
        for (UserStatusDto userStatusDto : userStatusDtoList){
            userService.updateUserStatus(userStatusDto);
        }
    }

    @Override
    public void approveOrRejectPlace(AdminPostApproveDto.Request approveDto) {
        switch (approveDto.getStatus()){
            case REGISTRATION_PENDING -> {
                Place place = adminPlaceRepository.findById(approveDto.getPostId())
                        .orElseThrow(() -> new IllegalStateException("존재하지 않는 장소 id"));
                PostStatus changedStatus = approveDto.getIsApprove()?
                        PostStatus.APPROVED:PostStatus.REJECTED;
                place.updatePlaceStatus(changedStatus);
            }
            case MODIFICATION_REQUESTED ->{
                handlePlaceModification(approveDto);
            }
        }
    }

    private void handlePlaceModification(AdminPostApproveDto.Request approveDto){
        Place reqPlace = adminPlaceRepository.getReferenceById(approveDto.getPostId());

        PlaceModifyRequest modifyRequest = placeModifyReqRepository.findByChangedPlace(reqPlace)
                .orElseThrow(() -> new IllegalStateException("잘못된 장소 정보"));
        Place changedPlace = modifyRequest.getChangedPlace();
        Place originalPlace = modifyRequest.getOriginalPlace();

//        거절된 경우 먼저 처리후 early return
        if(!approveDto.getIsApprove()){
            changedPlace.updatePlaceStatus(PostStatus.REJECTED);
            originalPlace.updatePlaceStatus(PostStatus.APPROVED);
            modifyRequest.setPlaceModifyStatus(PostModifyStatus.REJECTED);
            return ;
        }

//       생성일 조회수, 북마크, 문의, 예약 테이블의 장소들을 모두 기존 장소에서 바뀔 장소로 변경
        adminPlaceRepository.updateCreatedDateWithOriginal(originalPlace.getCreatedDate(),changedPlace.getId());
        placeInquiryRepository.bulkUpdateInquiryPlace(originalPlace,changedPlace);
        placeBookmarkRepository.bulkUpdateBookmarkPlace(originalPlace,changedPlace);
        placeReservationRepository.bulkUpdateReservationPlace(originalPlace,changedPlace);

        changedPlace.setViewCount(originalPlace.getViewCount());
        changedPlace.updatePlaceStatus(PostStatus.APPROVED);
        originalPlace.updatePlaceStatus(PostStatus.REPLACED);
        modifyRequest.setPlaceModifyStatus(PostModifyStatus.APPROVED);
    }

    @Override
    public void grantOrRevokeUserAuthority(AdminUserAuthorityConfigDto configDto) {
        User targetUser = userRepository.getReferenceById(configDto.getUserId());
        if(configDto.getIsGranted()){
//            관리자 권한 부여인 경우
            userAuthorityRepository.save(UserAuthority.builder()
                    .authorityType(AuthorityType.ROLE_ADMIN)
                    .user(targetUser)
                    .build());
            return;
        }
//            관리자 권한 해제인 경우
        userAuthorityRepository.delete(userAuthorityRepository
                .findByUserIdAndAuthorityType(configDto.getUserId(), AuthorityType.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("이미 관리자권한을 가지고 있지 않습니다")));
    }

    @Override
    public Slice<AdminPostListDto> findAdminTicketList(Pageable pageable,AdminPostSearchDto ticketSearchDto) {
        return adminTicketRepository.findAdminTicketList(pageable,ticketSearchDto);
    }

    @Override
    public void approveOrRejectTicket(AdminPostApproveDto.Request approveDto) {
        switch (approveDto.getStatus()){
            case REGISTRATION_PENDING -> {
                Ticket ticket = adminTicketRepository.findById(approveDto.getPostId())
                        .orElseThrow(() -> new IllegalStateException("존재하지 않는 장소 id"));
                PostStatus changedStatus = approveDto.getIsApprove()?
                        PostStatus.APPROVED:PostStatus.REJECTED;
                ticket.setTicketEventStatus(changedStatus);
            }
            case MODIFICATION_REQUESTED ->{
                handleTicketModification(approveDto);
            }
        }
    }

    private void handleTicketModification(AdminPostApproveDto.Request approveDto){
        Ticket reqTicket = adminTicketRepository.getReferenceById(approveDto.getPostId());

        TicketModifyRequest modifyRequest = ticketModifyReqRepository.findByChangedTicket(reqTicket)
                .orElseThrow(() -> new IllegalStateException("잘못된 장소 정보"));

        Ticket changedTicket = modifyRequest.getChangedTicket();
        Ticket originalTicket = modifyRequest.getOriginalTicket();

//        거절된 경우 먼저 처리후 early return
        if(!approveDto.getIsApprove()){
            changedTicket.setTicketEventStatus(PostStatus.REJECTED);
            originalTicket.setTicketEventStatus(PostStatus.APPROVED);
            modifyRequest.setTicketModifyStatus(PostModifyStatus.REJECTED);
            return ;
        }

//       생성일, 조회수, 좋아요, 문의, 주문, 등급 테이블의 티켓들을 모두 기존 티켓에서 바뀔 티켓로 변경
//        adminTicketRepository.updateCreatedDateWithOriginal(originalTicket.getCreatedDate(),changedTicket.getId());
//        ticketInquiryRepository.bulkUpdateInquiryTicket(originalTicket,changedTicket);
//        ticketLikeRepository.bulkUpdateLikeTicket(originalTicket,changedTicket);
//        ticketOrderRepository.bulkUpdateOrderTicket(originalTicket,changedTicket);
//        ticketGradeRepository.bulkUpdateGradeTicket(originalTicket,changedTicket);

        changedTicket.setTicketEventStatus(PostStatus.DELETED);
        originalTicket.replaceTicket(changedTicket);
        originalTicket.setTicketEventStatus(PostStatus.APPROVED);
        modifyRequest.setTicketModifyStatus(PostModifyStatus.APPROVED);
    }


}



















