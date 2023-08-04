package co.com.bancolombia.declaraciones.libcommons.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MLImportacionesDTO {
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
     String giroCurrencyCode;
     BigDecimal typeChangeToUsd;
     int numeral;
     BigDecimal giroCurrencyValue;
     BigDecimal usdValue;
     String giroCurrencyCodeTwo;
     BigDecimal typeChangeToUsdTwo;
     int numeralTwo;
     BigDecimal giroCurrencyValueTwo;
     BigDecimal usdValueTwo;
     String customsOfficerNumberDocument;
     BigDecimal usdFobValue;
     String customsOfficerInformation;
     String observations;
}
