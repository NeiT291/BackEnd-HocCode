package com.neit.hoccode.service;

import com.neit.hoccode.dto.response.RunCodeResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class RunCodeService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:2358")
            .build();

    public RunCodeResponse run(Integer language_id, String source_code, String stdin, String expected_output, Float cpu_time_limit, Integer memory_limit) {


        Map<String, Object> submitBody = Map.of(
                "language_id", language_id,
                "source_code", source_code,
                "stdin", stdin,
                "expected_output", expected_output,
                "cpu_time_limit", cpu_time_limit,
                "memory_limit", memory_limit
        );
        Map submitRes = webClient.post()
                .uri("/submissions?wait=true")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(submitBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String token = submitRes.get("token").toString();

        Map result;
        boolean b64_encode = false;
        if (language_id == 54){
            b64_encode = true;
        }
        do {
            result = webClient.get()
                    .uri("/submissions/{token}?base64_encoded=false" , token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } while ((int) ((Map) result.get("status")).get("id") <= 2);

        // 3️⃣ Map response
        Map status = (Map) result.get("status");

        RunCodeResponse response = new RunCodeResponse();
        response.setStdout((String) result.get("stdout"));
        response.setStderr((String) result.get("stderr"));
        response.setCompileOutput((String) result.get("compile_output"));
        response.setTime((String) result.get("time"));
        response.setMemory((Integer) result.get("memory"));
        response.setMessage((String) result.get("message"));
        response.setStatusId((Integer) status.get("id"));
        response.setStatus((String) status.get("description"));

        return response;
    }
    public RunCodeResponse runApiTest(Integer language_id, String source_code, String stdin) {


        Map<String, Object> submitBody = Map.of(
                "language_id", language_id,
                "source_code", source_code,
                "stdin", stdin
        );
        Map submitRes = webClient.post()
                .uri("/submissions?wait=true")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(submitBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String token = submitRes.get("token").toString();

        Map result;
        do {
            result = webClient.get()
                    .uri("/submissions/{token}?base64_encoded=false", token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } while ((int) ((Map) result.get("status")).get("id") <= 2);

        // 3️⃣ Map response
        Map status = (Map) result.get("status");

        RunCodeResponse response = new RunCodeResponse();
        response.setStdout((String) result.get("stdout"));
        response.setStderr((String) result.get("stderr"));
        response.setCompileOutput((String) result.get("compile_output"));
        response.setTime((String) result.get("time"));
        response.setMemory((Integer) result.get("memory"));
        response.setMessage((String) result.get("message"));
        response.setStatusId((Integer) status.get("id"));
        response.setStatus((String) status.get("description"));

        return response;
    }
}

