package br.com.devs.web.rest;

import br.com.devs.repository.ResponsavelLegalRepository;
import br.com.devs.service.ResponsavelLegalQueryService;
import br.com.devs.service.ResponsavelLegalService;
import br.com.devs.service.criteria.ResponsavelLegalCriteria;
import br.com.devs.service.dto.ResponsavelLegalDTO;
import br.com.devs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.devs.domain.ResponsavelLegal}.
 */
@RestController
@RequestMapping("/api")
public class ResponsavelLegalResource {

    private final Logger log = LoggerFactory.getLogger(ResponsavelLegalResource.class);

    private static final String ENTITY_NAME = "responsavelLegal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResponsavelLegalService responsavelLegalService;

    private final ResponsavelLegalRepository responsavelLegalRepository;

    private final ResponsavelLegalQueryService responsavelLegalQueryService;

    public ResponsavelLegalResource(
        ResponsavelLegalService responsavelLegalService,
        ResponsavelLegalRepository responsavelLegalRepository,
        ResponsavelLegalQueryService responsavelLegalQueryService
    ) {
        this.responsavelLegalService = responsavelLegalService;
        this.responsavelLegalRepository = responsavelLegalRepository;
        this.responsavelLegalQueryService = responsavelLegalQueryService;
    }

    /**
     * {@code POST  /responsavel-legals} : Create a new responsavelLegal.
     *
     * @param responsavelLegalDTO the responsavelLegalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new responsavelLegalDTO, or with status {@code 400 (Bad Request)} if the responsavelLegal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/responsavel-legals")
    public ResponseEntity<ResponsavelLegalDTO> createResponsavelLegal(@Valid @RequestBody ResponsavelLegalDTO responsavelLegalDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResponsavelLegal : {}", responsavelLegalDTO);
        if (responsavelLegalDTO.getId() != null) {
            throw new BadRequestAlertException("A new responsavelLegal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResponsavelLegalDTO result = responsavelLegalService.save(responsavelLegalDTO);
        return ResponseEntity
            .created(new URI("/api/responsavel-legals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /responsavel-legals/:id} : Updates an existing responsavelLegal.
     *
     * @param id the id of the responsavelLegalDTO to save.
     * @param responsavelLegalDTO the responsavelLegalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsavelLegalDTO,
     * or with status {@code 400 (Bad Request)} if the responsavelLegalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the responsavelLegalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/responsavel-legals/{id}")
    public ResponseEntity<ResponsavelLegalDTO> updateResponsavelLegal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResponsavelLegalDTO responsavelLegalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResponsavelLegal : {}, {}", id, responsavelLegalDTO);
        if (responsavelLegalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsavelLegalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsavelLegalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResponsavelLegalDTO result = responsavelLegalService.update(responsavelLegalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsavelLegalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /responsavel-legals/:id} : Partial updates given fields of an existing responsavelLegal, field will ignore if it is null
     *
     * @param id the id of the responsavelLegalDTO to save.
     * @param responsavelLegalDTO the responsavelLegalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsavelLegalDTO,
     * or with status {@code 400 (Bad Request)} if the responsavelLegalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the responsavelLegalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the responsavelLegalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/responsavel-legals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResponsavelLegalDTO> partialUpdateResponsavelLegal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResponsavelLegalDTO responsavelLegalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResponsavelLegal partially : {}, {}", id, responsavelLegalDTO);
        if (responsavelLegalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsavelLegalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsavelLegalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResponsavelLegalDTO> result = responsavelLegalService.partialUpdate(responsavelLegalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsavelLegalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /responsavel-legals} : get all the responsavelLegals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of responsavelLegals in body.
     */
    @GetMapping("/responsavel-legals")
    public ResponseEntity<List<ResponsavelLegalDTO>> getAllResponsavelLegals(
        ResponsavelLegalCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ResponsavelLegals by criteria: {}", criteria);
        Page<ResponsavelLegalDTO> page = responsavelLegalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /responsavel-legals/count} : count all the responsavelLegals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/responsavel-legals/count")
    public ResponseEntity<Long> countResponsavelLegals(ResponsavelLegalCriteria criteria) {
        log.debug("REST request to count ResponsavelLegals by criteria: {}", criteria);
        return ResponseEntity.ok().body(responsavelLegalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /responsavel-legals/:id} : get the "id" responsavelLegal.
     *
     * @param id the id of the responsavelLegalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the responsavelLegalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/responsavel-legals/{id}")
    public ResponseEntity<ResponsavelLegalDTO> getResponsavelLegal(@PathVariable Long id) {
        log.debug("REST request to get ResponsavelLegal : {}", id);
        Optional<ResponsavelLegalDTO> responsavelLegalDTO = responsavelLegalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(responsavelLegalDTO);
    }

    /**
     * {@code DELETE  /responsavel-legals/:id} : delete the "id" responsavelLegal.
     *
     * @param id the id of the responsavelLegalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/responsavel-legals/{id}")
    public ResponseEntity<Void> deleteResponsavelLegal(@PathVariable Long id) {
        log.debug("REST request to delete ResponsavelLegal : {}", id);
        responsavelLegalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
