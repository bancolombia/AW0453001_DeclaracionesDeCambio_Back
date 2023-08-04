package co.com.bancolombia.declaraciones.libcommons.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MassiveLoadErrorDTO {
    private String formName;
    private String fileName;
    private String cellDescription;
    private String errorDescription;
}
