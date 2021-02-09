/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgOrganismoCoordinacionEscolar;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ocsPk", scope = SgOrganismoCESede.class)
public class SgOrganismoCESede implements Serializable {
             
    private static final long serialVersionUID = 1L;
    
    private Long ocsPk;

    private SgSede ocsSede;
    
    private SgOrganismoCoordinacionEscolar ocsOrgCoordinacionEscolar;
    
    private Boolean ocsConsejoConsultivo;
    
    private Boolean ocsFuncionando;
  
    private LocalDateTime ocsUltModFecha;
    
    private String ocsUltModUsuario;
    
    private Integer ocsVersion;

    public SgOrganismoCESede() {
        this.ocsConsejoConsultivo = Boolean.FALSE;
    }

    public SgOrganismoCESede(Long ocsPk) {
        this.ocsPk = ocsPk;
    }

    public Long getOcsPk() {
        return ocsPk;
    }

    public void setOcsPk(Long ocsPk) {
        this.ocsPk = ocsPk;
    }

    public SgSede getOcsSede() {
        return ocsSede;
    }

    public void setOcsSede(SgSede ocsSede) {
        this.ocsSede = ocsSede;
    }

    public SgOrganismoCoordinacionEscolar getOcsOrgCoordinacionEscolar() {
        return ocsOrgCoordinacionEscolar;
    }

    public void setOcsOrgCoordinacionEscolar(SgOrganismoCoordinacionEscolar ocsOrgCoordinacionEscolar) {
        this.ocsOrgCoordinacionEscolar = ocsOrgCoordinacionEscolar;
    }

    public Boolean getOcsConsejoConsultivo() {
        return ocsConsejoConsultivo;
    }

    public void setOcsConsejoConsultivo(Boolean ocsConsejoConsultivo) {
        this.ocsConsejoConsultivo = ocsConsejoConsultivo;
    }

    public Boolean getOcsFuncionando() {
        return ocsFuncionando;
    }

    public void setOcsFuncionando(Boolean ocsFuncionando) {
        this.ocsFuncionando = ocsFuncionando;
    }

    public LocalDateTime getOcsUltModFecha() {
        return ocsUltModFecha;
    }

    public void setOcsUltModFecha(LocalDateTime ocsUltModFecha) {
        this.ocsUltModFecha = ocsUltModFecha;
    }

    public String getOcsUltModUsuario() {
        return ocsUltModUsuario;
    }

    public void setOcsUltModUsuario(String ocsUltModUsuario) {
        this.ocsUltModUsuario = ocsUltModUsuario;
    }

    public Integer getOcsVersion() {
        return ocsVersion;
    }

    public void setOcsVersion(Integer ocsVersion) {
        this.ocsVersion = ocsVersion;
    }


    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocsPk != null ? ocsPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgOrganismoCESede)) {
            return false;
        }
        SgOrganismoCESede other = (SgOrganismoCESede) object;
        if ((this.ocsPk == null && other.ocsPk != null) || (this.ocsPk != null && !this.ocsPk.equals(other.ocsPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgOrganismoCoordinacionEscolar[ ocsPk=" + ocsPk + " ]";
    }
    
}
