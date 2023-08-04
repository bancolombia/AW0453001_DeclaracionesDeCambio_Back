package co.com.bancolombia.declaraciones.model.panama.entity;


import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;


@NoArgsConstructor

@Entity
@Table(name = "REGISTROCONTRAPARTES", schema = "SCHEMA_DECLAPANAMA")
public class RegistroContrapartes implements Serializable {
	private static final long serialVersionUID = -8360524332289140364L;

	@Id
	@GeneratedValue(generator = "SeqRegistroContrapartes")
	@SequenceGenerator(name = "SeqRegistroContrapartes", 
		sequenceName = "SCHEMA_DECLAPANAMA.SEQ_IDREGCONTRAPARTE", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ID_REGISTROCONTRAPARTE")
	private Long idRegistroContraparte;

	@Basic(optional = false)
	@Column(name = "NROCTACOMPENSACION")
	private String numeroCuentaCompensacion;

	@Basic(optional = false)
	@Column(name = "NOMBRE")
	private String nombre;

	@Basic(optional = false)
	@Column(name = "IDTIPOIDENTIFICACION")
	private Integer idTipoIdentificacion;

	@Basic(optional = false)
	@Column(name = "IDENTIFICACION")
	private String identificacion;

	@Basic(optional = false)
	@Column(name = "DIGITOVERIFICACION")
	private Character digitoVerificacion;

	@Basic
	@Column(name = "CODIGOBANREPUBLICA")
	private String codigoBancoRepublica;

	public Long getIdRegistroContraparte() {
		return idRegistroContraparte;
	}

	public void setIdRegistroContraparte(Long idRegistroContraparte) {
		this.idRegistroContraparte = idRegistroContraparte;
	}

	public String getNumeroCuentaCompensacion() {
		return numeroCuentaCompensacion;
	}

	public void setNumeroCuentaCompensacion(String numeroCuentaCompensacion) {
		this.numeroCuentaCompensacion = numeroCuentaCompensacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}

	public void setIdTipoIdentificacion(Integer idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public Character getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(Character digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	public String getCodigoBancoRepublica() {
		return codigoBancoRepublica;
	}

	public void setCodigoBancoRepublica(String codigoBancoRepublica) {
		this.codigoBancoRepublica = codigoBancoRepublica;
	}
}