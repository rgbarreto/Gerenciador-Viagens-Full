package com.montanha.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montanha.pojo.Viagem;

import java.io.FileInputStream;
import java.io.IOException;

public class ViagemDataFactory {



    public static Viagem criarViagem() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Viagem viagem = objectMapper.readValue(new FileInputStream("/Volumes/Disco2/Projects/gerenciador-viagens/src/test/resouces/requestBody/postV1Viagens.JSON"), Viagem.class);
        return viagem;
    }


    public static Viagem criarViagemValida() throws IOException {

        Viagem viagemValida = criarViagem();
        return viagemValida;

    }

    public static Viagem criarViagemSemLocalDeDestino() throws IOException {

        Viagem viagemSemLocalDeDestino = criarViagem();
        viagemSemLocalDeDestino.setLocalDeDestino("");
        return  viagemSemLocalDeDestino;

    }
}
