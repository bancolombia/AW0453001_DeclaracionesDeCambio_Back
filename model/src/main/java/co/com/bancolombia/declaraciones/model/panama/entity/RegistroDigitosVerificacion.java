package co.com.bancolombia.declaraciones.model.panama.entity;


import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;


@NoArgsConstructor

@Entity
@Table(name = "REGISTRODIGITOSVERIFICACION", schema = "SCHEMA_DECLAPANAMA")
public class RegistroDigitosVerificacion implements Serializable {
	private static final long serialVersionUID = -859805389172225135L;

	@Id
	@GeneratedValue(generator = "SeqRegistroDigitosVerificacion")
	@SequenceGenerator(name = "SeqRegistroDigitosVerificacion", 
		sequenceName = "SCHEMA_DECLAPANAMA.SEQ_IDREGDIGITOVERIFICACION", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ID_REGISTRODIGITOVERIFICACION")
	private Long id;

	@Basic(optional = false)
	@Column(name = "NROCTACOMPENSACION")
	private String numeroCuentaCompensacion;

	@Basic(optional = false)
	@Column(name = "NIT")
	private String nit;

	@Basic(optional = false)
	@Column(name = "DIGITOVERIFICACION")
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