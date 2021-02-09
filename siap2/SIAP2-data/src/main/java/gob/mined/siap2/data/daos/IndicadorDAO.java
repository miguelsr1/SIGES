/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.daos;

import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Este clase corresponde al DAO de Indicadores.
 *
 * @author Sofis Solutions
 */
public class IndicadorDAO extends EclipselinkJpaDAOImp<Indicador, Integer> implements Serializable {

    /**
     * Este método permite ubicar un indicador que tiene una determinada clave
     * primaria.
     *
     * @param primaryKey
     * @return
     */
    public Indicador find(Integer primaryKey) {
        return entityManager.find(Indicador.class, primaryKey);
    }
}
