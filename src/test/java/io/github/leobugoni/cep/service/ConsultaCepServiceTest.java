package io.github.leobugoni.cep.service;

import io.github.leobugoni.cep.model.Endereco;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Léo Bugoni leobugoni@gmail.com
 */
public class ConsultaCepServiceTest {

    private ConsultaCepService service = new ConsultaCepService(){
        @Override
        public Endereco consultar(final String cep) {
            final Endereco endereco = new Endereco("Engenho Braun", 89809310, "Chapecó", "Rua Priamo do Amaral", "SC");
            return endereco;
        }
    };

    @Test
    public void testCep(){
        final Endereco retorno = service.consultar("89.809-310");
        assertEquals("Engenho Braun", retorno.getBairro());
        assertEquals("Chapecó", retorno.getCidade());
        assertEquals("Rua Priamo do Amaral", retorno.getRua());
        assertEquals(new Integer(89809310), retorno.getCep());
        assertEquals("SC", retorno.getUf());
    }
}



