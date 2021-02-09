/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumCategoriaAlertaDesempenio {
    EDUC_INICIAL_CICLO_UNICO("Educación Inicial - Ciclo único"),
    EDUC_PARVULARIA_CICLO_UNICO("Educación Parvularia - Ciclo único"),
    EDUC_BASICA_CICLO_I("Educación Básica - Ciclo I"),
    EDUC_BASICA_CICLO_II("Educación Básica - Ciclo II"),
    EDUC_BASICA_CICLO_III("Educación Básica - Ciclo III"),
    EDUC_MEDIA_CICLO_UNICO("Educación Media - Ciclo único");
    
    private final String text;

    private EnumCategoriaAlertaDesempenio(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
}
