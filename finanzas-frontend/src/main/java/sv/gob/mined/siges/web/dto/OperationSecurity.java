/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

/**
 *
 * @author Sofis Solutions
 */
public class OperationSecurity {

    private String operation;
    private String ambit;
    private boolean cascade;
    private Object context;

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAmbit() {
        return ambit;
    }

    public void setAmbit(String ambit) {
        this.ambit = ambit;
    }

    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    // </editor-fold>
}
