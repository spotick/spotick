package com.app.spotick.security.principal;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.type.user.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {

    private final UserDetailsDto user;
    private final List<UserAuthority> userAuthorities;

    public PrincipalDetails(UserDetailsDto userDetailsDto, List<UserAuthority> userAuthorities) {
        this.user = userDetailsDto;
        this.userAuthorities = userAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        userAuthorities.forEach(auth -> grantedAuthorities.add(
                (GrantedAuthority) () -> auth.getAuthorityType().name())
        );

        return grantedAuthorities;
    }

    public Long getId(){
        return user.getId();
    }
    public String getEmail() {
        return user.getEmail();
    }
    public String getNickName(){
        return user.getNickName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정이 만료되지 않았는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠긴건 아닌지?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 credential 이 만료된건 아닌지?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화 되었는지?
    @Override
    public boolean isEnabled() {
        return user.getUserStatus()==UserStatus.ACTIVATE;
    }


}
