package br.com.devs.service.mapper;

import br.com.devs.domain.ResponsavelLegal;
import br.com.devs.service.dto.ResponsavelLegalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResponsavelLegal} and its DTO {@link ResponsavelLegalDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResponsavelLegalMapper extends EntityMapper<ResponsavelLegalDTO, ResponsavelLegal> {}
