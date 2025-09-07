package co.com.pragma.api.dto;

import co.com.pragma.api.util.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldBuildApiResponseWithData() {
        String code = UserUtils.CREATE_CODE;
        String message = UserUtils.CREATE_MESSAGE;
        String data = "Usuario123";

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(code).message(message)
                .data(data).build();

        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getErrors()).isNull();
    }

    @Test
    void shouldBuildApiResponseWithErrors() {
        List<String> errors = List.of("Email invalido", "name must not be blank");

        ApiResponse<Object> response = ApiResponse.builder()
                .code(UserUtils.VALIDATION_CODE)
                .message(UserUtils.VALIDATION_ERROR)
                .errors(errors)
                .build();

        assertThat(response.getErrors()).hasSize(2);
        assertThat(response.getErrors()).anyMatch(e -> e.toString().equals("Email invalido"));
        assertThat(response.getData()).isNull();
    }

    @Test
    void shouldNotSerializeNullFields() throws JsonProcessingException {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(UserUtils.SUCCESS_CODE)
                .message(UserUtils.SUCCESS_MESSAGE)
                .build();

        String json = objectMapper.writeValueAsString(response);

        assertThat(json).contains("200.00");
        assertThat(json).contains("OK");
        assertThat(json).doesNotContain("data");
        assertThat(json).doesNotContain("errors");
    }

    @Test
    void shouldUseSettersAndGetters() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(UserUtils.INTERNAL_ERROR_CODE);
        response.setMessage(UserUtils.INTERNAL_ERROR_MESSAGE);

        assertThat(response.getCode()).isEqualTo("500.01");
        assertThat(response.getMessage()).isEqualTo("Error interno del servidor");
    }
}
