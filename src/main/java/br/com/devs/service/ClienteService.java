package br.com.devs.service;

import br.com.devs.service.dto.ClienteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.devs.domain.Cliente}.
 */
public interface ClienteService {
    /**
     * Save a cliente.
     *
     * @param clienteDTO the entity to save.
     * @return the persisted entity.
     */
    ClienteDTO save(ClienteDTO clienteDTO);

    /**
     * Updates a cliente.
     *
     * @param clienteDTO the entity to update.
     * @return the persisted entity.
     */
    ClienteDTO update(ClienteDTO clienteDTO);

    /**
     * Partially updates a cliente.
     *
     * @param clienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClienteDTO> partialUpdate(ClienteDTO clienteDTO);

    /**
     * Get all the clientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClienteDTO> findAll(Pageable pageable);

    /**
     * Get all the clientes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClienteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cliente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClienteDTO> findOne(Long id);

    /**
     * Delete the "id" cliente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
