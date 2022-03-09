package br.jus.jfsp.nuit.contadoria.aspect;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;

@FunctionalInterface
public interface ApplyHateoas<T> {

    T apply() throws RecordNotFoundException;
}
