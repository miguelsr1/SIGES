/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "autPk", scope = SgAutorizacionEdicionPresupuesto.class)
public class SgAutorizacionEdicionPresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long autPk;
    private SgPresupuestoEscolar autPresupuestoFk;
    private SgUsuario autUsuarioValidadoFk;

    private LocalDateTime autUltMod;
    private String autUltUsuario;
    private Integer autVersion;

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public Long getAutPk() {
        return autPk;
    }

    public void setAutPk(Long autPk) {
        this.autPk = autPk;
    }

    public SgPresupuestoEscolar getAutPresupuestoFk() {
        return autPresupuestoFk;
    }

    public void setAutPresupuestoFk(SgPresupuestoEscolar autPresupuestoFk) {
        this.autPresupuestoFk = autPresupuestoFk;
    }

    public SgUsuario getAutUsuarioValidadoFk() {
        return autUsuarioValidadoFk;
    }

    public void setAutUsuarioValidadoFk(SgUsuario autUsuarioValidadoFk) {
        this.autUsuarioValidadoFk = autUsuarioValidadoFk;
    }

    public LocalDateTime getAutUltMod() {
        return autUltMod;
    }

    public void setAutUltMod(LocalDateTime autUltMod) {
        this.autUltMod = autUltMod;
    }

    public String getAutUltUsuario() {
        return autUltUsuario;
    }

    public void setAutUltUsuario(String autUltUsuario) {
        this.autUltUsuario = autUltUsuario;
    }

    public Integer getAutVersion() {
        return autVersion;
    }

    public void setAutVersion(Integer autVersion) {
        this.autVersion = autVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.autPk);
        return hash;
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
        final SgAutorizacionEdicionPresupuesto other = (SgAutorizacionEdicionPresupuesto) obj;
        if (!Objects.equals(this.autPk, other.autPk)) {
            return false;
        }
        return true;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SgAutorizacionEdicionPresupuesto[ autPk=" + autPk + " ]";
    }

}  // </editor-fold>
