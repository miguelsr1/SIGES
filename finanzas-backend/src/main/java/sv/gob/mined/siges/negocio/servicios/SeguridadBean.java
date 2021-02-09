/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.security.OperationSecurity;
import java.util.List;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.daos.SeguridadDAO;

@Stateless
/**
 * Servicio que gestiona temas vinculados al a seguridad.
 *
 */
public class SeguridadBean {

    @PersistenceContext
    private EntityManager em;

    @CacheResult(cacheName = Constantes.OPERACIONES_SEGURIDAD_CACHE)
    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario) {
        SeguridadDAO segDAO = new SeguridadDAO(em);
        return segDAO.obtenerOperacionesSeguridad(operacion, codigoUsuario);
    }

    @CacheResult(cacheName = Constantes.OPERACIONES_SEGURIDAD_AMBITO_CACHE)
    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario, EnumAmbito ambito) {
        SeguridadDAO segDAO = new SeguridadDAO(em);
        return segDAO.obtenerOperacionesSeguridad(operacion, codigoUsuario, ambito);
    }

}
