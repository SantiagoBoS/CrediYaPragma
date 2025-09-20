package co.com.pragma.r2dbc.loan.loanlist.mapper;

import co.com.pragma.model.loan.loanlist.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loanlist.entity.LoanListEntity;

public class LoanListMapper {
    public static LoanList toDomain(LoanListEntity entity) {
        return LoanList.builder()
                .amount(entity.getAmount())
                .term(entity.getTerm())
                .email(entity.getEmail())
                .fullName(entity.getFirstName() + " " + entity.getLastName())
                .loanType(entity.getLoanType())
                .interestRate(entity.getInterestRate())
                .requestStatus(RequestStatus.valueOf(entity.getRequestStatus()))
                .baseSalary(entity.getBaseSalary())
                .monthlyPayment(entity.getMonthlyPayment())
                .build();
    }
}
