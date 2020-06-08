package dania.app.web.security;

import org.springframework.beans.factory.annotation.Value;

public class UserAuthenticationProviderCA extends UserAuthenticationProvider {

    @Value("${ldap.base.ca}")
    private String ldapBaseCA;

    @Override
    public String getLdapBase() {
        return this.ldapBaseCA;
    }
}
