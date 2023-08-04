package co.com.bancolombia.declaraciones.libcommons.dto;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.ToString;



@NoArgsConstructor
@ToString

public class RegistroDigitosVerificacionDTO
		implements Serializable {
	private static final long serialVersionUID = -7713061856787202270L;
	private Long id;
	private String numeroCuentaCompensacion;
	private String nit;
	private Character digitoVerificacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroCuentaCompensacion() {
		return numeroCuentaCompensacion;
	}

	public void setNumeroCuentaCompensacion(String numeroCuentaCompensacion) {
		this.numeroCuentaCompensacion = numeroCuentaCompensacion;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public Character getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(Character digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}
}
