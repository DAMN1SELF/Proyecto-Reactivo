package controller.advice;

import org.springframework.dao.DuplicateKeyException;           // <-- el de Spring, NO el de Mongo
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    // 409 por registro duplicado (índice único, alta duplicada, etc.)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(DuplicateKeyException ex,
                                                         ServerHttpRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Registro duplicado");
        pd.setDetail(ex.getMessage() != null ? ex.getMessage() : "codProducto ya existe");
        pd.setInstance(URI.create(request.getPath().toString()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    // 409 por reglas de negocio (ej: "Stock insuficiente")
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleIllegalState(IllegalStateException ex,
                                                            ServerHttpRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Operación inválida");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getPath().toString()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    // 400 por validaciones @Valid (si las usas en tus DTO)
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ProblemDetail> handleValidation(WebExchangeBindException ex,
                                                          ServerHttpRequest request) {
        Map<String,String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "valor inválido",
                        (a,b) -> a
                ));

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Datos inválidos");
        pd.setDetail("Revisa los campos enviados.");
        pd.setProperty("errors", errors);
        pd.setInstance(URI.create(request.getPath().toString()));
        return ResponseEntity.badRequest().body(pd);
    }
}
