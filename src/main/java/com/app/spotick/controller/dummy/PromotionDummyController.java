package com.app.spotick.controller.dummy;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import com.app.spotick.domain.type.ticket.TicketCategory;
import com.app.spotick.domain.type.ticket.TicketRatingType;
import com.app.spotick.repository.promotion.PromotionRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PromotionDummyController {
    private final UserRepository userRepository;
    private final PromotionRepository promotionRepository;

//    @PostConstruct
    public void createPromotionDummy() {
        User user = userRepository.findById(1L).get();

        List<PromotionBoard> contents = new ArrayList<>();

        for (int i = 0; i < 70; i++) {
            PromotionBoard entity = PromotionBoard.builder()
                    .title("제목" + i)
                    .subTitle("서브제목" + i)
                    .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin auctor dictum posuere. Nunc venenatis, ipsum a fringilla viverra, risus erat efficitur purus, ac laoreet diam neque non ligula. Sed semper ut quam at posuere. In quis enim augue. Ut scelerisque, orci quis maximus commodo, lectus magna ultricies sem, ut tincidunt augue est eget sapien. Donec elementum, nulla quis sollicitudin tempor, erat risus condimentum ipsum, id dignissim ligula sapien et dui. Aenean ut suscipit libero.\n" +
                             "\n" +
                             "Sed nec lacus non mi varius faucibus in et erat. Maecenas molestie ipsum eu augue lobortis, ut maximus lorem cursus. Sed ullamcorper quis nisi vitae euismod. Donec dictum, sapien in luctus tincidunt, dui tellus efficitur arcu, ut rhoncus dolor justo et ligula. Etiam scelerisque purus nunc, ut iaculis sem ultrices eget. Aliquam ultricies accumsan vulputate. Pellentesque nec lectus porttitor, dapibus nisl vitae, efficitur purus. Nam eget nisi scelerisque, fermentum tortor a, suscipit nulla. Fusce elementum porta ex, quis egestas mauris. Fusce sed sagittis elit, vehicula fringilla sapien.")
                    .promotionCategory(PromotionCategory.CF_FILMING)
                    .fileName("파일 제목" + i)
                    .uuid(UUID.randomUUID().toString())
                    .uploadPath("path" + i)
                    .user(user)
                    .build();
            contents.add(entity);
        }

        promotionRepository.saveAll(contents);
    }
}
