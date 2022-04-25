package br.com.letscode.compra.repository;

import br.com.letscode.compra.model.CompraProduto;
import br.com.letscode.compra.model.CompraProdutoKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraProdutoRepository extends JpaRepository<CompraProduto, CompraProdutoKey> {
}
