/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;

@Entity
@Table(name = "sg_calificaciones",uniqueConstraints = {
    @UniqueConstraint(name = "cal_codigo_uk", columnNames = {"cal_codigo"}),
    @UniqueConstraint(name = "cal_orden_escala_uk", columnNames = {"cal_orden","cal_escala_fk"})},  schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "calPk", scope = SgCalificacion.class) 
public class SgCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cal_pk")
    private Long calPk;

    @Size(max = 45)
    @Column(name = "cal_codigo", length = 45)
    @AtributoCodigo
    private String calCodigo;

    @Size(max = 25)
    @Column(name = "cal_valor", length = 25)
    private String calValor;
    
    @Size(max = 25)
    @Column(name = "cal_valor_certificado", length = 25)
    private String calValorEnCertificado;

    @Column(name = "cal_orden")
    private Integer calOrden;

    @Column(name = "cal_habilitado")
    @AtributoHabilitado
    private Boolean calHabilitado;

    @Column(name = "cal_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime calUltModFecha;

    @Size(max = 45)
    @Column(name = "cal_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String calUltModUsuario;

    @Column(name = "cal_version")
    @Version
    private Integer calVersion;

    @JoinColumn(name = "cal_escala_fk", referencedColumnName = "eca_pk")
    @ManyToOne
    private SgEscalaCalificacion calEscala;

    public SgCalificacion() {
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public String getCalValor() {
        return calValor;
    }

    public void setCalValor(String calValor) {
        this.calValor = calValor;
    }

    public Integer getCalOrden() {
        return calOrden;
    }

    public void setCalOrden(Integer calOrden) {
        this.calOrden = calOrden;
    }

    public Boolean getCalHabilitado() {
        return calHabilitado;
    }

    public void setCalHabilitado(Boolean calHabilitado) {
        this.calHabilitado = calHabilitado;
    }

    public LocalDateTime getCalUltModFecha() {
        return calUltModFecha;
    }

    public void setCalUltModFecha(LocalDateTime calUltModFecha) {
        this.calUltModFecha = calUltModFecha;
    }

    public String getCalUltModUsuario() {
        return calUltModUsuario;
    }

    public void setCalUltModUsuario(String calUltModUsuario) {
        this.calUltModUsuario = calUltModUsuario;
    }

    public Integer getCalVersion() {
        return calVersion;
    }

    public void setCalVersion(Integer calVersion) {
        this.calVersion = calVersion;
    }
    
    public SgEscalaCalificacion getCalEscala() {
        return calEscala;
    }

    public void setCalEscala(SgEscalaCalificacion calEscala) {
        this.calEscala = calEscala;
    }

    public String getCalCodigo() {
        return calCodigo;
    }

    public void setCalCodigo(String calCodigo) {
        this.calCodigo = calCodigo;
    }

    public String getCalValorEnCertificado() {
        return calValorEnCertificado;
    }

    public void setCalValorEnCertificado(String calValorEnCertificado) {
        this.calValorEnCertificado = calValorEnCertificado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calPk != null ? calPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCalificacion)) {
            return false;
        }
        SgCalificacion other = (SgCalificacion) object;
        if ((this.calPk == null && other.calPk != null) || (this.calPk != null && !this.calPk.equals(other.calPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCalificacion[ calPk=" + calPk + " ]";
    }

}
