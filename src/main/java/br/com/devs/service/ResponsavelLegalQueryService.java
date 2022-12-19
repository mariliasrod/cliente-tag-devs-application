package br.com.devs.service;

import br.com.devs.domain.*; // for static metamodels
import br.com.devs.domain.ResponsavelLegal;
import br.com.devs.repository.ResponsavelLegalRepository;
import br.com.devs.service.criteria.ResponsavelLegalCriteria;
import br.com.devs.service.dto.ResponsavelLegalDTO;
import br.com.devs.service.mapper.ResponsavelLegalMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ResponsavelLegal} entities in the database.
 * The main input is a {@link ResponsavelLegalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ResponsavelLegalDTO} or a {@link Page} of {@link ResponsavelLegalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResponsavelLegalQueryService extends QueryService<ResponsavelLegal> {

    private final Logger log = LoggerFactory.getLogger(ResponsavelLegalQueryService.class);

    private final ResponsavelLegalRepository responsavelLegalRepository;

    private final ResponsavelLegalMapper responsavelLegalMapper;

    public ResponsavelLegalQueryService(
        ResponsavelLegalRepository responsavelLegalRepository,
        ResponsavelLegalMapper responsavelLegalMapper
    ) {
        this.responsavelLegalRepository = responsavelLegalRepository;
        this.responsavelLegalMapper = responsavelLegalMapper;
    }

    /**
     * Return a {@link List} of {@link ResponsavelLegalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResponsavelLegalDTO> findByCriteria(ResponsavelLegalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ResponsavelLegal> specification = createSpecification(criteria);
        return responsavelLegalMapper.toDto(responsavelLegalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ResponsavelLegalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResponsavelLegalDTO> findByCriteria(ResponsavelLegalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ResponsavelLegal> specification = createSpecification(criteria);
        return responsavelLegalRepository.findAll(specification, page).map(responsavelLegalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResponsavelLegalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ResponsavelLegal> specification = createSpecification(criteria);
        return responsavelLegalRepository.count(specification);
    }

    /**
     * Function to convert {@link ResponsavelLegalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ResponsavelLegal> createSpecification(ResponsavelLegalCriteria criteria) {
        Specification<ResponsavelLegal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ResponsavelLegal_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), ResponsavelLegal_.nome));
            }
        }
        return specification;
    }
}
