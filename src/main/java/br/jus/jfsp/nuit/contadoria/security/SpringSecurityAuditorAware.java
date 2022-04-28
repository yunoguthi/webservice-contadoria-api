package br.jus.jfsp.nuit.predial.security;

import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
//import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
//import org.keycloak.KeycloakSecurityContext;
//import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
//import org.keycloak.representations.AccessToken;
import java.util.Optional;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Autowired
    private HttpServletRequest request;

    public Optional<String> getCurrentAuditor() {

        return Optional.ofNullable(Consts.SERVICO);
    }

}
