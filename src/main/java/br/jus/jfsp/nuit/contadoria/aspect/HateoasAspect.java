package br.jus.jfsp.nuit.contadoria.aspect;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Aspect
@Component
public class HateoasAspect implements Ordered {

    @Around("@annotation(Hateoas)")
    public Object aroundHateoas(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object ret = proceedingJoinPoint.proceed();

        execute(ret);

        return ret;
    }

    private void execute(Object ret) throws RecordNotFoundException {
        if (ret instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) ret;
            Object body = responseEntity.getBody();

            if (body instanceof ApplyHateoas) {
                applyHateoas((ApplyHateoas) body);
            } else if (body instanceof Page) {
                applyHateoas((Page<ApplyHateoas>) body);
            } else if (body instanceof PagedModel) {
                applyHateoas((PagedModel<EntityModel<ApplyHateoas>>) body);
            }
        }
    }

    private void applyHateoas(PagedModel<EntityModel<ApplyHateoas>> pagedModel) throws RecordNotFoundException {
        Iterator<EntityModel<ApplyHateoas>> it = pagedModel.iterator();
        while (it.hasNext()) {
            it.next().getContent().apply();
        }
    }

    private void applyHateoas(Page<ApplyHateoas> page) throws RecordNotFoundException {
        Iterator<ApplyHateoas> it = page.iterator();
        while (it.hasNext()) {
            it.next().apply();
        }
    }

    private void applyHateoas(ApplyHateoas applyHateoas) throws RecordNotFoundException {
        applyHateoas.apply();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
