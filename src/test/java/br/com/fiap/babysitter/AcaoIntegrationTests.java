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
class AcaoIntegrationTests {
	
	@LocalServerPort
    private Integer port;
	
	@Autowired
	private AcaoRepository acaoRepository;
	
	@Autowired
	private ExecucaoRepository execucaoRepository;
	
	private Acao recolherBrinquedos;
	private Integer quantidadeAcoesCadastradas;
	private String jsonRecolherBrinquedos;
	private String jsonRecolherBrinquedosAtualizado;
	private String jsonAcaoInvalida;
	
	@BeforeEach
    private void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.basePath = "/acoes";
        RestAssured.port = port;
        
        jsonRecolherBrinquedos = ResourceUtils
        		.getContentFromResource("/mockdata/collection/json/acao-recolher-brinquedos.json");
        
        jsonRecolherBrinquedosAtualizado = ResourceUtils
        		.getContentFromResource("/mockdata/collection/json/acao-recolher-brinquedos-atualizada.json");
        
        jsonAcaoInvalida = ResourceUtils
        		.getContentFromResource("/mockdata/collection/json/acao-invalida.json");
        
        prepararDados();
    }

	@Test
	void deveRetornarStatus200_QuandoConsultarAcoes() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	void deveRetornarQuantidadeCorreta_QuandoConsultarAcoes() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(quantidadeAcoesCadastradas));
	}
	
	@Test
	void deveRetornarStatus201_AoCadastrarNovaAcaoValida() {
		RestAssured.given()
			.body(jsonRecolherBrinquedos)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	void deveRetornarStatus200EAcaoCorreta_QuandoConsultarAcao() {
		RestAssured.given()
		.pathParam("id", recolherBrinquedos.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", Matchers.equalTo(recolherBrinquedos.getNome()));
	}
	
	@Test
	void deveRetornarStatus200EAcaoAtualizada_AoAtualizarAcao() {
		String propAtualizada = "Recolhimento dos briquedos espalhados pelo quarto e garagem.";
		
		RestAssured.given()
			.pathParam("id", recolherBrinquedos.getId())
			.body(jsonRecolherBrinquedosAtualizado)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("descricao", Matchers.equalTo(propAtualizada));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarAcaoInexistente() {
		RestAssured.given()
			.pathParam("id", 9999999)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarStatus400_QuandoViolarConstraint() {
		RestAssured.given()
			.body(jsonAcaoInvalida)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	
	}
	
	
	@Test
	public void deveRetornarStatus409_QuandoExcluirUmaAcaoComExecucoes() {
		RestAssured.given()
			.pathParam("id", recolherBrinquedos.getId())
			.accept(ContentType.JSON)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.CONFLICT.value());
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
		
		quantidadeAcoesCadastradas = (int) acaoRepository.count();
		
		recolherBrinquedos = acaoA;
		
		Execucao execucao = new Execucao();
		execucao.setAcao(acaoA);
		
		execucaoRepository.save(execucao);
	}

}
