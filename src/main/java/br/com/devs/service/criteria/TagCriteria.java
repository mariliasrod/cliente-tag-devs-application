package br.com.devs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.devs.domain.Tag} entity. This class is used
 * in {@link br.com.devs.web.rest.TagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private LongFilter clientesId;

    private Boolean distinct;

    public TagCriteria() {}

    public TagCriteria(TagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.clientesId = other.clientesId == null ? null : other.clientesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TagCriteria copy() {
        return new TagCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public LongFilter getClientesId() {
        return clientesId;
    }

    public LongFilter clientesId() {
        if (clientesId == null) {
            clientesId = new LongFilter();
        }
        return clientesId;
    }

    public void setClientesId(LongFilter clientesId) {
        this.clientesId = clientesId;
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
        final TagCriteria that = (TagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(clientesId, that.clientesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, clientesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (clientesId != null ? "clientesId=" + clientesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
