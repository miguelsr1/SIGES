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
public enum TipoMedida {

    DOCENTE("Docente",1),
    ESTUDIANTE("Estudiante",2),
    CENTRO("Centro",3);
    
    private String label;
    private Integer valor;

    private TipoMedida(String label, Integer valor) {
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
