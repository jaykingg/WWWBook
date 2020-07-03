package com.community.weddingbook.Author;

import com.community.weddingbook.Board.Board;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AuthorSerializer extends JsonSerializer<Author> {
    @Override
    public void serialize(Author author, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("userName", author.getName());
        jsonGenerator.writeStringField("bearer token", author.getServiceAccessToken());
        jsonGenerator.writeStringField("role", author.getRoles().toString());
        jsonGenerator.writeEndObject();
    }
}
