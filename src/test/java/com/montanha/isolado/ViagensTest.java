package com.montanha.isolado;

import com.montanha.config.Configuracoes;
import com.montanha.factory.UsuarioDataFactory;
import com.montanha.factory.ViagemDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.Viagem;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import javafx.beans.binding.When;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;


public class ViagensTest {


    private String token;
    private String tokenUsuario;

    @Before
    public void setUP(){

        // configuraçao Rest-Assured

        Configuracoes configuracoes = ConfigFactory.create(Configuracoes.class);
        baseURI = configuracoes.baseURI();
        port = configuracoes.port();
        basePath = configuracoes.basePath();

        Usuario usuarioAdministrador = UsuarioDataFactory.criarUsuarioAdministrador();


        this.token = given()
            .contentType(ContentType.JSON)
            .body(usuarioAdministrador)
        .when()
            .post("v1/auth")
        .then()
            .extract()
            .path("data.token");

        Usuario usuarioComum = UsuarioDataFactory.criarUsuarioComum();

        this.tokenUsuario = given()
                .contentType(ContentType.JSON)
                .body(usuarioComum)
        .when()
                .post("v1/auth")
        .then()
                .extract()
                .path("data.token");

    }


    @Test
    public void testCadastraViagemValidaRetornasucesso() throws IOException {

       Viagem viagemValida = ViagemDataFactory.criarViagemValida();

            given()
                .contentType(ContentType.JSON)
                .body(viagemValida)
                .header("Authorization",token)
            .when()
                 .post("/v1/viagens/")
            .then()
                 .assertThat()
                    .statusCode(201)
                    .body("data.localDeDestino",equalTo("Sao Paul"))
                    .body("data.acompanhante",equalToIgnoringCase("dig"));


    }

    @Test
    public void testViagemNaoPodemSerCadastradaSemLocalDeDestino() throws IOException {

        Viagem viagemSemLocalDestino = ViagemDataFactory.criarViagemSemLocalDeDestino();

        given()
                .contentType(ContentType.JSON)
                .body(viagemSemLocalDestino)
                .header("Authorization",token)
        .when()
                .post("/v1/viagens/")
        .then()
                .assertThat()
                .statusCode(400);

    }

   @Test
    public void testCadastraViagemValidaContrato() throws IOException {

        Viagem viagemValida = ViagemDataFactory.criarViagemValida();

        given()
                .contentType(ContentType.JSON)
                .body(viagemValida)
                .header("Authorization",token)
        .when()
                .post("/v1/viagens/")
        .then()
                .assertThat()
                  .statusCode(201)
                  .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/postV1ViagensViagemValida.JSON"));


    }

    @Test
    public void testRetornaUmaViagemPossuiStatusCode200MostraLocalDeDestino(){

        given()
            .header("Authorization", tokenUsuario)

        .when()
                .get("v1/viagens/1")
        .then()
                .assertThat()
                    .statusCode(200)
                    .body("data.localDeDestino",equalTo("Porto Alegre"));

    }

    @Test
    public void testViagemProcessaCorretamenteRetornoApiDoTempo(){

        given()
                .header("Authorization", tokenUsuario)

                .when()
                .get("v1/viagens/1")
                .then()
                .assertThat()
                .statusCode(201)
                .body("data.temperatura",equalTo(35.5f));

    }


}
