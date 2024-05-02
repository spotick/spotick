package com.app.spotick.service.ticket.order;

import com.app.spotick.api.dto.ticket.TicketOrderDetailDto;
import com.app.spotick.api.dto.ticket.TicketOrderDto;
import com.app.spotick.api.response.DataResponse;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import com.app.spotick.domain.entity.ticket.TicketOrder;
import com.app.spotick.domain.entity.ticket.TicketOrderDetail;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.payment.PaymentStatus;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.ticket.grade.TicketGradeRepository;
import com.app.spotick.repository.ticket.order.TicketOrderDetailRepository;
import com.app.spotick.repository.ticket.order.TicketOrderRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketOrderServiceImpl implements TicketOrderService {
    private final TicketRepository ticketRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final TicketOrderDetailRepository ticketOrderDetailRepository;
    private final TicketGradeRepository ticketGradeRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> saveTicketOrder(TicketOrderDto.Save saveDto, Long userId) {
        List<Long> gradeIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        saveDto.getTicketOrderDetailDtoList().stream()
                .sorted(Comparator.comparingLong(TicketOrderDetailDto::getGradeId))
                .forEach(detailDto -> {
                    gradeIds.add(detailDto.getGradeId());
                    quantities.add(detailDto.getQuantity());
                });
        List<Integer> prices = ticketGradeRepository.findTicketGradePriceByTicketIdAndGradeIds(saveDto.getTicketId(), gradeIds);

        // gradeId의 갯수만큼 price값을 조회해오지 못했다면 클라이언트의 id값 조작을 의심할 수 있다.
        // 제대로된 결제를 위해 검증 절차를 거친다.
        if (prices.size() != gradeIds.size()) {
            throw new NoSuchElementException("가격 조회 실패");
        }

        // 결과값 조회에 이상이 없다면 그 티켓의 갯수 만큼 곱셈하여 총가격값을 계산한다.
        Integer amount = IntStream.range(0, prices.size())
                .map(i -> prices.get(i) * quantities.get(i))
                .sum();

        // TicketOrder 엔티티를 제작하여 저장한다.
        Ticket tmpTicket = ticketRepository.getReferenceById(saveDto.getTicketId());
        User tmpUser = userRepository.getReferenceById(userId);

        TicketOrder orderEntity = TicketOrder.builder()
                .amount(amount)
                .eventDate(saveDto.getEventDate())
                .paymentMethod(saveDto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .ticket(tmpTicket)
                .user(tmpUser)
                .build();
        TicketOrder savedOrder = ticketOrderRepository.save(orderEntity);


        // TicketOrderDetail 엔티티화 시켜 저장한다.
        List<TicketOrderDetail> ticketOrderDetails = new ArrayList<>();

        saveDto.getTicketOrderDetailDtoList().forEach(detailDto -> {
            TicketGrade tmpGrade = ticketGradeRepository.getReferenceById(detailDto.getGradeId());

            ticketOrderDetails.add(
                    TicketOrderDetail.builder()
                            .quantity(detailDto.getQuantity())
                            .ticketOrder(savedOrder)
                            .ticketGrade(tmpGrade)
                            .build()
            );
        });
        ticketOrderDetailRepository.saveAll(ticketOrderDetails);


        // 여기까지 코드가 진행되었다면 이상이 없다는 의미이며 결제 정보값(ID 포함)을 리턴한다.
        TicketOrderDto.Info returnValue = ticketOrderRepository.findOrderInfoById(savedOrder.getId());

        return new ResponseEntity<>(
                DataResponse.builder()
                        .success(true)
                        .message("티켓 결제정보 저장")
                        .data(returnValue)
                        .build(),
                HttpStatus.OK
        );
    }

    @Override
    public void declineOrder(Long orderId, PaymentStatus paymentStatus) {
//        ticketOrderDetailRepository.deleteAllByTicketOrderId(orderId);

        TicketOrder foundOrder = ticketOrderRepository.findById(orderId).orElseThrow(
                NoSuchElementException::new
        );
        foundOrder.updatePaymentStatus(PaymentStatus.DECLINED);
    }
}
