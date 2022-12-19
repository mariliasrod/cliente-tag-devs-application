package br.com.devs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "possui_beneficio_ativo")
    private Boolean possuiBeneficioAtivo;

    @Column(name = "renda_bruta", precision = 21, scale = 2)
    private BigDecimal rendaBruta;

    @ManyToOne
    private ResponsavelLegal responsavelLegal;

    @ManyToMany
    @JoinTable(
        name = "rel_cliente__tags",
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    @JsonIgnoreProperties(value = { "clientes" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cliente name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Cliente foto(byte[] foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Cliente fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Cliente dataNascimento(LocalDate dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getPossuiBeneficioAtivo() {
        return this.possuiBeneficioAtivo;
    }

    public Cliente possuiBeneficioAtivo(Boolean possuiBeneficioAtivo) {
        this.setPossuiBeneficioAtivo(possuiBeneficioAtivo);
        return this;
    }

    public void setPossuiBeneficioAtivo(Boolean possuiBeneficioAtivo) {
        this.possuiBeneficioAtivo = possuiBeneficioAtivo;
    }

    public BigDecimal getRendaBruta() {
        return this.rendaBruta;
    }

    public Cliente rendaBruta(BigDecimal rendaBruta) {
        this.setRendaBruta(rendaBruta);
        return this;
    }

    public void setRendaBruta(BigDecimal rendaBruta) {
        this.rendaBruta = rendaBruta;
    }

    public ResponsavelLegal getResponsavelLegal() {
        return this.responsavelLegal;
    }

    public void setResponsavelLegal(ResponsavelLegal responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    public Cliente responsavelLegal(ResponsavelLegal responsavelLegal) {
        this.setResponsavelLegal(responsavelLegal);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Cliente tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Cliente addTags(Tag tag) {
        this.tags.add(tag);
        tag.getClientes().add(this);
        return this;
    }

    public Cliente removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getClientes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", possuiBeneficioAtivo='" + getPossuiBeneficioAtivo() + "'" +
            ", rendaBruta=" + getRendaBruta() +
            "}";
    }
}
