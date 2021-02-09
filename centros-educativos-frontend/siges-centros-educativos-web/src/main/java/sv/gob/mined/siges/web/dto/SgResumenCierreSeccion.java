package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;


/**
 *
 * @author fabricio
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rcsPk", scope = SgResumenCierreSeccion.class)
public class SgResumenCierreSeccion implements Serializable {
    
    private Long rcsPk;

    private SgCierreAnioLectivoSede rcsCierreAnioLectivoSede;

    private SgSeccion rcsSeccion;

    private Integer rcsPromocionesPendientesMasc;

    private Integer rcsPromocionesPendientesFem;

    private Integer rcsPromovidosMasc;

    private Integer rcsPromovidosFem;

    private Integer rcsNoPromovidosMasc;

    private Integer rcsNoPromovidosFem;

    private Integer rcsAsistencias;

    private Integer rcsInasistencias;

    private Integer rcsInasistenciasJust;

    private Integer rcsSolicitudesTitulosMasc;

    private Integer rcsSolicitudesTitulosFem;

    public SgResumenCierreSeccion() {
    }
    
    public Long getRcsPk() {
        return rcsPk;
    }

    public void setRcsPk(Long rcsPk) {
        this.rcsPk = rcsPk;
    }

    public SgCierreAnioLectivoSede getRcsCierreAnioLectivoSede() {
        return rcsCierreAnioLectivoSede;
    }

    public void setRcsCierreAnioLectivoSede(SgCierreAnioLectivoSede rcsCierreAnioLectivoSede) {
        this.rcsCierreAnioLectivoSede = rcsCierreAnioLectivoSede;
    }

    public SgSeccion getRcsSeccion() {
        return rcsSeccion;
    }

    public void setRcsSeccion(SgSeccion rcsSeccion) {
        this.rcsSeccion = rcsSeccion;
    }

    public Integer getRcsPromocionesPendientesMasc() {
        return rcsPromocionesPendientesMasc;
    }

    public void setRcsPromocionesPendientesMasc(Integer rcsPromocionesPendientesMasc) {
        this.rcsPromocionesPendientesMasc = rcsPromocionesPendientesMasc;
    }

    public Integer getRcsPromocionesPendientesFem() {
        return rcsPromocionesPendientesFem;
    }

    public void setRcsPromocionesPendientesFem(Integer rcsPromocionesPendientesFem) {
        this.rcsPromocionesPendientesFem = rcsPromocionesPendientesFem;
    }

    public Integer getRcsPromovidosMasc() {
        return rcsPromovidosMasc;
    }

    public void setRcsPromovidosMasc(Integer rcsPromovidosMasc) {
        this.rcsPromovidosMasc = rcsPromovidosMasc;
    }

    public Integer getRcsPromovidosFem() {
        return rcsPromovidosFem;
    }

    public void setRcsPromovidosFem(Integer rcsPromovidosFem) {
        this.rcsPromovidosFem = rcsPromovidosFem;
    }

    public Integer getRcsNoPromovidosMasc() {
        return rcsNoPromovidosMasc;
    }

    public void setRcsNoPromovidosMasc(Integer rcsNoPromovidosMasc) {
        this.rcsNoPromovidosMasc = rcsNoPromovidosMasc;
    }

    public Integer getRcsNoPromovidosFem() {
        return rcsNoPromovidosFem;
    }

    public void setRcsNoPromovidosFem(Integer rcsNoPromovidosFem) {
        this.rcsNoPromovidosFem = rcsNoPromovidosFem;
    }

    public Integer getRcsAsistencias() {
        return rcsAsistencias;
    }

    public void setRcsAsistencias(Integer rcsAsistencias) {
        this.rcsAsistencias = rcsAsistencias;
    }

    public Integer getRcsInasistencias() {
        return rcsInasistencias;
    }

    public void setRcsInasistencias(Integer rcsInasistencias) {
        this.rcsInasistencias = rcsInasistencias;
    }

    public Integer getRcsInasistenciasJust() {
        return rcsInasistenciasJust;
    }

    public void setRcsInasistenciasJust(Integer rcsInasistenciasJust) {
        this.rcsInasistenciasJust = rcsInasistenciasJust;
    }

    public Integer getRcsSolicitudesTitulosMasc() {
        return rcsSolicitudesTitulosMasc;
    }

    public void setRcsSolicitudesTitulosMasc(Integer rcsSolicitudesTitulosMasc) {
        this.rcsSolicitudesTitulosMasc = rcsSolicitudesTitulosMasc;
    }

    public Integer getRcsSolicitudesTitulosFem() {
        return rcsSolicitudesTitulosFem;
    }

    public void setRcsSolicitudesTitulosFem(Integer rcsSolicitudesTitulosFem) {
        this.rcsSolicitudesTitulosFem = rcsSolicitudesTitulosFem;
    }

  
    
    
    
}
