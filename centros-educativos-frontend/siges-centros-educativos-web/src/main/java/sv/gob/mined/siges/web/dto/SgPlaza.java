/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPlaza;
import sv.gob.mined.siges.web.enumerados.EnumSituacionPlaza;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "plaPk", scope = SgPlaza.class)
public class SgPlaza implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Long plaPk;
    
    private String plaNombre;
    
    private Long plaIdPuesto;
    
    private Integer plaPartida;
    
    private Integer plaSubpartida;
    
    private EnumEstadoPlaza plaEstado;
    
    private EnumSituacionPlaza plaSituacion;

    private Integer plaAnioPartida;
    
    private LocalDateTime plaUltModFecha;
    
    private String plaUltModUsuario;
    
    private Integer plaVersion;
    
    private SgSede plaSedeFk;

    public SgPlaza() {
        
    }
    
    public String getPlaCodigoPlaza(){
        return String.valueOf(this.plaPartida)+"-"+String.valueOf(this.plaSubpartida);
    }
    
    public String getPlaCodigoNombre(){
        return this.getPlaCodigoPlaza()+" "+this.plaNombre;
    }

    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public String getPlaNombre() {
        return plaNombre;
    }

    public void setPlaNombre(String plaNombre) {
        this.plaNombre = plaNombre;
    }

    public Long getPlaIdPuesto() {
        return plaIdPuesto;
    }

    public void setPlaIdPuesto(Long plaIdPuesto) {
        this.plaIdPuesto = plaIdPuesto;
    }

    public Integer getPlaPartida() {
        return plaPartida;
    }

    public void setPlaPartida(Integer plaPartida) {
        this.plaPartida = plaPartida;
    }

    public Integer getPlaSubpartida() {
        return plaSubpartida;
    }

    public void setPlaSubpartida(Integer plaSubpartida) {
        this.plaSubpartida = plaSubpartida;
    }

    public EnumEstadoPlaza getPlaEstado() {
        return plaEstado;
    }

    public void setPlaEstado(EnumEstadoPlaza plaEstado) {
        this.plaEstado = plaEstado;
    }

    public EnumSituacionPlaza getPlaSituacion() {
        return plaSituacion;
    }

    public void setPlaSituacion(EnumSituacionPlaza plaSituacion) {
        this.plaSituacion = plaSituacion;
    }

    public Integer getPlaAnioPartida() {
        return plaAnioPartida;
    }

    public void setPlaAnioPartida(Integer plaAnioPartida) {
        this.plaAnioPartida = plaAnioPartida;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
    }

    public SgSede getPlaSedeFk() {
        return plaSedeFk;
    }

    public void setPlaSedeFk(SgSede plaSedeFk) {
        this.plaSedeFk = plaSedeFk;
    }

    
    //</editor-fold>
    
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plaPk != null ? plaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPlaza)) {
            return false;
        }
        SgPlaza other = (SgPlaza) object;
        if ((this.plaPk == null && other.plaPk != null) || (this.plaPk != null && !this.plaPk.equals(other.plaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPlaza[ plaPk=" + plaPk + " ]";
    }
    
}
