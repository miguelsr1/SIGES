/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.sofisform.to;

import java.util.List;

/**
 *
 * @author Sofis Solutions (www.sofis-solutions.com)
 */
public abstract class ContainerCriteriaTO extends CriteriaTO {

    public abstract List<CriteriaTO> getCriterias();
}