package co.com.bancolombia.declaraciones.libcommons.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MLExportacionesDto {
    String documentType;
    Long documentNumber;
    Integer validationDigit;
    String socialReasonName;
    String compensationAccountCode;
    String financeEntityAccountNumber;
    String typeOperation;
    Date formDate;
    String consecutive;
    Date documentOldDate;
    String consecutiveOld;
    String refundCurrencyCode;
    BigDecimal refundCurrencyValue;
    BigDecimal typeChangeToUsd;
    int numeral;
    String customsOfficerNumberDocument;
    BigDecimal refundUsdValue;
    BigDecimal numeralExchangeDeductions;
    String customsOfficerInformation;
    String observations;
}
