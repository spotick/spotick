package com.app.spotick.repository.admin.user;

import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import com.app.spotick.domain.dto.user.UserAuthorityDto;
import com.app.spotick.domain.type.user.AuthorityType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.app.spotick.domain.entity.user.QUser.user;
import static com.app.spotick.domain.entity.user.QUserAuthority.userAuthority;

@RequiredArgsConstructor
public class AdminUserQDSLRepositoryImpl implements AdminUserQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<AdminUserListDto> findAdminUserList(Pageable pageable, AdminUserSearchDto userSearchDto) {
        List<AdminUserListDto> userList = queryFactory.select(
                        Projections.constructor(AdminUserListDto.class,
                                user.id,
                                user.email,
                                user.nickName,
                                user.tel,
                                user.userStatus,
                                user.createdDate
                        )
                )
                .from(user)
                .where(createSearchCondition(userSearchDto))
                .orderBy(user.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (userList.size() > pageable.getPageSize()) {
            userList.remove(pageable.getPageSize());
            hasNext = true;
        }

        List<Long> idList = userList.stream().map(AdminUserListDto::getId).toList();

        List<UserAuthorityDto> authorityDtoList = queryFactory.select(
                        Projections.constructor(UserAuthorityDto.class,
                                userAuthority.user.id,
                                userAuthority.authorityType
                        )
                )
                .from(userAuthority)
                .where(userAuthority.user.id.in(idList))
                .fetch();

        Map<Long, List<UserAuthorityDto>> authorityListMap = authorityDtoList.stream().collect(Collectors.groupingBy(UserAuthorityDto::getUserId));

        userList.forEach(adminUserListDto -> {
            adminUserListDto.setAuthorityType(
                    authorityListMap.get(adminUserListDto.getId())
                            .size() == 1 ? AuthorityType.ROLE_USER : AuthorityType.ROLE_ADMIN
            );
        });

        return new SliceImpl<>(userList,pageable,hasNext);
    }

    private BooleanBuilder createSearchCondition(AdminUserSearchDto userSearchDto){
        BooleanBuilder builder = new BooleanBuilder();
        if (userSearchDto.getEmail() != null) {
            builder.and(user.email.contains(userSearchDto.getEmail()));
        }
        if (userSearchDto.getNickName() != null) {
            builder.and(user.nickName.contains(userSearchDto.getNickName()));
        }
        if (userSearchDto.getStatus() != null) {
            builder.and(user.userStatus.eq(userSearchDto.getStatus()));
        }
        return builder;
    }



}

