package dania.app.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RestUserDetails implements UserDetails {

    private Set<? super GrantedAuthority> grantedAuthorities = new HashSet<>();

    private User user;

    public RestUserDetails(User user) {
        this.user = user;
    }

    public Set<? super GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(grantedAuthorities.toArray(new GrantedAuthority[] {}));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getCn().get(0);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
