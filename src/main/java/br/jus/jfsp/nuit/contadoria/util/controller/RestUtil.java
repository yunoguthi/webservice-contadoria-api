package br.jus.jfsp.nuit.contadoria.util.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

public final class RestUtil {

    public static <T, C, K> ResponseEntity<T> createWithLocation(Class<C> controllerClass, T to) {
        try {
            final URI uri = MvcUriComponentsBuilder.fromController(controllerClass)
                    .path("/{id}")
                    .buildAndExpand((K) getId(to))
                    .toUri();

            return ResponseEntity.created(uri).body(to);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <K, T> K getId(T to) throws InvocationTargetException, IllegalAccessException {
        Method getterMethod = Arrays.stream(to.getClass().getMethods())
                .filter(method -> method.getName().equals("getId"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No getter method related to Id field"));

        return (K) getterMethod.invoke(to);
    }
}
