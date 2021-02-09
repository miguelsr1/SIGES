package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroPersonaOrganismoAdministrativo extends FiltroPersona implements Serializable {

    private Long poaPk;
    private Long poaOrganismoAdministrativoEscolar;
    private Long sedeId;
    private List<String> codigosTOA;
    private Boolean poaHabilitado;
    private Long poaCargoOAEPk;
    

    public FiltroPersonaOrganismoAdministrativo() {

    }

    public Long getPoaPk() {
        return poaPk;
    }

    public void setPoaPk(Long poaPk) {
        this.poaPk = poaPk;
    }

    public Long getPoaOrganismoAdministrativoEscolar() {
        return poaOrganismoAdministrativoEscolar;
    }

    public void setPoaOrganismoAdministrativoEscolar(Long poaOrganismoAdministrativoEscolar) {
        this.poaOrganismoAdministrativoEscolar = poaOrganismoAdministrativoEscolar;
    }

    public Boolean getPoaHabilitado() {
        return poaHabilitado;
    }

    public void setPoaHabilitado(Boolean poaHabilitado) {
        this.poaHabilitado = poaHabilitado;
    }

    public Long getPoaCargoOAEPk() {
        return poaCargoOAEPk;
    }

    public void setPoaCargoOAEPk(Long poaCargoOAEPk) {
        this.poaCargoOAEPk = poaCargoOAEPk;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public List<String> getCodigosTOA() {
        return codigosTOA;
    }

    public void setCodigosTOA(List<String> codigosTOA) {
        this.codigosTOA = codigosTOA;
    }
}