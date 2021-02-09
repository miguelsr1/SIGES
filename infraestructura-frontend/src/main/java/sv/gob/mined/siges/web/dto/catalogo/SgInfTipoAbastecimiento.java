/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tabPk", scope = SgInfTipoAbastecimiento.class)
public class SgInfTipoAbastecimiento implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tabPk;

    private String tabCodigo;

    private String tabNombre;

    private Boolean tabHabilitado;

    private LocalDateTime tabUltModFecha;

    private String tabUltModUsuario;

    private Integer tabVersion;
    
    
    public SgInfTipoAbastecimiento() {
        this.tabHabilitado = Boolean.TRUE;
    }

    public Long getTabPk() {
        return tabPk;
    }

    public void setTabPk(Long tabPk) {
        this.tabPk = tabPk;
    }

    public String getTabCodigo() {
        return tabCodigo;
    }

    public void setTabCodigo(String tabCodigo) {
        this.tabCodigo = tabCodigo;
    }

    public String getTabNombre() {
        return tabNombre;
    }

    public void setTabNombre(String tabNombre) {
        this.tabNombre = tabNombre;
    }

    public LocalDateTime getTabUltModFecha() {
        return tabUltModFecha;
    }

    public void setTabUltModFecha(LocalDateTime tabUltModFecha) {
        this.tabUltModFecha = tabUltModFecha;
    }

    public String getTabUltModUsuario() {
        return tabUltModUsuario;
    }

    public void setTabUltModUsuario(String tabUltModUsuario) {
        this.tabUltModUsuario = tabUltModUsuario;
    }

    public Integer getTabVersion() {
        return tabVersion;
    }

    public void setTabVersion(Integer tabVersion) {
        this.tabVersion = tabVersion;
    }

    
     public Boolean getTabHabilitado() {
        return tabHabilitado;
    }

    public void setTabHabilitado(Boolean tabHabilitado) {
        this.tabHabilitado = tabHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tabPk != null ? tabPk.hashCode() : 0);
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
        final SgInfTipoAbastecimiento other = (SgInfTipoAbastecimiento) obj;
        if (!Objects.equals(this.tabPk, other.tabPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfTipoAbastecimiento[ tabPk=" + tabPk + " ]";
    }
    
}
