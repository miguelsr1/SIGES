/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.centros_educativos.EnumAmbito;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgContexto.class)
public class SgContexto implements Serializable {

    private static final long serialVersionUID = 1L;

    
    private Long conPk;

    private LocalDateTime conUltModFecha;

    private String conUltModUsuario;

    private Integer conVersion;

    private EnumAmbito conAmbito;

    private Long contextoId;
    
    private SgReglaContexto conRegla;
   

    public SgContexto() {
    }

    public SgContexto(Long conPk) {
        this.conPk = conPk;
    }

    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    public EnumAmbito getConAmbito() {
        return conAmbito;
    }

    public void setConAmbito(EnumAmbito conAmbito) {
        this.conAmbito = conAmbito;
    }

    public Long getContextoId() {
        return contextoId;
    }

    public void setContextoId(Long contextoId) {
        this.contextoId = contextoId;
    }

    public SgReglaContexto getConRegla() {
        return conRegla;
    }

    public void setConRegla(SgReglaContexto conRegla) {
        this.conRegla = conRegla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conPk != null ? conPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgContexto)) {
            return false;
        }
        SgContexto other = (SgContexto) object;
        if ((this.conPk == null && other.conPk != null) || (this.conPk != null && !this.conPk.equals(other.conPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgContexto[ conPk=" + conPk + " ]";
    }

}
