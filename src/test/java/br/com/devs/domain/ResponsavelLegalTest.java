package br.com.devs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.devs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResponsavelLegalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponsavelLegal.class);
        ResponsavelLegal responsavelLegal1 = new ResponsavelLegal();
        responsavelLegal1.setId(1L);
        ResponsavelLegal responsavelLegal2 = new ResponsavelLegal();
        responsavelLegal2.setId(responsavelLegal1.getId());
        assertThat(responsavelLegal1).isEqualTo(responsavelLegal2);
        responsavelLegal2.setId(2L);
        assertThat(responsavelLegal1).isNotEqualTo(responsavelLegal2);
        responsavelLegal1.setId(null);
        assertThat(responsavelLegal1).isNotEqualTo(responsavelLegal2);
    }
}
