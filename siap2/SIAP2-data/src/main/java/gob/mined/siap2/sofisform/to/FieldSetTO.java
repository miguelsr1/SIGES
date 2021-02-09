/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.sofisform.to;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 * <sofis:fieldset>
 *               <f:facet name="svGroupLabel">
 *                   <ice:selectBooleanCheckbox  />
 *               </f:facet>
 *  </sofis:fieldset>
 *
 * Clase desarrollada por Sofis Solutions
 * @author Sofis Solutions
 */
public class FieldSetTO extends ContainerComponentTO{

    List<ComponentTO> legendComponents = new ArrayList();

    public List<ComponentTO> getLegendComponents() {
        return legendComponents;
    }

    public void setLegendComponents(List<ComponentTO> legendComponents) {
        this.legendComponents = legendComponents;
    }

    @Override
    public Element toXML() {
        Element toReturn = new Element("fieldset");
        toReturn = super.toXMLMetadata(toReturn);
        Element legendE = new Element("legend");
        for (ComponentTO compo : legendComponents){
            legendE.getChildren().add(compo.toXML());
        }
        toReturn.getChildren().add(legendE);
        return toReturn;
    }


    
}
