/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.validacion;

import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoSede;

public class SedesViewValidator {

    private TipoSede tipoSede;

    private Boolean renderSuvencionado = Boolean.FALSE;
    private Boolean renderLegalizado = Boolean.FALSE;
    private Boolean renderSedeAdscrita = Boolean.FALSE;
    private Boolean renderFinesDeLucro = Boolean.FALSE;
    private Boolean renderTipoOrganizacionAdministrativa = Boolean.FALSE;
    private Boolean renderCodigoSede = Boolean.TRUE;

    public SedesViewValidator(SgSede sgSede) {
        this.tipoSede = sgSede.getSedTipo();
        if (tipoSede != null) {
            switch (tipoSede) {
                case CED_OFI:
                    renderLegalizado = Boolean.TRUE;
                    renderFinesDeLucro = Boolean.TRUE;
                    renderTipoOrganizacionAdministrativa = Boolean.TRUE;
                    break;
                case CED_PRI:
                    renderLegalizado = Boolean.TRUE;
                    renderSuvencionado = Boolean.TRUE;
                    renderFinesDeLucro = Boolean.TRUE;
                    renderTipoOrganizacionAdministrativa = Boolean.TRUE;
                    break;
                case CIR_ALF:
                    if(BooleanUtils.isNotTrue(sgSede.getSedEsAdscriptaAOtraSede())){
                        renderSedeAdscrita = Boolean.TRUE;
                    }
                    
                    if (sgSede.getSedPk() == null){
                        renderCodigoSede = Boolean.FALSE;
                    }
                    break;
                case CIR_INF:
                    if(BooleanUtils.isNotTrue(sgSede.getSedEsAdscriptaAOtraSede())){
                        renderSedeAdscrita = Boolean.TRUE;
                    }
                    break;
                case UBI_EDUC:
                    if(BooleanUtils.isNotTrue(sgSede.getSedEsAdscriptaAOtraSede())){
                        renderSedeAdscrita = Boolean.TRUE;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public Boolean getRenderSuvencionado() {
        return renderSuvencionado;
    }

    public void setRenderSuvencionado(Boolean renderSuvencionado) {
        this.renderSuvencionado = renderSuvencionado;
    }

    public Boolean getRenderLegalizado() {
        return renderLegalizado;
    }

    public void setRenderLegalizado(Boolean renderLegalizado) {
        this.renderLegalizado = renderLegalizado;
    }

    public TipoSede getTipoSede() {
        return tipoSede;
    }

    public void setTipoSede(TipoSede tipoSede) {
        this.tipoSede = tipoSede;
    }

    public Boolean getRenderSedeAdscrita() {
        return renderSedeAdscrita;
    }

    public void setRenderSedeAdscrita(Boolean renderSedeAdscrita) {
        this.renderSedeAdscrita = renderSedeAdscrita;
    }

    public Boolean getRenderFinesDeLucro() {
        return renderFinesDeLucro;
    }

    public void setRenderFinesDeLucro(Boolean renderFinesDeLucro) {
        this.renderFinesDeLucro = renderFinesDeLucro;
    }

    public Boolean getRenderTipoOrganizacionAdministrativa() {
        return renderTipoOrganizacionAdministrativa;
    }

    public void setRenderTipoOrganizacionAdministrativa(Boolean renderTipoOrganizacionAdministrativa) {
        this.renderTipoOrganizacionAdministrativa = renderTipoOrganizacionAdministrativa;
    }

    public Boolean getRenderCodigoSede() {
        return renderCodigoSede;
    }

    public void setRenderCodigoSede(Boolean renderCodigoSede) {
        this.renderCodigoSede = renderCodigoSede;
    }

    
}
