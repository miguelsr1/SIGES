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
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgObtenerOperacionesSeguridad;
import sv.gob.mined.siges.enumerados.EnumAmbito;

public class SeguridadDAO implements Serializable {

    private EntityManager em;

    public SeguridadDAO(EntityManager em) {
        this.em = em;
    }

    private static final Logger LOGGER = Logger.getLogger(SeguridadDAO.class.getName());

    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario) {
        SgObtenerOperacionesSeguridad obj = new SgObtenerOperacionesSeguridad(operacion, codigoUsuario);
        return this.obtenerOperacionesSeguridad(obj);

    }

    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario, EnumAmbito ambito) {
        SgObtenerOperacionesSeguridad obj = new SgObtenerOperacionesSeguridad(operacion, codigoUsuario, ambito);
        return this.obtenerOperacionesSeguridad(obj);
    }

    public List<OperationSecurity> obtenerOperacionesSeguridad(SgObtenerOperacionesSeguridad obj) {
        String querys = "Select operacion_codigo, ambito, operacion_cascada, contexto, regla from " + Constantes.SCHEMA_SEGURIDAD + ".permisos "
                + "where usuario_codigo = :cod and operacion_codigo = :opcodigo";

        if (obj.getAmbito() != null) {
            querys += " and ambito = :ambito ";
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

        if (obj.getOperacion().equals(ConstantesOperaciones.BUSCAR_SEDES)
                || obj.getOperacion().equals(ConstantesOperaciones.BUSCAR_SERVICIO_EDUCATIVO)
                || obj.getOperacion().equals(ConstantesOperaciones.BUSCAR_SECCION)) {
            Long cantAmbitoPersona = ret.stream().filter(o -> o.getAmbit().equals("PERSONA")).count();

            if (cantAmbitoPersona > 0 && ret.size() > cantAmbitoPersona) {
                //Si hay algún ambito adicional a PERSONA, ignoramos el ámbito de tipo persona
                //Se hace esto para mejorar performance de querys por dataSecurity
                //Si por ejemplo un usuario es director y padre, deberá utilizar app móvil como padre y web app como director
                ret = ret.stream().filter(o -> !o.getAmbit().equals("PERSONA")).collect(Collectors.toList());
            }


        }

        return ret;
    }

}
