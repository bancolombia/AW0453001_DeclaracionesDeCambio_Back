package co.com.bancolombia.declaraciones.model.panama.entity.administracion;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.NoArgsConstructor;


@NoArgsConstructor

@Entity
@Table(name = "MONEDAS", schema = "SCHEMA_DECLAMAESTROS")
public class Monedas implements Serializable {

    private static final long serialVersionUID = -6918189268405385188L;

    @Id
    @GeneratedValue(generator = "SeqMonedas")
    @SequenceGenerator(name = "SeqMonedas", sequenceName = "SCHEMA_DECLAMAESTROS.SEQ_MONEDAS", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDMONEDA")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "FECHAACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @Basic(optional = false)
    @Column(name = "HORAACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaActualizacion;

    @Basic(optional = false)
    @Column(name = "ESTADO")
    private Character estado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getHoraActualizacion() {
		return horaActualizacion;
	}

	public void setHoraActualizacion(Date horaActualizacion) {
		this.horaActualizacion = horaActualizacion;
	}

	public Character getEstado() {
		return estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
	}
}
