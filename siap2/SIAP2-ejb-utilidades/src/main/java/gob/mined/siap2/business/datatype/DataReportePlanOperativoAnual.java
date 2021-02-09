/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos del reporte de reprogramación.
 *
 * @author Eduardo
 */
public class DataReportePlanOperativoAnual extends DataReporteTemplate{
    private String direccion;
    private String anio;
    private String objetivo;
    private String trimestre;
    private String meta;
    private String t1;
    private String t2;
    private String t3;
    private String t4;
    private String total;
    private String t1Real;
    private String t2Real;
    private String t3Real;
    private String t4Real;
    private String totalReal;
    private String medioVerificacion;
    private String tituloReporte;
    private String avancesLimitantes;
    private String responsable;
    private String cargoResponsable;
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getT1Real() {
        return t1Real;
    }

    public void setT1Real(String t1Real) {
        this.t1Real = t1Real;
    }

    public String getT2Real() {
        return t2Real;
    }

    public void setT2Real(String t2Real) {
        this.t2Real = t2Real;
    }

    public String getT3Real() {
        return t3Real;
    }

    public void setT3Real(String t3Real) {
        this.t3Real = t3Real;
    }

    public String getT4Real() {
        return t4Real;
    }

    public void setT4Real(String t4Real) {
        this.t4Real = t4Real;
    }

    public String getTotalReal() {
        return totalReal;
    }

    public void setTotalReal(String totalReal) {
        this.totalReal = totalReal;
    }

    public String getMedioVerificacion() {
        return medioVerificacion;
    }

    public void setMedioVerificacion(String medioVerificacion) {
        this.medioVerificacion = medioVerificacion;
    }

    public String getTituloReporte() {
        return tituloReporte;
    }

    public void setTituloReporte(String tituloReporte) {
        this.tituloReporte = tituloReporte;
    }


    public String getAvancesLimitantes() {
        return avancesLimitantes;
    }

    public void setAvancesLimitantes(String avancesLimitantes) {
        this.avancesLimitantes = avancesLimitantes;
    }

    public String getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(String trimestre) {
        this.trimestre = trimestre;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCargoResponsable() {
        return cargoResponsable;
    }

    public void setCargoResponsable(String cargoResponsable) {
        this.cargoResponsable = cargoResponsable;
    }

}
