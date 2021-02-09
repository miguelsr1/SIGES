/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.persistencia.entidades.SgConfiguracion;

@Stateless
public class ConfiguracionBean {

    @PersistenceContext
    private EntityManager em;


    
    /**
     * Devuelve los codigos de las operaciones del usuario con el codigo
     * indicado
     *
     * @param codigo String
     * @return Lista de String
     * @throws GeneralException
     */
    public Integer obtenerMaxResultPorAudience(String audience) throws GeneralException {
        if (audience != null) {
            try {
                Integer maxResult=50;
                Query q = em.createNativeQuery("select * from catalogo.sg_configuraciones where con_codigo=:codigo",SgConfiguracion.class);
                q.setParameter("codigo", Constantes.AUDIENCE_MAX_RESULTS);
                
                List<SgConfiguracion> list=q.getResultList();
                
                if(list!=null && !list.isEmpty()){
                    SgConfiguracion conf =list.get(0);
                    if(!StringUtils.isBlank(conf.getConValor()) && conf.getConValor().contains(audience)){
                        String codigo=conf.getConValor();
                        codigo= codigo.replaceAll("\\s+", "");
                        String[] valores=codigo.split(",");
                        for(String val:valores){
                            String[] valCodigo=val.split(":");
                            if(valCodigo.length>1 && audience.equals(valCodigo[0])){
                                if(!StringUtils.isBlank(valCodigo[1])){
                                    return Integer.parseInt(valCodigo[1]);
                                }
                            }
                        }
                    }
                }
                
                return maxResult;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return 50;
    }

    
}
