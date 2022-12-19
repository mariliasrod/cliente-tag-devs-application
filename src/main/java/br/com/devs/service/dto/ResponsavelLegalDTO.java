package br.com.devs.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.devs.domain.ResponsavelLegal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResponsavelLegalDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponsavelLegalDTO)) {
            return false;
        }

        ResponsavelLegalDTO responsavelLegalDTO = (ResponsavelLegalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, responsavelLegalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResponsavelLegalDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
