package br.com.devs.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.devs.domain.Cliente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private byte[] foto;

    private String fotoContentType;
    private LocalDate dataNascimento;

    private Boolean possuiBeneficioAtivo;

    private BigDecimal rendaBruta;

    private ResponsavelLegalDTO responsavelLegal;

    private Set<TagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getPossuiBeneficioAtivo() {
        return possuiBeneficioAtivo;
    }

    public void setPossuiBeneficioAtivo(Boolean possuiBeneficioAtivo) {
        this.possuiBeneficioAtivo = possuiBeneficioAtivo;
    }

    public BigDecimal getRendaBruta() {
        return rendaBruta;
    }

    public void setRendaBruta(BigDecimal rendaBruta) {
        this.rendaBruta = rendaBruta;
    }

    public ResponsavelLegalDTO getResponsavelLegal() {
        return responsavelLegal;
    }

    public void setResponsavelLegal(ResponsavelLegalDTO responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        ClienteDTO clienteDTO = (ClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", foto='" + getFoto() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", possuiBeneficioAtivo='" + getPossuiBeneficioAtivo() + "'" +
            ", rendaBruta=" + getRendaBruta() +
            ", responsavelLegal=" + getResponsavelLegal() +
            ", tags=" + getTags() +
            "}";
    }
}
