package co.com.bancolombia.declaraciones.libcommons.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@ToString
public class DocumentosImportacionDTO implements Serializable {
    private static final long serialVersionUID = 3134709429352608873L;
    private Integer idDocumento;
    private String anho;
    private String numero;
    private BigDecimal valorUsd;
    private String idFormulario1;
	public Integer getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getAnho() {
		return anho;
	}
	public void setAnho(String anho) {
		this.anho = anho;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public BigDecimal getValorUsd() {
		return valorUsd;
	}
	public void setValorUsd(BigDecimal valorUsd) {
		this.valorUsd = valorUsd;
	}
	public String getIdFormulario1() {
		return idFormulario1;
	}
	public void setIdFormulario1(String idFormulario1) {
		this.idFormulario1 = idFormulario1;
	}
}
