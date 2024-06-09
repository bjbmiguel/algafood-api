package com.algaworks.algafood.core.jakson;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent //Para registrar um serializador
public class PageJsonSerializer  extends JsonSerializer<Page<?>> {

    @Override
    public void serialize(Page<?> page, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeStartObject(); //Começa a escrever o object

        gen.writeObjectField("content", page.getContent());//Escreve uma propriedade "content" passando o seu conteúdo completo

        //Escrevemos as propriedades de paginação que queremos visualizar...
        gen.writeNumberField("size", page.getSize());
        gen.writeNumberField("totalElements", page.getTotalElements());
        gen.writeNumberField("totalPages", page.getTotalPages());
        gen.writeNumberField("number", page.getNumber());

        gen.writeEndObject(); //Termina de escrever o objecto
    }
}
