/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumTipoFormula {
    GRADO("Grado"),
    COMPONENTE_PLAN_ESTUDIO("Componente plan estudio"),
    COMPONENTE_PLAN_ESTUDIO_PARAM("Componente plan estudio con parámetro nota evaluación"),
    TITULO("Título"),
    PRUEBA_EXTRAORDINARIA("Prueba extraordinaria"),
    PRUEBA_EXTRAORDINARIA_PARAM("Prueba extraordinaria con parámetro nota evaluación");

    private final String text;

    private EnumTipoFormula(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}