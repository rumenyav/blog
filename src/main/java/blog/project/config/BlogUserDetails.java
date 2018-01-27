package blog.project.config;
import antlr.StringUtils;
import blog.project.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class BlogUserDetails extends User implements UserDetails {
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    private ArrayList<String> roles;
    private User user;


    public BlogUserDetails(User user, ArrayList<String> roles)
    {
        super(user.getEmail(), user.getFullName(), user.getPassword());

        this.roles = roles;
        this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        String userRoles = org.springframework.util.StringUtils.collectionToCommaDelimitedString(this.roles);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userRoles);
    }

    @Override
    public String getUsername() {
        return null;
    }

    public User getUser()
    {
        return this.user;
    }
}



