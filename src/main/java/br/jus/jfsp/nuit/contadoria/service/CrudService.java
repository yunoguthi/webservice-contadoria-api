package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<T> {

    T create(T entity) throws RecordNotFoundException;

    void delete(Integer id) throws RecordNotFoundException;

    T update(T entity) throws RecordNotFoundException;

    T read(Integer id) throws RecordNotFoundException;

    Page<T> findAll(Pageable pageable);

    Iterable<T> getAll();

    void deleteAll();

    T save(T entity) throws RecordNotFoundException;
}
