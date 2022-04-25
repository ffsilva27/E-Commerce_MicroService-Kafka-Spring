package br.com.letscode.user.repository;

import br.com.letscode.user.model.Authority;
import br.com.letscode.user.model.AuthorityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, AuthorityKey> {
}
