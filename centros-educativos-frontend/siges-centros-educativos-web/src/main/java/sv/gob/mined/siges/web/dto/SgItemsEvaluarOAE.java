/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgItemEvaluarOrganismo;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "oaiPk", scope = SgItemsEvaluarOAE.class)
public class SgItemsEvaluarOAE implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long oaiPk;

    private SgItemEvaluarOrganismo oaiItemFk;

    private SgOrganismoAdministracionEscolar oaiOrganismoFk;
    
    private LocalDateTime oaiUltModFecha;
    
    private String oaiUltModUsuario;
    
    private Integer oaiVersion;
    
    public SgItemsEvaluarOAE() {
    }

    public Long getOaiPk() {
        return oaiPk;
    }

    public void setOaiPk(Long oaiPk) {
        this.oaiPk = oaiPk;
    }

    public SgItemEvaluarOrganismo getOaiItemFk() {
        return oaiItemFk;
    }

    public void setOaiItemFk(SgItemEvaluarOrganismo oaiItemFk) {
        this.oaiItemFk = oaiItemFk;
    }

    public SgOrganismoAdministracionEscolar getOaiOrganismoFk() {
        return oaiOrganismoFk;
    }

    public void setOaiOrganismoFk(SgOrganismoAdministracionEscolar oaiOrganismoFk) {
        this.oaiOrganismoFk = oaiOrganismoFk;
    }

    public LocalDateTime getOaiUltModFecha() {
        return oaiUltModFecha;
    }

    public void setOaiUltModFecha(LocalDateTime oaiUltModFecha) {
        this.oaiUltModFecha = oaiUltModFecha;
    }

    public String getOaiUltModUsuario() {
        return oaiUltModUsuario;
    }

    public void setOaiUltModUsuario(String oaiUltModUsuario) {
        this.oaiUltModUsuario = oaiUltModUsuario;
    }

    public Integer getOaiVersion() {
        return oaiVersion;
    }

    public void setOaiVersion(Integer oaiVersion) {
        this.oaiVersion = oaiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.oaiPk);
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
        final SgItemsEvaluarOAE other = (SgItemsEvaluarOAE) obj;
        if (!Objects.equals(this.oaiPk, other.oaiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgItemsEvaluarOAE{" + "oaiPk=" + oaiPk + ", oaiItemFk=" + oaiItemFk + ", oaiOrganismoFk=" + oaiOrganismoFk + ", oaiUltModFecha=" + oaiUltModFecha + ", oaiUltModUsuario=" + oaiUltModUsuario + ", oaiVersion=" + oaiVersion + '}';
    }
    
}
