/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import org.primefaces.component.datatable.DataTable;

public class DataTableSelectionFix extends DataTable {

	public java.lang.Object getSelection() {
	    Object o = getLocalSelection();
	    if (o == null)
	        return (java.lang.Object) getStateHelper().eval(PropertyKeys.selection, null);
	    else
	        return o;
	}

	public void setSelection(java.lang.Object _selection) {
	    getStateHelper().put(PropertyKeys.selection + this.getClientId(), _selection);
	}

	public Object getLocalSelection() {
	    return getStateHelper().get(PropertyKeys.selection + this.getClientId());
	}
}