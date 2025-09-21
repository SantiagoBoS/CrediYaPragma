package co.com.pragma.r2dbc.loan.loanupdate.customrepository;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LoanUpdateCustomRepositoryImpl implements LoanUpdateCustomRepository {

    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Mono<LoanEntity> updateLoanStatus(UUID publicId, RequestStatus status, String advisorId) {
        //Para realizar la actualizacion a la base de datos
        return entityTemplate.select(LoanEntity.class)
                .matching(Query.query(Criteria.where("public_id").is(publicId)))
                .one()
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.LOAN_NOT_FOUND)))
                .flatMap(entity -> {
                    entity.setStatus(status);
                    entity.setAdvisorId(advisorId);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return entityTemplate.update(entity);
                })
                .onErrorResume(throwable -> {
                    if (throwable instanceof BusinessException) {
                        return Mono.error(throwable);
                    }
                    return Mono.error(new BusinessException(AppMessages.LOAN_UPDATE_ERROR));
                });
    }
}