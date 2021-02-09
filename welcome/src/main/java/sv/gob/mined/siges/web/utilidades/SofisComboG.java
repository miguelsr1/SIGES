/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class SofisComboG<T> {

    private static final Logger LOGGER = Logger.getLogger(SofisComboG.class.getName());

    private HashMap<Integer, T> objetos = new HashMap<Integer, T>();
    private HashMap<Integer, T> objetosComunes = new HashMap<Integer, T>();
    private HashMap<Integer, T> objetosNoComunes = new HashMap<Integer, T>();
    private List<SelectItem> items = new LinkedList<SelectItem>();
    private List<SelectItem> itemsComunes = new LinkedList<SelectItem>();
    private List<SelectItem> itemsNoComunes = new LinkedList<SelectItem>();
    private Integer selected = null;
    private Boolean visible = true;
    private Boolean disabled = false;
    private String propertyName;
    // Esta variable solo se setea cuando se usan los items comunes y no comunes
    private Boolean comunes = null;

    // <editor-fold defaultstate="collapsed" desc="  Constructores ">
    public SofisComboG() {

    }

    public SofisComboG(List<T> objetos) {
        if ((objetos != null) && (objetos.size() > 0)) {
            for (T obj : objetos) {
                add(obj);
            }
            selected = objetos.get(0).hashCode();
        }
    }

    public SofisComboG(List<T> objetos, String propertyName) {
        this.propertyName = propertyName;
        if ((objetos != null) && (objetos.size() > 0)) {
            for (T obj : objetos) {
                add(obj);
            }
            selected = objetos.get(0).hashCode();
        }
        this.objetos = this.objetosNoComunes;
        this.items = this.itemsNoComunes;
    }

    public SofisComboG(List<T> objetosComunes, List<T> objetos, String propertyName) {
        this.propertyName = propertyName;
        if ((objetos != null) && (objetos.size() > 0)) {
            for (T obj : objetos) {
                add(obj);
            }
        }
        if ((objetosComunes != null) && (objetosComunes.size() > 0)) {
            for (T obj : objetosComunes) {
                addComun(obj);
            }
            selected = objetosComunes.get(0).hashCode();
        }
        this.objetos = this.objetosComunes;
        this.items = this.itemsComunes;
        SelectItem si = new SelectItem(-2, "Otro...");
        this.items.add(si);
        comunes = true;
    }

    public SofisComboG(Set<T> objetos, String propertyName) {
        this.propertyName = propertyName;
        if ((objetos != null) && (objetos.size() > 0)) {
            for (T obj : objetos) {
                add(obj);
            }
            selected = objetos.iterator().next().hashCode();
        }
        this.objetos = this.objetosNoComunes;
        this.items = this.itemsNoComunes;
    }

    public Boolean isEmpty() {
        if (comunes != null && comunes) {
            return objetosComunes.isEmpty();
        } else {
            return objetosNoComunes.isEmpty();
        }
    }

    // </editor-fold>
    /**
     * Devulve todos los objetos que tiene el combo
     *
     * @return Lista de T
     */
    public List getAllTs() {
        LinkedList resultado = new LinkedList();
        Collection<T> c = objetos.values();
        for (T o : c) {
            if (o != null) {
                resultado.add(o);
            }
        }
        return resultado;
    }

    /**
     * Elimina el objeto del combo
     *
     * @param objeto T
     */
    public void remove(T objeto) {
        int hc = objeto.hashCode();
        SelectItem item = null;
        for (SelectItem si : items) {
            if (si.getValue().hashCode() == hc) {
                item = si;
            }
        }
        if (item != null) {
            items.remove(item);
            objetos.remove(item.getValue());
        }
    }

    public void addComun(T objeto) {
        if (objeto != null) {
            String name = null;
            if (propertyName == null) {
                name = objeto.toString();
            } else {
                Object p = getPropertyValue(objeto, propertyName);
                if (p != null) {
                    name = p.toString();
                }
            }
            SelectItem selectItem = new SelectItem(objeto.hashCode(), name);
            itemsComunes.add(selectItem);
            objetosComunes.put(objeto.hashCode(), objeto);
        }
    }

    public void add(T objeto) {
        if (objeto != null) {
            String name = null;
            if (propertyName == null) {
                name = objeto.toString();
            } else {
                Object p = getPropertyValue(objeto, propertyName);
                if (p != null) {
                    name = p.toString();
                }
            }
            SelectItem selectItem = new SelectItem(objeto.hashCode(), name);
            itemsNoComunes.add(selectItem);
            objetosNoComunes.put(objeto.hashCode(), objeto);
        }
    }

    public T getSelectedT() {
        if (selected != null) {
            return objetos.get(selected);
        } else {
            return null;
        }
    }

    public void setSelectedT(T objeto) {
        if (comunes != null && comunes == true) {
            if (!this.objetos.containsValue(objeto)) {
                this.items = itemsNoComunes;
                this.objetos = objetosNoComunes;
                comunes = false;
            }
        }
        if (objeto != null) {
            selected = objeto.hashCode();
        }
    }

    public void addEmptyItem(String name) {
        SelectItem si = new SelectItem(-1, name);
        items.add(0, si);
        objetos.put(-1, null);
        selected = -1;
    }

    public void addEmptyWithoutSelecting(String name) {
        SelectItem si = new SelectItem(-1, name);
        items.add(0, si);
        objetos.put(-1, null);
    }

    /**
     * Este evento es solo para cuando hay elementos comunes y no comunes
     *
     * @param event ValueChangeEvent
     */
    public void changeItem(ValueChangeEvent event) {
        this.selected = (Integer) event.getNewValue();
        if (this.selected == -2) {
            this.items = this.itemsNoComunes;
            this.objetos = this.objetosNoComunes;
            comunes = false;
        }
    }

    /**
     * Este método permite ordenar los valores del combo por la etiqueta del
     * mismo
     */
    public void ordenar() {
        List<SelectItem> listaOrdenada = this.items.stream().sorted(Comparator.comparing(SelectItem::getLabel)).collect(Collectors.toList());
        this.items = listaOrdenada;
    }

// <editor-fold defaultstate="collapsed" desc="  Funciones Auxiliares ">
    private Object getPropertyValue(T o, String propertyName) {
        if (o == null) {
            return null;
        }

        if (propertyName.equals("toString")) {
            try {
                Method m = o.getClass().getMethod("toString");
                return m.invoke(o);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }

        String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        Method m = null;
        try {
            m = o.getClass().getMethod(methodName);
        } catch (NoSuchMethodException nsme) {
            methodName = "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                m = o.getClass().getMethod(methodName);
            } catch (NoSuchMethodException nsme1) {
                LOGGER.log(Level.SEVERE, nsme1.getMessage());
                return null;
            }
        } catch (SecurityException se) {
            LOGGER.log(Level.SEVERE, se.getMessage());
            return null;
        }

        Object propertyValue = null;
        try {
            propertyValue = m.invoke(o);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, exc.getMessage());
            return null;
        }

        return propertyValue;
    }

    private void setPropertyValue(T o, String propertyName,
            Class propertyClass, T value) {

        String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        Method m = null;
        try {
            m = o.getClass().getDeclaredMethod(methodName, propertyClass);
        } catch (NoSuchMethodException nsme) {
            LOGGER.log(Level.SEVERE, nsme.getMessage());
            return;

        } catch (SecurityException se) {
            LOGGER.log(Level.SEVERE, se.getMessage());
            return;
        }

        try {
            m.invoke(o, value);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, exc.getMessage());
            return;
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="  Getters y setters para la jsf ">
    public List<SelectItem> getItems() {
        return items;
    }

    public void setItems(List<SelectItem> items) {
        this.items = items;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean v) {
        this.visible = v;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    // </editor-fold>
}
