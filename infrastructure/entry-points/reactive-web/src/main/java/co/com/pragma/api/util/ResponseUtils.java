package co.com.pragma.api.util;

import co.com.pragma.api.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ResponseUtils {
    //Manejo de las respuestas
    public static SuccessResponseBuilder success() {
        return new SuccessResponseBuilder();
    }

    public static class SuccessResponseBuilder {
        private HttpStatus status = HttpStatus.OK;
        private String code;
        private String message;

        public SuccessResponseBuilder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public SuccessResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public SuccessResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public <T> Mono<ServerResponse> withData(T data) {
            ApiResponse<T> response = ApiResponse.<T>builder()
                    .code(code).message(message)
                    .data(data).build();

            return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        }
    }
}