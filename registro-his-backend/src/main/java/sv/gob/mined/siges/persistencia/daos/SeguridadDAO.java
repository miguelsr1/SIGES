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
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sv.gob.mined.siges.constantes.Constantes;

public class SeguridadDAO implements Serializable {

    private EntityManager em;

    public SeguridadDAO(EntityManager em) {
        this.em = em;
    }

    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigousuario) {

        Query q = em.createNativeQuery("Select operacion_codigo, ambito, operacion_cascada, contexto, regla from " + Constantes.SCHEMA_SEGURIDAD + ".permisos "
                + "where usuario_codigo = :cod and operacion_codigo = :opcodigo");
        q.setParameter("cod", codigousuario);
        q.setParameter("opcodigo", operacion);

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