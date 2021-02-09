/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils;

import gob.mined.siap2.sofisform.to.AND_TO;
import gob.mined.siap2.sofisform.to.BindingComponentTO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.sofisform.to.OR_TO;
import java.util.Date;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Clase con utilidades para la creación del criteriaTO
 * @author Sofis Solutions (www.sofis-solutions.com)
 */
public class CriteriaTOUtils {

    //auxiliar para que los nombres no se repitan en el bindingComponent
    private static long sequence = 0;

    /**
     * Crea un match criteria TO
     * @param type el tipo de criteria
     * @param property la propiedad a la cual se le va a aplicar
     * @param value el valor seteado para la propiedad
     * @return
     */
    public static MatchCriteriaTO createMatchCriteriaTO(MatchCriteriaTO.types type, String property, Object value) {
        MatchCriteriaTO matchCriteriaTO = new MatchCriteriaTO();
        matchCriteriaTO.setMatchType(type.getType());
        matchCriteriaTO.setValue(value);
        BindingComponentTO bindingComponentTO = new BindingComponentTO();
        bindingComponentTO.setProperty(property);
        Date d = new Date();
        bindingComponentTO.setKey("prop_" + property.replaceAll("\\.", "_") + "_" + sequence);
        matchCriteriaTO.setComponent(bindingComponentTO);

        if (sequence == Long.MAX_VALUE) {
            sequence = 0;
        }
        sequence++;

        return matchCriteriaTO;
    }

    /**
     * Crea un AND_TO a partir de la colección de criterias criteria1 and criteria2 and ..... and criteriaN
     * @param criterias
     * @return
     */
    public static AND_TO createANDTO(CriteriaTO... criterias) {

        if (criterias.length == 0 || criterias.length == 1) {
            return null;
        }
        AND_TO toReturn = new AND_TO(criterias[0], criterias[1]);
        int index = 0;
        for (CriteriaTO criteria : criterias) {
            if (index == 0 || index == 1) {
                index++;
                continue;
            }
            createANDTORecursive(toReturn, criteria);
            index++;
        }
        return toReturn;
    }

    private static void createANDTORecursive(AND_TO and_to, CriteriaTO criteria) {
        CriteriaTO currentCriteria1 = and_to.getCriteria1();
        if (currentCriteria1 instanceof AND_TO) {
            AND_TO currentCriteriaAnd1 = (AND_TO) currentCriteria1;
            createANDTORecursive(currentCriteriaAnd1, criteria);
        } else {
            AND_TO newAndTO = new AND_TO(criteria, currentCriteria1);
            and_to.setCriteria1(newAndTO);
            return;
        }
    }
    
     /**
     * Crea un AND_TO a partir de la colección de criterias criteria1 and criteria2 and ..... and criteriaN
     * @param criterias
     * @return
     */
    public static OR_TO createORTO(CriteriaTO... criterias) {

        if (criterias.length == 0 || criterias.length == 1) {
            return null;
        }
        OR_TO toReturn = new OR_TO(criterias[0], criterias[1]);
        int index = 0;
        for (CriteriaTO criteria : criterias) {
            if (index == 0 || index == 1) {
                index++;
                continue;
            }
            createORTORecursive(toReturn, criteria);
            index++;
        }
        return toReturn;
    }

    private static void createORTORecursive(OR_TO and_to, CriteriaTO criteria) {
        CriteriaTO currentCriteria1 = and_to.getCriteria1();
        if (currentCriteria1 instanceof OR_TO) {
            OR_TO currentCriteriaAnd1 = (OR_TO) currentCriteria1;
            createORTORecursive(currentCriteriaAnd1, criteria);
        } else {
            OR_TO newAndTO = new OR_TO(criteria, currentCriteria1);
            and_to.setCriteria1(newAndTO);
            return;
        }
    }

}
