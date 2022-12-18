package br.com.devs.web.rest;

import static br.com.devs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.devs.IntegrationTest;
import br.com.devs.domain.Cliente;
import br.com.devs.domain.ResponsavelLegal;
import br.com.devs.domain.Tag;
import br.com.devs.repository.ClienteRepository;
import br.com.devs.service.ClienteService;
import br.com.devs.service.criteria.ClienteCriteria;
import br.com.devs.service.dto.ClienteDTO;
import br.com.devs.service.mapper.ClienteMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ClienteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClienteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_POSSUI_BENEFICIO_ATIVO = false;
    private static final Boolean UPDATED_POSSUI_BENEFICIO_ATIVO = true;

    private static final BigDecimal DEFAULT_RENDA_BRUTA = new BigDecimal(1);
    private static final BigDecimal UPDATED_RENDA_BRUTA = new BigDecimal(2);
    private static final BigDecimal SMALLER_RENDA_BRUTA = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteRepository clienteRepositoryMock;

    @Autowired
    private ClienteMapper clienteMapper;

    @Mock
    private ClienteService clienteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .name(DEFAULT_NAME)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .possuiBeneficioAtivo(DEFAULT_POSSUI_BENEFICIO_ATIVO)
            .rendaBruta(DEFAULT_RENDA_BRUTA);
        return cliente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createUpdatedEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .name(UPDATED_NAME)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .possuiBeneficioAtivo(UPDATED_POSSUI_BENEFICIO_ATIVO)
            .rendaBruta(UPDATED_RENDA_BRUTA);
        return cliente;
    }

    @BeforeEach
    public void initTest() {
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();
        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCliente.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testCliente.getPossuiBeneficioAtivo()).isEqualTo(DEFAULT_POSSUI_BENEFICIO_ATIVO);
        assertThat(testCliente.getRendaBruta()).isEqualByComparingTo(DEFAULT_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void createClienteWithExistingId() throws Exception {
        // Create the Cliente with an existing ID
        cliente.setId(1L);
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setName(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].possuiBeneficioAtivo").value(hasItem(DEFAULT_POSSUI_BENEFICIO_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].rendaBruta").value(hasItem(sameNumber(DEFAULT_RENDA_BRUTA))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientesWithEagerRelationshipsIsEnabled() throws Exception {
        when(clienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(clienteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(clienteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.possuiBeneficioAtivo").value(DEFAULT_POSSUI_BENEFICIO_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.rendaBruta").value(sameNumber(DEFAULT_RENDA_BRUTA)));
    }

    @Test
    @Transactional
    void getClientesByIdFiltering() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        Long id = cliente.getId();

        defaultClienteShouldBeFound("id.equals=" + id);
        defaultClienteShouldNotBeFound("id.notEquals=" + id);

        defaultClienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClienteShouldNotBeFound("id.greaterThan=" + id);

        defaultClienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where name equals to DEFAULT_NAME
        defaultClienteShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the clienteList where name equals to UPDATED_NAME
        defaultClienteShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClientesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClienteShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the clienteList where name equals to UPDATED_NAME
        defaultClienteShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClientesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where name is not null
        defaultClienteShouldBeFound("name.specified=true");

        // Get all the clienteList where name is null
        defaultClienteShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByNameContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where name contains DEFAULT_NAME
        defaultClienteShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the clienteList where name contains UPDATED_NAME
        defaultClienteShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClientesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where name does not contain DEFAULT_NAME
        defaultClienteShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the clienteList where name does not contain UPDATED_NAME
        defaultClienteShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento equals to DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento in DEFAULT_DATA_NASCIMENTO or UPDATED_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.in=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is not null
        defaultClienteShouldBeFound("dataNascimento.specified=true");

        // Get all the clienteList where dataNascimento is null
        defaultClienteShouldNotBeFound("dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is greater than or equal to DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is greater than or equal to UPDATED_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is less than or equal to DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is less than or equal to SMALLER_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is less than DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is less than UPDATED_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is greater than DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is greater than SMALLER_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByPossuiBeneficioAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where possuiBeneficioAtivo equals to DEFAULT_POSSUI_BENEFICIO_ATIVO
        defaultClienteShouldBeFound("possuiBeneficioAtivo.equals=" + DEFAULT_POSSUI_BENEFICIO_ATIVO);

        // Get all the clienteList where possuiBeneficioAtivo equals to UPDATED_POSSUI_BENEFICIO_ATIVO
        defaultClienteShouldNotBeFound("possuiBeneficioAtivo.equals=" + UPDATED_POSSUI_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void getAllClientesByPossuiBeneficioAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where possuiBeneficioAtivo in DEFAULT_POSSUI_BENEFICIO_ATIVO or UPDATED_POSSUI_BENEFICIO_ATIVO
        defaultClienteShouldBeFound("possuiBeneficioAtivo.in=" + DEFAULT_POSSUI_BENEFICIO_ATIVO + "," + UPDATED_POSSUI_BENEFICIO_ATIVO);

        // Get all the clienteList where possuiBeneficioAtivo equals to UPDATED_POSSUI_BENEFICIO_ATIVO
        defaultClienteShouldNotBeFound("possuiBeneficioAtivo.in=" + UPDATED_POSSUI_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void getAllClientesByPossuiBeneficioAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where possuiBeneficioAtivo is not null
        defaultClienteShouldBeFound("possuiBeneficioAtivo.specified=true");

        // Get all the clienteList where possuiBeneficioAtivo is null
        defaultClienteShouldNotBeFound("possuiBeneficioAtivo.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByRendaBrutaIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rendaBruta equals to DEFAULT_RENDA_BRUTA
        defaultClienteShouldBeFound("rendaBruta.equals=" + DEFAULT_RENDA_BRUTA);

        // Get all the clienteList where rendaBruta equals to UPDATED_RENDA_BRUTA
        defaultClienteShouldNotBeFound("rendaBruta.equals=" + UPDATED_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void getAllClientesByRendaBrutaIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rendaBruta in DEFAULT_RENDA_BRUTA or UPDATED_RENDA_BRUTA
        defaultClienteShouldBeFound("rendaBruta.in=" + DEFAULT_RENDA_BRUTA + "," + UPDATED_RENDA_BRUTA);

        // Get all the clienteList where rendaBruta equals to UPDATED_RENDA_BRUTA
        defaultClienteShouldNotBeFound("rendaBruta.in=" + UPDATED_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void getAllClientesByRendaBrutaIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rendaBruta is not null
        defaultClienteShouldBeFound("rendaBruta.specified=true");

        // Get all the clienteList where rendaBruta is null
        defaultClienteShouldNotBeFound("rendaBruta.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByRendaBrutaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rendaBruta is greater than or equal to DEFAULT_RENDA_BRUTA
        defaultClienteShouldBeFound("rendaBruta.greaterThanOrEqual=" + DEFAULT_RENDA_BRUTA);

        // Get all the clienteList where rendaBruta is greater than or equal to UPDATED_RENDA_BRUTA
        defaultClienteShouldNotBeFound("rendaBruta.greaterThanOrEqual=" + UPDATED_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void getAllClientesByRendaBrutaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rendaBruta is less than or equal to DEFAULT_RENDA_BRUTA
        defaultClienteShouldBeFound("rendaBruta.lessThanOrEqual=" + DEFAULT_RENDA_BRUTA);

        // Get all the clienteList where rendaBruta is less than or equal to SMALLER_RENDA_BRUTA
        defaultClienteShouldNotBeFound("rendaBruta.lessThanOrEqual=" + SMALLER_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void getAllClientesByRendaBrutaIsLessThanSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rendaBruta is less than DEFAULT_RENDA_BRUTA
        defaultClienteShouldNotBeFound("rendaBruta.lessThan=" + DEFAULT_RENDA_BRUTA);

        // Get all the clienteList where rendaBruta is less than UPDATED_RENDA_BRUTA
        defaultClienteShouldBeFound("rendaBruta.lessThan=" + UPDATED_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void getAllClientesByRendaBrutaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rendaBruta is greater than DEFAULT_RENDA_BRUTA
        defaultClienteShouldNotBeFound("rendaBruta.greaterThan=" + DEFAULT_RENDA_BRUTA);

        // Get all the clienteList where rendaBruta is greater than SMALLER_RENDA_BRUTA
        defaultClienteShouldBeFound("rendaBruta.greaterThan=" + SMALLER_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void getAllClientesByResponsavelLegalIsEqualToSomething() throws Exception {
        ResponsavelLegal responsavelLegal;
        if (TestUtil.findAll(em, ResponsavelLegal.class).isEmpty()) {
            clienteRepository.saveAndFlush(cliente);
            responsavelLegal = ResponsavelLegalResourceIT.createEntity(em);
        } else {
            responsavelLegal = TestUtil.findAll(em, ResponsavelLegal.class).get(0);
        }
        em.persist(responsavelLegal);
        em.flush();
        cliente.setResponsavelLegal(responsavelLegal);
        clienteRepository.saveAndFlush(cliente);
        Long responsavelLegalId = responsavelLegal.getId();

        // Get all the clienteList where responsavelLegal equals to responsavelLegalId
        defaultClienteShouldBeFound("responsavelLegalId.equals=" + responsavelLegalId);

        // Get all the clienteList where responsavelLegal equals to (responsavelLegalId + 1)
        defaultClienteShouldNotBeFound("responsavelLegalId.equals=" + (responsavelLegalId + 1));
    }

    @Test
    @Transactional
    void getAllClientesByTagsIsEqualToSomething() throws Exception {
        Tag tags;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            clienteRepository.saveAndFlush(cliente);
            tags = TagResourceIT.createEntity(em);
        } else {
            tags = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tags);
        em.flush();
        cliente.addTags(tags);
        clienteRepository.saveAndFlush(cliente);
        Long tagsId = tags.getId();

        // Get all the clienteList where tags equals to tagsId
        defaultClienteShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the clienteList where tags equals to (tagsId + 1)
        defaultClienteShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClienteShouldBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].possuiBeneficioAtivo").value(hasItem(DEFAULT_POSSUI_BENEFICIO_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].rendaBruta").value(hasItem(sameNumber(DEFAULT_RENDA_BRUTA))));

        // Check, that the count call also returns 1
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClienteShouldNotBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).get();
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente
            .name(UPDATED_NAME)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .possuiBeneficioAtivo(UPDATED_POSSUI_BENEFICIO_ATIVO)
            .rendaBruta(UPDATED_RENDA_BRUTA);
        ClienteDTO clienteDTO = clienteMapper.toDto(updatedCliente);

        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCliente.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testCliente.getPossuiBeneficioAtivo()).isEqualTo(UPDATED_POSSUI_BENEFICIO_ATIVO);
        assertThat(testCliente.getRendaBruta()).isEqualByComparingTo(UPDATED_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void putNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente.dataNascimento(UPDATED_DATA_NASCIMENTO);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCliente.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testCliente.getPossuiBeneficioAtivo()).isEqualTo(DEFAULT_POSSUI_BENEFICIO_ATIVO);
        assertThat(testCliente.getRendaBruta()).isEqualByComparingTo(DEFAULT_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void fullUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente
            .name(UPDATED_NAME)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .possuiBeneficioAtivo(UPDATED_POSSUI_BENEFICIO_ATIVO)
            .rendaBruta(UPDATED_RENDA_BRUTA);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCliente.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testCliente.getPossuiBeneficioAtivo()).isEqualTo(UPDATED_POSSUI_BENEFICIO_ATIVO);
        assertThat(testCliente.getRendaBruta()).isEqualByComparingTo(UPDATED_RENDA_BRUTA);
    }

    @Test
    @Transactional
    void patchNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Delete the cliente
        restClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cliente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
