/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.utilidades;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author usuario
 */
@Named
@ApplicationScoped
public class PassthroughColumnExporter {

   public String export(final UIColumn column) {
      String value = StringUtils.EMPTY;
      for (final UIComponent child : column.getChildren()) {
         final Object exportValue = child.getAttributes().get("data-export");
         if (exportValue != null) {
            value = "'"+exportValue;
            break;
         }
      }

      return value;
   }
}