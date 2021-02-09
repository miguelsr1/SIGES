/*
 *  Clase desarrollada por Sofis Solutions
 *  
 */

package com.sofis.sofisform.to;

/**
 * Clase desarrollada por Sofis Solutions
 * @author Sofis Solutions
 */
public class SelectItemActionTO {
  public enum SelectItemActionType{
        DISPLAY("display"),
        UPDATE("update");

        String type;

        private SelectItemActionType(String type) {
            this.type = type;
        }
    }

  SelectItemActionType type;

    public SelectItemActionType getType() {
        return type;
    }

    public void setType(SelectItemActionType type) {
        this.type = type;
    }

  
}
