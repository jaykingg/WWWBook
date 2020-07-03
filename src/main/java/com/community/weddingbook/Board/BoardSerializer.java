package com.community.weddingbook.Board;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class BoardSerializer extends JsonSerializer<Board> {

    @Override
    public void serialize(Board board, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        /*  */
        jsonGenerator.writeEndObject();
    }
}
