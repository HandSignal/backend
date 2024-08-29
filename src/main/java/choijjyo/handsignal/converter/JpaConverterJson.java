package choijjyo.handsignal.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Converter
public class JpaConverterJson implements AttributeConverter<List<Map<String, Object>>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Map<String, Object>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert list of maps to JSON string.", e);
        }
    }

    @Override
    public List<Map<String, Object>> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert JSON string to list of maps.", e);
        }
    }
}
