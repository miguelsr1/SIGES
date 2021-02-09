/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "purPk", scope = SgUsuarioRol.class)
public class SgUsuarioRol implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long purPk;
    
    private SgUsuario purUsuario;
    
    private SgRol purRol;
    
    private SeguridadAmbitos purAmbito;
    
    private SgContexto purContexto;
    
    private LocalDateTime purUltModFecha;
    
    private String purUltModUsuario;
    
    private Integer purVersion;

    public SgUsuarioRol() {
    }

    public SgUsuarioRol(Long purPk) {
        this.purPk = purPk;
    }

    public Long getPurPk() {
        return purPk;
    }

    public void setPurPk(Long purPk) {
        this.purPk = purPk;
    }

    public SgUsuario getPurUsuario() {
        return purUsuario;
    }

    public void setPurUsuario(SgUsuario purUsuario) {
        this.purUsuario = purUsuario;
    }

  

    public SgRol getPurRol() {
        return purRol;
    }

    public void setPurRol(SgRol purRol) {
        this.purRol = purRol;
    }

    public SeguridadAmbitos getPurAmbito() {
        return purAmbito;
    }

    public void setPurAmbito(SeguridadAmbitos purAmbito) {
        this.purAmbito = purAmbito;
    }

    public LocalDateTime getPurUltModFecha() {
        return purUltModFecha;
    }

    public void setPurUltModFecha(LocalDateTime purUltModFecha) {
        this.purUltModFecha = purUltModFecha;
    }

    public String getPurUltModUsuario() {
        return purUltModUsuario;
    }

    public void setPurUltModUsuario(String purUltModUsuario) {
        this.purUltModUsuario = purUltModUsuario;
    }

    public Integer getPurVersion() {
        return purVersion;
    }

    public void setPurVersion(Integer purVersion) {
        this.purVersion = purVersion;
    }

    public SgContexto getPurContexto() {
        return purContexto;
    }

    public void setPurContexto(SgContexto purContexto) {
        this.purContexto = purContexto;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purPk != null ? purPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgUsuarioRol)) {
            return false;
        }
        SgUsuarioRol other = (SgUsuarioRol) object;
        if ((this.purPk == null && other.purPk != null) || (this.purPk != null && !this.purPk.equals(other.purPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgUsuarioRol[ purPk=" + purPk + " ]";
    }
    
}
