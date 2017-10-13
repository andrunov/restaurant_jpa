package ru.agorbunov.restaurant.web.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.agorbunov.restaurant.util.DateTimeUtil;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Class for customize serialize-deserialize LocalDateTime
 */
class JsonLocalDateTimeConverter {
    static class UserSettingSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime ldt, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(DateTimeUtil.toString(ldt));
        }

        @Override
        public Class<LocalDateTime> handledType() {
            return LocalDateTime.class;
        }
    }

    static class UserSettingDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
            return DateTimeUtil.parseLocalDateTime(jp.getText());
        }

        @Override
        public Class<LocalDateTime> handledType() {
            return LocalDateTime.class;
        }
    }
}
