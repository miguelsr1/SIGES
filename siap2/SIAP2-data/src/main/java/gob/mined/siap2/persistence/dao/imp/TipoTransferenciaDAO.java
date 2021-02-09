package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Rubro;
import gob.mined.siap2.entities.data.impl.SubRubro;
import gob.mined.siap2.entities.data.impl.TipoTransferencia;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class TipoTransferenciaDAO extends EclipselinkJpaDAOImp<TipoTransferencia, Integer> implements  Serializable{
    
    
    
    /**
     * Este método devuelve un registro de Rubro filtrado por código
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public TipoTransferencia getRubroByCodigo(String codigo) throws DAOGeneralException {
        return (TipoTransferencia) entityManager
                .createQuery("select g from TipoTransferencia g where g.codigo = :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }

    /**
     * Este método devuelve todos los registros de TipoTransferencia
     * @return
     * @throws DAOGeneralException 
     */
    public List<TipoTransferencia> getTiposTransferencia() throws DAOGeneralException{
        return (List<TipoTransferencia>) entityManager.createQuery("select g from TipoTransferencia g").getResultList();
    }

    /**
     * Este método devuelve una lista de registros de TipoTransferencia, que se encuentren habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<TipoTransferencia> getTransferenciasHabilitados() throws DAOGeneralException {
        return (List<TipoTransferencia>) entityManager
                .createQuery("select g from TipoTransferencia g where g.habilitado = TRUE").getResultList();
    }

}
