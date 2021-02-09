/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumTipoNotificacion;

@Entity
@Table(name = "sg_notificaciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nfsPk", scope = SgNotificacion.class)
public class SgNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nfs_pk")
    private Long nfsPk;

    @Column(name = "nfs_tipo")
    @Enumerated(value = EnumType.STRING)
    private EnumTipoNotificacion nfsTipo;

    @JoinColumn(name = "nfs_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede nfsSede;

    @JoinColumn(name = "nfs_seccion_fk", referencedColumnName = "sec_pk")
    @ManyToOne
    private SgSeccion nfsSeccion;

    @JoinColumn(name = "nfs_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante nfsEstudiante;

    @Size(max = 400)
    @Column(name = "nfs_notificacion", length = 400)
    private String nfsNotificacion;

    @Size(max = 100)
    @Column(name = "nfs_texto_breve", length = 100)
    private String nfsTextoBreve;

    @Column(name = "nfs_fecha")
    private LocalDate nfsFecha;

    @JoinColumn(name = "nfs_usuario_fk", referencedColumnName = "usu_pk")
    @ManyToOne
    private SgUsuario nfsUsuario;

    @Column(name = "nfs_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nfsUltModFecha;

    @Size(max = 45)
    @Column(name = "nfs_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String nfsUltModUsuario;

    @Column(name = "nfs_version")
    @Version
    private Integer nfsVersion;

    public SgNotificacion() {
    }

    public SgNotificacion(Long nfsPk) {
        this.nfsPk = nfsPk;
    }

    public Long getNfsPk() {
        return nfsPk;
    }

    public void setNfsPk(Long nfsPk) {
        this.nfsPk = nfsPk;
    }

    public EnumTipoNotificacion getNfsTipo() {
        return nfsTipo;
    }

    public void setNfsTipo(EnumTipoNotificacion nfsTipo) {
        this.nfsTipo = nfsTipo;
    }

    public SgSede getNfsSede() {
        return nfsSede;
    }

    public void setNfsSede(SgSede nfsSede) {
        this.nfsSede = nfsSede;
    }

    public SgSeccion getNfsSeccion() {
        return nfsSeccion;
    }

    public void setNfsSeccion(SgSeccion nfsSeccion) {
        this.nfsSeccion = nfsSeccion;
    }

    public SgEstudiante getNfsEstudiante() {
        return nfsEstudiante;
    }

    public void setNfsEstudiante(SgEstudiante nfsEstudiante) {
        this.nfsEstudiante = nfsEstudiante;
    }

    public String getNfsNotificacion() {
        return nfsNotificacion;
    }

    public void setNfsNotificacion(String nfsNotificacion) {
        this.nfsNotificacion = nfsNotificacion;
    }

    public String getNfsTextoBreve() {
        return nfsTextoBreve;
    }

    public void setNfsTextoBreve(String nfsTextoBreve) {
        this.nfsTextoBreve = nfsTextoBreve;
    }

    public LocalDate getNfsFecha() {
        return nfsFecha;
    }

    public void setNfsFecha(LocalDate nfsFecha) {
        this.nfsFecha = nfsFecha;
    }

    public SgUsuario getNfsUsuario() {
        return nfsUsuario;
    }

    public void setNfsUsuario(SgUsuario nfsUsuario) {
        this.nfsUsuario = nfsUsuario;
    }

    public LocalDateTime getNfsUltModFecha() {
        return nfsUltModFecha;
    }

    public void setNfsUltModFecha(LocalDateTime nfsUltModFecha) {
        this.nfsUltModFecha = nfsUltModFecha;
    }

    public String getNfsUltModUsuario() {
        return nfsUltModUsuario;
    }

    public void setNfsUltModUsuario(String nfsUltModUsuario) {
        this.nfsUltModUsuario = nfsUltModUsuario;
    }

    public Integer getNfsVersion() {
        return nfsVersion;
    }

    public void setNfsVersion(Integer nfsVersion) {
        this.nfsVersion = nfsVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nfsPk != null ? nfsPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNotificacion)) {
            return false;
        }
        SgNotificacion other = (SgNotificacion) object;
        if ((this.nfsPk == null && other.nfsPk != null) || (this.nfsPk != null && !this.nfsPk.equals(other.nfsPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNotificacion[ nfsPk=" + nfsPk + " ]";
    }

}
