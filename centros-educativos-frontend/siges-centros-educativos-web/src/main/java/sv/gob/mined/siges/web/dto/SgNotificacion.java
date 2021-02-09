/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.EnumTipoNotificacion;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nfsPk", scope = SgNotificacion.class)
public class SgNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long nfsPk;
    
    private EnumTipoNotificacion nfsTipo;
    
    private SgSede nfsSede;
    
    private SgSeccion nfsSeccion;
    
    private SgEstudiante nfsEstudiante;
    
    private String nfsNotificacion;
    
    private String nfsTextoBreve;
    
    private LocalDate nfsFecha;
    
    private SgUsuario nfsUsuario;
    
    private LocalDateTime nfsUltModFecha;
    
    private String nfsUltModUsuario;
    
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
