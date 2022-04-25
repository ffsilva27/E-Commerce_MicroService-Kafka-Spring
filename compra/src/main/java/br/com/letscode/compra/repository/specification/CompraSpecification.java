package br.com.letscode.compra.repository.specification;

import br.com.letscode.compra.model.Compra;
import org.springframework.data.jpa.domain.Specification;

public class CompraSpecification {

    public static Specification<Compra> filterOneByCpf(String cpf) {
        return (root, query, builder) ->
                builder.like(root.get("cpf"), cpf);
    }

}
