package io.github.leobugoni.cep.service;

import io.github.leobugoni.cep.model.Endereco;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Léo Bugoni leobugoni@gmail.com
 */
public class ConsultaCepServiceIntegrationTest {

    private ConsultaCepService service;

    @Before
    public void init(){
        service = new ConsultaCepService("https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl");
    }

    @Test
	public void testConsultarCepReal(){
    	final Endereco retorno = service.consultar("89.809-310");
        assertEquals("Engenho Braun", retorno.getBairro());
        assertEquals("Chapecó", retorno.getCidade());
        assertEquals("Rua Priamo do Amaral", retorno.getRua());
        assertEquals(new Integer(89809310), retorno.getCep());
        assertEquals("SC", retorno.getUf());
	}
}
