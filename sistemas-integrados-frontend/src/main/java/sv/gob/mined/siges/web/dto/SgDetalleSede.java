package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class SgDetalleSede implements Serializable {

    private Integer nivOrden;

    private Long nivPk;

    private String nivNombre;

    private Long sduPk;

    private String modalidad;

    private Long totalSecciones;

    private Long totalMasculino;

    private Long totalFemenino;
    private Long totalDocentesFemenino;

    private Long totalDocentesMasculino;

    public SgDetalleSede() {

    }

    @JsonIgnore
    public Long getTotalDocentes() {
        return (this.totalDocentesFemenino != null ? this.totalDocentesFemenino : 0) + (this.totalDocentesMasculino != null ? this.totalDocentesMasculino : 0);
    }

    @JsonIgnore
    public Long getTotalEstudiantes() {
        return (this.totalFemenino != null ? this.totalFemenino : 0) + (this.totalMasculino != null ? this.totalMasculino : 0);
    }

    public Integer getNivOrden() {
        return nivOrden;
    }

    public void setNivOrden(Integer nivOrden) {
        this.nivOrden = nivOrden;
    }

    public Long getNivPk() {
        return nivPk;
    }

    public void setNivPk(Long nivPk) {
        this.nivPk = nivPk;
    }

    public String getNivNombre() {
        return nivNombre;
    }

    public void setNivNombre(String nivNombre) {
        this.nivNombre = nivNombre;
    }

    public Long getSduPk() {
        return sduPk;
    }

    public void setSduPk(Long sduPk) {
        this.sduPk = sduPk;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public Long getTotalSecciones() {
        return totalSecciones;
    }

    public void setTotalSecciones(Long totalSecciones) {
        this.totalSecciones = totalSecciones;
    }

    public Long getTotalMasculino() {
        return totalMasculino;
    }

    public void setTotalMasculino(Long totalMasculino) {
        this.totalMasculino = totalMasculino;
    }

    public Long getTotalFemenino() {
        return totalFemenino;
    }

    public void setTotalFemenino(Long totalFemenino) {
        this.totalFemenino = totalFemenino;
    }

    public Long getTotalDocentesFemenino() {
        return totalDocentesFemenino;
    }

    public void setTotalDocentesFemenino(Long totalDocentesFemenino) {
        this.totalDocentesFemenino = totalDocentesFemenino;
    }

    public Long getTotalDocentesMasculino() {
        return totalDocentesMasculino;
    }

    public void setTotalDocentesMasculino(Long totalDocentesMasculino) {
        this.totalDocentesMasculino = totalDocentesMasculino;
    }

}
