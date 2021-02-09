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
public enum TipoParametro {
    
    MATRICULA_POR_NIVEL("Matricula por nivel",1),
    CANTIDAD_DOCENTES("Cantidad docentes",2),
    CENTRO_EDUCATIVO("Centro Educativo",3),
    RANGO_MATRICULA("Por rango de matricula", 4);
    
    private final String label;
    private final Integer valor;


    private TipoParametro(String label, Integer valor) {
        this.label = label;
        this.valor = valor;
    }
    

    public Integer getValor() {
        return valor;
    }
    
    public String getLabel() {
        return label;
    }

}
