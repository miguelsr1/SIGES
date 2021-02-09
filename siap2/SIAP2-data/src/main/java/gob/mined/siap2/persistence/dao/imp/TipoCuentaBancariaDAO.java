package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siges.entities.data.impl.SgTipoCuentaBancaria;
import java.io.Serializable;
import java.util.List;


@JPADAO
public class TipoCuentaBancariaDAO extends EclipselinkJpaDAOImp<SgTipoCuentaBancaria, Integer> implements  Serializable{
    
     /**
     * Este método devuelve todos los registros habilitados de TipoCuentaBancaria
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgTipoCuentaBancaria> getTipoCuentaBancariaHabilitado() throws DAOGeneralException {

        return (List<SgTipoCuentaBancaria>) entityManager
                .createQuery("select g from SgTipoCuentaBancaria g where g.habilitado = TRUE")
                .getResultList();
    }
}
