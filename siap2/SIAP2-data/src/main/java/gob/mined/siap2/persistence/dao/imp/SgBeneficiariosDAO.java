package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;
import javax.persistence.NoResultException;

@JPADAO
public class SgBeneficiariosDAO extends EclipselinkJpaDAOImp<Beneficiarios, Integer> implements  Serializable{
 
    
    
    /**
     * Este método devuelve un registro de Beneficiarios filtrado por código
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public Beneficiarios getBeneficiarioById(String codigo) throws DAOGeneralException {

        return (Beneficiarios) entityManager
                .createQuery("select g from Beneficiarios g where g.id = :id")
                .setParameter("id", codigo)
                .getSingleResult();
    }
        
    
    /**
     * Este método devuelve todos los registros de SubModalidades que tengan los mismos ID's de relacion
     * @param beneficiarios
     * @return
     * @throws DAOGeneralException 
     */
    public Beneficiarios getBeneficiariosByModalidadAtencion(Beneficiarios beneficiarios) throws DAOGeneralException {
        try {
            
            Beneficiarios ben = new Beneficiarios();
            ben = (Beneficiarios) entityManager.createQuery("select b from Beneficiarios WHERE b.organizacionesCurricular.id = :idOc");
            
            return ben;
        } catch (NoResultException e) {
            return null;
        }catch(Exception e){
            throw e;
        }
        
    }
    
    
    /**
     * Este método devuelve todos los registros de Beneficiarios
     * @return
     * @throws DAOGeneralException 
     */
    public List<Beneficiarios> getBeneficiarios() throws DAOGeneralException{
        return (List<Beneficiarios>) entityManager.createQuery("select g from Beneficiarios g").getResultList();
    }
    
    /**
     * Este método devuelve todos los registros de Beneficiarios que pertenezcan al mismo componente
     * 
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<Beneficiarios> getBeneficiariosPorPresEsAnioFiscal(Integer id) throws DAOGeneralException{
        return (List<Beneficiarios>) entityManager.createQuery("select g from Beneficiarios g where g.sgPresEsAnioFiscal.id = :id order by g.sgNiveles.orden asc, g.sgModalidades.orden asc")
                .setParameter("id", id).getResultList();
    }
}
