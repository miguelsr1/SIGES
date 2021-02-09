
package uy.com.sofis.pfea.utilidades;

import java.util.Comparator;
import javax.faces.model.SelectItem;

/**
 * @author Sofis Solutions
 */
public class SelectItemComparator implements Comparator<SelectItem> {

    @Override
    public int compare(SelectItem item1, SelectItem item2) {
        if(item1==null || item1.getLabel()==null) {
            if(item2==null || item2.getLabel()==null) {
                return 0;
            }
            return 1;
        }
        if(item2==null || item2.getLabel()==null) {
            return -1;
        }
        return item1.getLabel().toUpperCase().compareTo(item2.getLabel().toUpperCase());
    }

}
