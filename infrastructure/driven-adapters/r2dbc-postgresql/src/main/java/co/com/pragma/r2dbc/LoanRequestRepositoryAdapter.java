package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.Loan;
import co.com.pragma.r2dbc.entity.LoanRequestEntity;

public class LoanRequestRepositoryAdapter extends ReactiveAdapterOperations<Loan, LoanRequestEntity, String, LoanRequestReactiveRepository> implements LoanRequestRepository {
}
