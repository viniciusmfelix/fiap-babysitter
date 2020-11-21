package br.com.fiap.babysitter;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import br.com.fiap.babysitter.domain.model.Acao;
import br.com.fiap.babysitter.domain.model.Execucao;
import br.com.fiap.babysitter.domain.repository.AcaoRepository;
import br.com.fiap.babysitter.domain.repository.ExecucaoRepository;
import br.com.fiap.babysitter.utils.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class ExecucaoIntegrationTests {
	
	@LocalServerPort
    private Integer port;
	
	@Autowired
	private AcaoRepository acaoRepository;
	
	@Autowired
	private ExecucaoRepository execucaoRepository;
	
	Integer quantidadeExecucoesCadastradas;
	
	private Execucao execucao;
	private String jsonAcao1;
	
	@BeforeEach
    private void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.basePath = "/execucoes";
        RestAssured.port = port;
        
        jsonAcao1 = ResourceUtils
        		.getContentFromResource("/mockdata/collection/json/execucao-valida.json");
        
        prepararDados();
    }
	
	@Test
	void deveRetornarStatus200_QuandoConsultarExecucoes() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	void deveRetornarQuantidadeCorreta_QuandoConsultarExecucoes() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(quantidadeExecucoesCadastradas));
	}
	
	@Test
	void deveRetornarStatus201_AoCadastrarNovaExecucaoValida() {
		RestAssured.given()
			.body(jsonAcao1)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarStatus400_QuandoExcluirExecucaoExistente() {
		RestAssured.given()
			.pathParam("id", execucao.getId())
			.accept(ContentType.JSON)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	public void prepararDados() {
		Acao acaoA = new Acao();
		Acao acaoB = new Acao();
		Acao acaoC = new Acao();
		
		acaoA.setNome("Recolher brinquedos.");
		acaoA.setDescricao("Recolher os brinquedos do quarto e da sala");
		acaoA.setAtivo(true);
		
		acaoB.setNome("Passear no parque.");
		acaoB.setDescricao("Passea no parque do Ibirapuera.");
		acaoB.setAtivo(false);
		
		acaoC.setNome("Ir ao hortifruti");
		acaoC.setDescricao("Ir ao hortifruti uma vez a semana nas quartas.");
		acaoC.setAtivo(true);
		
		acaoRepository.save(acaoA);
		acaoRepository.save(acaoB);
		acaoRepository.save(acaoC);
		
		Execucao execucaoA = new Execucao();
		execucaoA.setAcao(acaoA);
		
		Execucao execucaoB = new Execucao();
		execucaoB.setAcao(acaoB);
		
		Execucao execucaoC = new Execucao();
		execucaoC.setAcao(acaoC);
		
		execucao = execucaoA;
		
		execucaoRepository.save(execucaoA);
		execucaoRepository.save(execucaoB);
		execucaoRepository.save(execucaoC);
		
		quantidadeExecucoesCadastradas = (int) execucaoRepository.count();
	}

}
