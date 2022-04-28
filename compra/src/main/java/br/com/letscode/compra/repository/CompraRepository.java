package br.com.letscode.compra.repository;

import br.com.letscode.compra.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer>, JpaSpecificationExecutor<Compra> {

    Compra findByCpf(String cpf);

}
