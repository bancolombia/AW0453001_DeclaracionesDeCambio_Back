package co.com.bancolombia.declaraciones.model.panama.entity.administracion;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.NoArgsConstructor;


@NoArgsConstructor

@Entity
@Table(name = "NUMERALES", schema = "SCHEMA_DECLAMAESTROS")
public class Numerales implements Serializable {

    private static final long serialVersionUID = -8596484511002564533L;

    @Id
    @GeneratedValue(generator = "SeqNumerales")
    @SequenceGenerator(name = "SeqNumerales", sequenceName = "SCHEMA_DECLAMAESTROS.SEQ_NUMERALES", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDNUMERAL")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "CODIGO")
    private Integer codigo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "ISEXPORTACION")
    private Character isExportacion;

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

    @Column(name = "TRANSMISIONDIAN")
    private Character transmisionDian;

    @Column(name = "TRANSMISIONBANREP")
    private Character transmisionBanRep;

    @ManyToOne
    @JoinColumn(name = "IDOPERACION", referencedColumnName = "IDOPERACION")
    private Operaciones idOperacion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Character getIsExportacion() {
		return isExportacion;
	}

	public void setIsExportacion(Character isExportacion) {
		this.isExportacion = isExportacion;
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

	public Character getTransmisionDian() {
		return transmisionDian;
	}

	public void setTransmisionDian(Character transmisionDian) {
		this.transmisionDian = transmisionDian;
	}

	public Character getTransmisionBanRep() {
		return transmisionBanRep;
	}

	public void setTransmisionBanRep(Character transmisionBanRep) {
		this.transmisionBanRep = transmisionBanRep;
	}

	public Operaciones getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(Operaciones idOperacion) {
		this.idOperacion = idOperacion;
	}
}
