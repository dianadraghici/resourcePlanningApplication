package dania.app.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class UserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Value("${ldap.url}")
    private String ldapUrl;

    public abstract String getLdapBase();

    @Override
    public void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    public UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setBase(getLdapBase());
        contextSource.setUrl(ldapUrl);
        contextSource.setUserDn("CN=" + username + "," + getLdapBase());
        contextSource.setPassword((String) usernamePasswordAuthenticationToken.getCredentials());
        contextSource.afterPropertiesSet();

        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);

        User user = ldapTemplate.findOne(LdapQueryBuilder.query().where("cn").is(username), User.class);
        RestUserDetails restUserDetails = new RestUserDetails(user);
        return restUserDetails;
    }
}
