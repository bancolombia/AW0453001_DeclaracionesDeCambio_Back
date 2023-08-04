package co.com.bancolombia.declaraciones.libcommons.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MLEndeudamientoDTO {
    String documentType;
    Long documentNumber;
    Integer validationDigit;
    String compensationAccountCode;
    String financeEntityAccountNumber;
    String typeOperation;
    String operationExpenseIncome;
    String city;
    Date formDate;
    String consecutive;
    Date documentOldDate;
    String consecutiveOld;
    String loanOrGuaranteeNumber;
    String nameDebtorOrCreditor;
    int numeral;
    String contractedCurrency;
    BigDecimal contractedCurrencyValue;
    String currencyTrading;
    BigDecimal typeChangeToUsd;
    BigDecimal currencyTradingValue;
    BigDecimal giroCurrencyValue;
    BigDecimal usdValue;
}
