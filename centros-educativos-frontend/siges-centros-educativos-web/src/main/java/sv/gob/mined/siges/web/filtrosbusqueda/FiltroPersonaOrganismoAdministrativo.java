package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroPersonaOrganismoAdministrativo extends FiltroPersona implements Serializable {

    private Long poaPk;
    private Long poaOrganismoAdministrativoEscolar;
    private Boolean poaHabilitado;
    private Long poaCargoOAEPk;
    private Boolean incluirDatoHabiltadoRemplazo;

    public FiltroPersonaOrganismoAdministrativo() {
        super();
        poaHabilitado=Boolean.TRUE;
    }
    
    public Long getPoaPk() {
        return poaPk;
    }

    public void setPoaPk(Long poaPk) {
        this.poaPk = poaPk;
    }

    public Boolean getPoaHabilitado() {
        return poaHabilitado;
    }

    public void setPoaHabilitado(Boolean poaHabilitado) {
        this.poaHabilitado = poaHabilitado;
    }
    
    public Long getPoaOrganismoAdministrativoEscolar() {
        return poaOrganismoAdministrativoEscolar;
    }

    public void setPoaOrganismoAdministrativoEscolar(Long poaOrganismoAdministrativoEscolar) {
        this.poaOrganismoAdministrativoEscolar = poaOrganismoAdministrativoEscolar;
    }

    public Long getPoaCargoOAEPk() {
        return poaCargoOAEPk;
    }

    public void setPoaCargoOAEPk(Long poaCargoOAEPk) {
        this.poaCargoOAEPk = poaCargoOAEPk;
    }

    public Boolean getIncluirDatoHabiltadoRemplazo() {
        return incluirDatoHabiltadoRemplazo;
    }

    public void setIncluirDatoHabiltadoRemplazo(Boolean incluirDatoHabiltadoRemplazo) {
        this.incluirDatoHabiltadoRemplazo = incluirDatoHabiltadoRemplazo;
    }

    
    
    

}
