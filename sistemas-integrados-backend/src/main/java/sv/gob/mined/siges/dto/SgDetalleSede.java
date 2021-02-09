
package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class SgDetalleSede implements Serializable {
    
    private Integer nivOrden;
    
    private String nivCodigo;
    
    private Long nivPk;
    
    private String nivNombre;
    
    private Long sduPk;
    
    private String modalidad;
    
    private String codModalidad;
    
    private Long totalSecciones;
    
    private Long totalMasculino;
    
    private Long totalFemenino;
    
    private Long totalModalidaes;
    
    private Long totalDocentesFemenino;
    
    private Long totalDocentesMasculino;
    
    public SgDetalleSede(){
        
    }

    public Integer getNivOrden() {
        return nivOrden;
    }

    public void setNivOrden(Integer nivOrden) {
        this.nivOrden = nivOrden;
    }

    public String getNivCodigo() {
        return nivCodigo;
    }

    public void setNivCodigo(String nivCodigo) {
        this.nivCodigo = nivCodigo;
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

    public String getCodModalidad() {
        return codModalidad;
    }

    public void setCodModalidad(String codModalidad) {
        this.codModalidad = codModalidad;
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

    public Long getTotalModalidaes() {
        return totalModalidaes;
    }

    public void setTotalModalidaes(Long totalModalidaes) {
        this.totalModalidaes = totalModalidaes;
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
