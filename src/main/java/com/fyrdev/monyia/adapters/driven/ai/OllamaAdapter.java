package com.fyrdev.monyia.adapters.driven.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.dto.ClassificationResult;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OllamaAdapter implements AITextClassifierPort {
    private final OllamaClient ollamaClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public ClassificationResult classifyTransaction(String text) {
        LocalDate currentDate = LocalDate.now();
        String userText = "%s Fecha actual: %s".formatted(text, currentDate);
        String prompt = "Eres un asistente que extrae información de transacciones financieras a partir de un texto. Tu única salida debe ser un JSON válido, sin ningún texto adicional, sin explicaciones, sin comillas envolventes ni etiquetas como 'json'. El JSON debe tener exclusivamente las siguientes claves: date (fecha mencionada en el texto, si no hay, devuélvela como 'desconocida' o infiérela si se proporciona una fecha de referencia en el prompt, en formato YYYY-MM-DD), periodicity (ONCE, DAILY, WEEKLY, MONTHLY o ANUAL, usar 'ONCE' por defecto), category (como Alimentación, Transporte, Mascotas, etc.), value (solo el número sin símbolos ni separadores) y type (INCOME o EXPENSE). La clave description es obligatoria: debe ser un resumen breve y claro del texto de entrada. Devuelve únicamente ese JSON y nada más.";


        String response = ollamaClient.sendPrompt(prompt, userText);

        try {
            JsonNode root = objectMapper.readTree(response);

            Category category = new Category();
            category.setName(root.get("category").asText());

            return new ClassificationResult(
                    root.get("date").asText(),
                    root.get("periodicity").asText(),
                    category,
                    root.get("value").asLong(),
                    root.get("type").asText(),
                    root.get("description").asText()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear la respuesta de la IA", e);
        }
    }

    @Override
    public List<String> suggestEmojis(String categoryName) {
        String prompt = "Eres un asistente que responde únicamente con datos en formato JSON. " +
                        "Dame exactamente 5 emojis que representen visualmente la categoría financiera, entendida como una categoría de ingreso o gasto dentro de una app de gestión de finanzas personales. " +
                        "No asumas que todos los ítems deben llevar símbolos de dinero. En su lugar, elige emojis que reflejen visualmente el tipo de gasto o ingreso según su naturaleza " +
                        "(por ejemplo, útiles escolares puede incluir lápices, libros, etc.). " +
                        "Solo responde con un objeto JSON válido, sin texto adicional, sin explicaciones, sin títulos, sin saltos de línea. " +
                        "El formato debe ser estrictamente este: {\\\"emojis\\\": [\\\"emoji1\\\", \\\"emoji2\\\", \\\"emoji3\\\", \\\"emoji4\\\", \\\"emoji5\\\"]}.";

        String response = ollamaClient.sendPrompt(prompt, categoryName);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode emojisArray = root.get("emojis");

            List<String> emojis = new ArrayList<>();
            if (emojisArray != null && emojisArray.isArray()) {
                for (JsonNode node : emojisArray) {
                    emojis.add(node.asText());
                }
            }

            return emojis;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener o parsear los emojis de la IA", e);
        }
    }

}
