package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.imp.TipoCuentaBancariaDAO;
import gob.mined.siges.entities.data.impl.SgTipoCuentaBancaria;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless(name = "TipoCuentaBancariaBean")
public class TipoCuentaBancariaBean {
    
    private static final Logger logger = Logger.getLogger(TipoCuentaBancariaBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private TipoCuentaBancariaDAO tipoCuentaBancariaDAO;
    
    
    /**
     * Este método devuelve todos los registros habilitados de TipoCuentaBancaria
     * @return 
     */
    public List<SgTipoCuentaBancaria> getTipoCuentaBancariaHabilitado() {
        try {
            return tipoCuentaBancariaDAO.getTipoCuentaBancariaHabilitado();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
}
