package br.com.rml.BFF_PROJECT.adapter.exception.handler;

import br.com.rml.BFF_PROJECT.adapter.exception.handler.response.ApiErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

/**
 * Exception Handler centralizado — trata erros do domínio e erros internos do Spring.
 * TODO: Adicionar handlers específicos do domínio conforme necessário.
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String erro = ex.getParameterName() + " parametro nao informado";
        return buildResponseEntity(new ApiErroResponse(BAD_REQUEST, erro, ex), ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        var builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type nao suportado. Suportados: ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(
                new ApiErroResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex), ex);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem("Erro de validacao");
        apiErro.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiErro.addValidacaoErro(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(jakarta.validation.ConstraintViolationException ex) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem("Erro de validacao");
        return buildResponseEntity(apiErro, ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return buildResponseEntity(new ApiErroResponse(BAD_REQUEST, "Malformed JSON request", ex), ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return buildResponseEntity(new ApiErroResponse(INTERNAL_SERVER_ERROR, "Erro ao serializar resposta JSON", ex), ex);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem(String.format("Rota nao encontrada: %s %s", ex.getHttpMethod(), ex.getRequestURL()));
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem(String.format("Parametro '%s' com valor '%s' invalido para o tipo '%s'",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> handleConversionFailed(ConversionFailedException ex) {
        return buildResponseEntity(new ApiErroResponse(BAD_REQUEST, ex), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return buildResponseEntity(new ApiErroResponse(INTERNAL_SERVER_ERROR, ex), ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErroResponse apiErro, Exception ex) {
        LOGGER.error("RestExceptionHandler — status: {}, mensagem: {}", apiErro.getCodigoErro(), apiErro.getMensagem(), ex);
        return new ResponseEntity<>(apiErro, apiErro.getStatus());
    }
}
