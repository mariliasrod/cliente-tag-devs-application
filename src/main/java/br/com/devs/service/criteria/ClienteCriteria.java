package br.com.devs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.devs.domain.Cliente} entity. This class is used
 * in {@link br.com.devs.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter dataNascimento;

    private BooleanFilter possuiBeneficioAtivo;

    private BigDecimalFilter rendaBruta;

    private LongFilter responsavelLegalId;

    private LongFilter tagsId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dataNascimento = other.dataNascimento == null ? null : other.dataNascimento.copy();
        this.possuiBeneficioAtivo = other.possuiBeneficioAtivo == null ? null : other.possuiBeneficioAtivo.copy();
        this.rendaBruta = other.rendaBruta == null ? null : other.rendaBruta.copy();
        this.responsavelLegalId = other.responsavelLegalId == null ? null : other.responsavelLegalId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getDataNascimento() {
        return dataNascimento;
    }

    public LocalDateFilter dataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = new LocalDateFilter();
        }
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public BooleanFilter getPossuiBeneficioAtivo() {
        return possuiBeneficioAtivo;
    }

    public BooleanFilter possuiBeneficioAtivo() {
        if (possuiBeneficioAtivo == null) {
            possuiBeneficioAtivo = new BooleanFilter();
        }
        return possuiBeneficioAtivo;
    }

    public void setPossuiBeneficioAtivo(BooleanFilter possuiBeneficioAtivo) {
        this.possuiBeneficioAtivo = possuiBeneficioAtivo;
    }

    public BigDecimalFilter getRendaBruta() {
        return rendaBruta;
    }

    public BigDecimalFilter rendaBruta() {
        if (rendaBruta == null) {
            rendaBruta = new BigDecimalFilter();
        }
        return rendaBruta;
    }

    public void setRendaBruta(BigDecimalFilter rendaBruta) {
        this.rendaBruta = rendaBruta;
    }

    public LongFilter getResponsavelLegalId() {
        return responsavelLegalId;
    }

    public LongFilter responsavelLegalId() {
        if (responsavelLegalId == null) {
            responsavelLegalId = new LongFilter();
        }
        return responsavelLegalId;
    }

    public void setResponsavelLegalId(LongFilter responsavelLegalId) {
        this.responsavelLegalId = responsavelLegalId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public LongFilter tagsId() {
        if (tagsId == null) {
            tagsId = new LongFilter();
        }
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(possuiBeneficioAtivo, that.possuiBeneficioAtivo) &&
            Objects.equals(rendaBruta, that.rendaBruta) &&
            Objects.equals(responsavelLegalId, that.responsavelLegalId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dataNascimento, possuiBeneficioAtivo, rendaBruta, responsavelLegalId, tagsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dataNascimento != null ? "dataNascimento=" + dataNascimento + ", " : "") +
            (possuiBeneficioAtivo != null ? "possuiBeneficioAtivo=" + possuiBeneficioAtivo + ", " : "") +
            (rendaBruta != null ? "rendaBruta=" + rendaBruta + ", " : "") +
            (responsavelLegalId != null ? "responsavelLegalId=" + responsavelLegalId + ", " : "") +
            (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
