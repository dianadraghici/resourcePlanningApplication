package dania.app.web.security;

import org.springframework.beans.factory.annotation.Value;

public class UserAuthenticationProviderFR extends UserAuthenticationProvider {

    @Value("${ldap.base.fr}")
    private String ldapBaseFR;

    @Override
    public String getLdapBase() {
        return this.ldapBaseFR;
    }
}
