package br.com.jfsp.nuit.contadoria.controllers;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditAwareImpl implements AuditorAware <Long> {

    @Override
    public Optional <Long> getCurrentAuditor() {
    	//ApplicationUser principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //return Optional.of(principal.getId());
        return Optional.of(new Long(0L));
    }
}
