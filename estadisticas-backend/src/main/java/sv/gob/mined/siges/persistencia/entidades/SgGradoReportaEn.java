/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_grado_reporta_en", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "repPk", scope = SgGradoReportaEn.class)
@Audited
public class SgGradoReportaEn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rep_pk", nullable = false)
    private Long repPk;

    @Column(name = "rep_grado_origen")
    private Long repGradoOrigen;

    @Column(name = "rep_grado_destino")
    private Long repGradoDestino;

    @JoinColumn(name = "rep_extraccion")
    @ManyToOne
    private SgExtraccion repExtraccion;

    @Column(name = "rep_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime repUltModFecha;

    @Size(max = 45)
    @Column(name = "rep_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String repUltModUsuario;

    @Column(name = "rep_version")
    @Version
    private Integer repVersion;

    public SgGradoReportaEn() {
    }

    public Long getRepPk() {
        return repPk;
    }

    public void setRepPk(Long repPk) {
        this.repPk = repPk;
    }

    public Long getRepGradoOrigen() {
        return repGradoOrigen;
    }

    public void setRepGradoOrigen(Long repGradoOrigen) {
        this.repGradoOrigen = repGradoOrigen;
    }

    public Long getRepGradoDestino() {
        return repGradoDestino;
    }

    public void setRepGradoDestino(Long repGradoDestino) {
        this.repGradoDestino = repGradoDestino;
    }

    public SgExtraccion getRepExtraccion() {
        return repExtraccion;
    }

    public void setRepExtraccion(SgExtraccion repExtraccion) {
        this.repExtraccion = repExtraccion;
    }

    public LocalDateTime getRepUltModFecha() {
        return repUltModFecha;
    }

    public void setRepUltModFecha(LocalDateTime repUltModFecha) {
        this.repUltModFecha = repUltModFecha;
    }

    public String getRepUltModUsuario() {
        return repUltModUsuario;
    }

    public void setRepUltModUsuario(String repUltModUsuario) {
        this.repUltModUsuario = repUltModUsuario;
    }

    public Integer getRepVersion() {
        return repVersion;
    }

    public void setRepVersion(Integer repVersion) {
        this.repVersion = repVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.repPk);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgGradoReportaEn other = (SgGradoReportaEn) obj;
        if (!Objects.equals(this.repPk, other.repPk)) {
            return false;
        }
        return true;
    }

}
