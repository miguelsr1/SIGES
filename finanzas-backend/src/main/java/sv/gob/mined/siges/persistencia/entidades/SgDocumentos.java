/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoDocumento;
import sv.gob.mined.siges.enumerados.TipoDocumento;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_documentos_presupuesto", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "docPk", scope = SgDocumentos.class)
@Audited
/**
 * Entidad correspondiente a los documentos del presupuesto escolar.
 */
public class SgDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "doc_pk", nullable = false)
    private Long docPk;

    @JoinColumn(name = "doc_presupuesto_fk", referencedColumnName = "pes_pk")
    @ManyToOne
    private SgPresupuestoEscolar docPresupuestoFk;

    @JoinColumn(name = "doc_archivo_fk", referencedColumnName = "ach_pk")
    @ManyToOne
    private SgArchivo docArchivoFk;

    @Column(name = "doc_tipo_documento", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TipoDocumento docTipoDocumento;

    @Column(name = "doc_estado_documento", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumEstadoDocumento docEstadoDocumento;

    @Column(name = "doc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime docUltModFecha;

    @Size(max = 45)
    @Column(name = "doc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String docUltModUsuario;

    @Column(name = "doc_version")
    @Version
    private Integer docVersion;

    public SgDocumentos() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        //this.docNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.docNombre);
    }

    public Long getDocPk() {
        return docPk;
    }

    public void setDocPk(Long docPk) {
        this.docPk = docPk;
    }

    public SgPresupuestoEscolar getDocPresupuestoFk() {
        return docPresupuestoFk;
    }

    public void setDocPresupuestoFk(SgPresupuestoEscolar docPresupuestoFk) {
        this.docPresupuestoFk = docPresupuestoFk;
    }

    public SgArchivo getDocArchivoFk() {
        return docArchivoFk;
    }

    public void setDocArchivoFk(SgArchivo docArchivoFk) {
        this.docArchivoFk = docArchivoFk;
    }

    public TipoDocumento getDocTipoDocumento() {
        return docTipoDocumento;
    }

    public void setDocTipoDocumento(TipoDocumento docTipoDocumento) {
        this.docTipoDocumento = docTipoDocumento;
    }

    public EnumEstadoDocumento getDocEstadoDocumento() {
        return docEstadoDocumento;
    }

    public void setDocEstadoDocumento(EnumEstadoDocumento docEstadoDocumento) {
        this.docEstadoDocumento = docEstadoDocumento;
    }

    public LocalDateTime getDocUltModFecha() {
        return docUltModFecha;
    }

    public void setDocUltModFecha(LocalDateTime docUltModFecha) {
        this.docUltModFecha = docUltModFecha;
    }

    public String getDocUltModUsuario() {
        return docUltModUsuario;
    }

    public void setDocUltModUsuario(String docUltModUsuario) {
        this.docUltModUsuario = docUltModUsuario;
    }

    public Integer getDocVersion() {
        return docVersion;
    }

    public void setDocVersion(Integer docVersion) {
        this.docVersion = docVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.docPk);
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
        final SgDocumentos other = (SgDocumentos) obj;
        if (!Objects.equals(this.docPk, other.docPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDocumentos{" + "docPk=" + docPk + ", docUltModFecha=" + docUltModFecha + ", docUltModUsuario=" + docUltModUsuario + ", docVersion=" + docVersion + '}';
    }

}
