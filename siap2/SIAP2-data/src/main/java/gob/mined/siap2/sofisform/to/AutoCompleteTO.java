/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.sofisform.to;



/**
 *
 * @author Sofis
 */
public class AutoCompleteTO extends BindingComponentTO{

    private String maxRows;
    String itemPropertyId;
    String itemPropertyLabel;
    //si esta presneta esta propiedad la busqueda la realiza por este campo, ademas de los criterias
    String itemPropertySearchLabel;
    CriteriaTO criteria;
    String searchStyle;

    public String getItemPropertySearchLabel() {
        return itemPropertySearchLabel;
    }

    public void setItemPropertySearchLabel(String itemPropertySearchLabel) {
        this.itemPropertySearchLabel = itemPropertySearchLabel;
    }
    
    public String getSearchStyle() {
        return searchStyle;
    }

    public void setSearchStyle(String searchStyle) {
        this.searchStyle = searchStyle;
    }


    public CriteriaTO getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaTO criteria) {
        this.criteria = criteria;
    }
    

    public String getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(String maxRows) {
        this.maxRows = maxRows;
    }

    public String getItemPropertyId() {
        return itemPropertyId;
    }

    public void setItemPropertyId(String itemPropertyId) {
        this.itemPropertyId = itemPropertyId;
    }

    public String getItemPropertyLabel() {
        return itemPropertyLabel;
    }

    public void setItemPropertyLabel(String itemPropertyLabel) {
        this.itemPropertyLabel = itemPropertyLabel;
    }

    

}
