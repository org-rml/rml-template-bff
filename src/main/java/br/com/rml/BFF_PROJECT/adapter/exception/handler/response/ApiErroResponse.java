package br.com.rml.BFF_PROJECT.adapter.exception.handler.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErroResponse {

    private final OffsetDateTime timestamp = OffsetDateTime.now();
    private HttpStatus status;
    private String codigoErro;
    private String mensagem;
    private String mensagemDetalhada;
    private List<String> erros;

    public ApiErroResponse(HttpStatus status) {
        this.status = status;
        this.codigoErro = String.valueOf(status.value());
    }

    public ApiErroResponse(HttpStatus status, String mensagem, Throwable ex) {
        this(status);
        this.mensagem = mensagem;
        this.mensagemDetalhada = ex.getLocalizedMessage();
    }

    public ApiErroResponse(HttpStatus status, Throwable ex) {
        this(status);
        this.mensagem = ex.getLocalizedMessage();
    }

    public void addErro(String mensagem) {
        if (this.erros == null) this.erros = new ArrayList<>();
        this.erros.add(mensagem);
    }

    public void addValidationErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        fieldErrors.forEach(e -> addErro(e.getField() + ": " + e.getDefaultMessage()));
    }

    public void addValidacaoErro(List<org.springframework.validation.ObjectError> globalErrors) {
        globalErrors.forEach(e -> addErro(e.getDefaultMessage()));
    }

    public HttpStatus getStatus() { return status; }
    public String getCodigoErro() { return codigoErro; }
    public String getMensagem() { return mensagem; }
    public String getMensagemDetalhada() { return mensagemDetalhada; }
    public List<String> getErros() { return erros; }
    public OffsetDateTime getTimestamp() { return timestamp; }

    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public void setMensagemDetalhada(String mensagemDetalhada) { this.mensagemDetalhada = mensagemDetalhada; }
}
