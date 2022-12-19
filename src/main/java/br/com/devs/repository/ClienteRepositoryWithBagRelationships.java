package br.com.devs.repository;

import br.com.devs.domain.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ClienteRepositoryWithBagRelationships {
    Optional<Cliente> fetchBagRelationships(Optional<Cliente> cliente);

    List<Cliente> fetchBagRelationships(List<Cliente> clientes);

    Page<Cliente> fetchBagRelationships(Page<Cliente> clientes);
}
