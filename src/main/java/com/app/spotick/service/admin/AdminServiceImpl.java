package com.app.spotick.service.admin;

import com.app.spotick.domain.dto.admin.AdminUserListDto;
import com.app.spotick.repository.admin.user.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminUserRepository adminUserRepository;

    public Slice<AdminUserListDto> findAdminUserList(Pageable pageable){
        return adminUserRepository.findAdminUserList(pageable);
    }


}
