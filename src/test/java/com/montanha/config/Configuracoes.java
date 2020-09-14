package com.montanha.config;

import org.aeonbits.owner.Config;


@Config.Sources({"file:/Volumes/Disco2/Projects/gerenciador-viagens/src/test/resouces/properties/test.properties"})
public interface Configuracoes extends Config {

    @Key("baseURI")
    String baseURI();

    @Key("port")
    Integer port();

    @Key("basePath")
    String basePath();



}