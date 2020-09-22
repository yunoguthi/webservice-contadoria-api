package br.com.jfsp.nuit.contadoria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.ERole;
import br.com.jfsp.nuit.contadoria.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
