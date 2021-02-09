/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.reference;

import java.io.Serializable;
import java.util.Map;

/**
 * Clase desarrollada por Sofis Solutions
 * @author Sofis Solutions
 */
public class EntityReference<ID> implements Serializable {

    private ID id;
    private Map<String, Object> propertyMap;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }


}
