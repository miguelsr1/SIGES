/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.enums;

/**
 *
 * @author Sofis Solutions
 */
public enum TipoSubcomponente {
    CALCULO_POR_SISTEMA("Calculado por el sistema",1),
    CARGA_DESDE_ARCHIVO("Carga desde archivo",2);
    
   
    private String label;
    private Integer valor;

    
    

    private TipoSubcomponente(String label, Integer valor) {
        this.label = label;
        this.valor = valor;
    }
    

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
    
    public String getLabel() {
        return label;
    }

}
