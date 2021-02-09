/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.security.OperationSecurity;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgObtenerOperacionesSeguridad;
import sv.gob.mined.siges.enumerados.EnumAmbito;

/**
 * DAO que gestiona la seguridad
 *
 * @author jgiron
 */
public class SeguridadDAO implements Serializable {

    private EntityManager em;

    /**
     * Constructor de la clase
     *
     * @param em
     */
    public SeguridadDAO(EntityManager em) {
        this.em = em;
    }

    private static final Logger LOGGER = Logger.getLogger(SeguridadDAO.class.getName());

    /**
     * Obtiene las operaciones de un usuario
     *
     * @param operacion
     * @param codigoUsuario
     * @return
     */
    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario) {
        SgObtenerOperacionesSeguridad obj = new SgObtenerOperacionesSeguridad(operacion, codigoUsuario, null);
        return this.obtenerOperacionesSeguridad(obj);

    }

    /**
     * Obtiene las operaciones de segurida de un usuario en un contexto
     *
     * @param operacion
     * @param codigoUsuario
     * @param ambito
     * @return
     */
    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario, EnumAmbito ambito) {
        SgObtenerOperacionesSeguridad obj = new SgObtenerOperacionesSeguridad(operacion, codigoUsuario, ambito);
        return this.obtenerOperacionesSeguridad(obj);
    }

    /**
     * Devuelve las operaciones de seguridad de un usuario y contexto
     *
     * @param obj
     * @return
     */
    public List<OperationSecurity> obtenerOperacionesSeguridad(SgObtenerOperacionesSeguridad obj) {
        String querys = "Select operacion_codigo, ambito, operacion_cascada, contexto, regla from " + Constantes.SCHEMA_SEGURIDAD + ".permisos "
                + "where usuario_codigo = :cod and operacion_codigo = :opcodigo";

        if (obj.getAmbito() != null) {
            querys += " and ambito = :ambito";
        }

        Query q = em.createNativeQuery(querys);
        q.setParameter("cod", obj.getCodigoUsuario());
        q.setParameter("opcodigo", obj.getOperacion());

        if (obj.getAmbito() != null) {
            q.setParameter("ambito", obj.getAmbito().name());
        }

        List<OperationSecurity> ret = new ArrayList<>();

        List<Object[]> objs = (List<Object[]>) q.getResultList();
        for (Object[] ob : objs) {
            OperationSecurity op = new OperationSecurity(
                    (String) ob[0],
                    (String) ob[1],
                    (boolean) ob[2],
                    ob[3] != null ? ((BigInteger) ob[3]).longValue() : null,
                    (String) ob[4]
            );
            ret.add(op);
        }
        return ret;
    }

}
