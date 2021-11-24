package br.jus.jfsp.nuit.contadoria.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 2960267921042373135L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime date;

    private int status;

    private String message;

    private String detail;
}
