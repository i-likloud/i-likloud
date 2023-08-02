package com.backend.global.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

public class UnixTimestampDeserializer extends JsonDeserializer {

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String timestamp = jp.getText().trim();
        return new Date(Long.parseLong(timestamp) * 1000L);
    }
}
