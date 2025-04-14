package com.student.management.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExerciseGeneratorService {

    private final WebClient openAiClient;

    @Autowired
    public ExerciseGeneratorService(WebClient.Builder webClientBuilder) {
        // Lê a variável de ambiente OPENAI_API_KEY
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("A chave da API OpenAI não foi configurada como variável de ambiente.");
        }

        // Configura o WebClient com o cabeçalho Authorization com a chave da API
        this.openAiClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1")  // Definindo o URL base da API
                .defaultHeader("Authorization", "Bearer " + apiKey)  // Usando a chave da API recuperada
                .build();
    }

    public List<String> generateExercises(String subject, int count, String topic) {
        // 1) Monte o prompt
        String prompt = String.format(
                "Você é um gerador de exercícios didáticos. Gere %d questões de %s sobre %s. " +
                        "Cada questão deve vir com enunciado e a resposta separada.",
                count, subject, topic
        );

        // 2) Crie o corpo da requisição
        var body = Map.of(
                "model", "gpt-3.5-turbo", // Usando modelo 3.5, ou altere conforme sua necessidade
                "messages", List.of(
                        Map.of("role", "system", "content", "Você é um gerador de exercícios escolares."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        // 3) Faça a chamada
        JsonNode responseJson = openAiClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                System.out.println("Erro de servidor! Resposta: " + errorBody);
                                return Mono.error(new RuntimeException("Erro no servidor: " + errorBody));
                            });
                })
                .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                System.out.println("Erro de cliente! Resposta: " + errorBody);
                                return Mono.error(new RuntimeException("Erro no cliente: " + errorBody));
                            });
                })
                .bodyToMono(JsonNode.class)
                .block();  // bloqueia até receber a resposta

        // Log do corpo da resposta
        System.out.println("Resposta da OpenAI: " + responseJson.toString());

        // 4) Extraia cada "message.content" dos choices
        if (responseJson.has("choices")) {
            return StreamSupport.stream(responseJson
                            .path("choices")
                            .spliterator(), false)
                    .map(node -> node.path("message").path("content").asText())
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Erro na resposta da OpenAI: " + responseJson.toString());
        }
    }
}
