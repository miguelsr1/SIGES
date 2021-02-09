/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ritPk", scope = SgRelInmuebleServicioSanitario.class)
public class SgRelInmuebleServicioSanitario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ritPk;

    private Integer ritBueno;

    private Integer ritMalo;

    private Integer ritRegular;

    private Integer ritNinos;

    private Integer ritNinas;

    private Integer ritMaestros;

    private Integer ritAdministrativos;

    private LocalDateTime ritUltModFecha;

    private String ritUltModUsuario;

    private Integer ritVersion;

    private SgInmueblesSedes ritInmuebleSede;

    private SgTipoServicioSanitario ritServicioSanitario;

    public SgRelInmuebleServicioSanitario() {

    }
    
    @JsonIgnore
    public Integer getTotalCondiciones(){
        Integer total=0;
        total=total+(ritBueno!=null?ritBueno:0);
        total=total+(ritRegular!=null?ritRegular:0);
        total=total+(ritMalo!=null?ritMalo:0);
        return total;
    }
    
    @JsonIgnore
    public Integer getTotalTipoUsuario(){
        Integer total=0;
        total=total+(ritNinos!=null?ritNinos:0);
        total=total+(ritNinas!=null?ritNinas:0);
        total=total+(ritMaestros!=null?ritMaestros:0);
        total=total+(ritAdministrativos!=null?ritAdministrativos:0);
        return total;
    }

    public Long getRitPk() {
        return ritPk;
    }

    public void setRitPk(Long ritPk) {
        this.ritPk = ritPk;
    }

    public Integer getRitBueno() {
        return ritBueno;
    }

    public void setRitBueno(Integer ritBueno) {
        this.ritBueno = ritBueno;
    }

    public Integer getRitMalo() {
        return ritMalo;
    }

    public void setRitMalo(Integer ritMalo) {
        this.ritMalo = ritMalo;
    }

    public Integer getRitRegular() {
        return ritRegular;
    }

    public void setRitRegular(Integer ritRegular) {
        this.ritRegular = ritRegular;
    }

    public Integer getRitNinos() {
        return ritNinos;
    }

    public void setRitNinos(Integer ritNinos) {
        this.ritNinos = ritNinos;
    }

    public Integer getRitNinas() {
        return ritNinas;
    }

    public void setRitNinas(Integer ritNinas) {
        this.ritNinas = ritNinas;
    }

    public Integer getRitMaestros() {
        return ritMaestros;
    }

    public void setRitMaestros(Integer ritMaestros) {
        this.ritMaestros = ritMaestros;
    }

    public Integer getRitAdministrativos() {
        return ritAdministrativos;
    }

    public void setRitAdministrativos(Integer ritAdministrativos) {
        this.ritAdministrativos = ritAdministrativos;
    }

    public LocalDateTime getRitUltModFecha() {
        return ritUltModFecha;
    }

    public void setRitUltModFecha(LocalDateTime ritUltModFecha) {
        this.ritUltModFecha = ritUltModFecha;
    }

    public String getRitUltModUsuario() {
        return ritUltModUsuario;
    }

    public void setRitUltModUsuario(String ritUltModUsuario) {
        this.ritUltModUsuario = ritUltModUsuario;
    }

    public Integer getRitVersion() {
        return ritVersion;
    }

    public void setRitVersion(Integer ritVersion) {
        this.ritVersion = ritVersion;
    }

    public SgInmueblesSedes getRitInmuebleSede() {
        return ritInmuebleSede;
    }

    public void setRitInmuebleSede(SgInmueblesSedes ritInmuebleSede) {
        this.ritInmuebleSede = ritInmuebleSede;
    }

    public SgTipoServicioSanitario getRitServicioSanitario() {
        return ritServicioSanitario;
    }

    public void setRitServicioSanitario(SgTipoServicioSanitario ritServicioSanitario) {
        this.ritServicioSanitario = ritServicioSanitario;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgRelInmuebleServicioSanitario other = (SgRelInmuebleServicioSanitario) obj;
        if (!Objects.equals(this.ritPk, other.ritPk)) {
            return false;
        }
        if (!Objects.equals(this.ritInmuebleSede, other.ritInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.ritServicioSanitario, other.ritServicioSanitario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleServicioSanitario{" + "ritPk=" + ritPk + ", ritBueno=" + ritBueno + ", ritMalo=" + ritMalo + ", ritRegular=" + ritRegular + ", ritNinos=" + ritNinos + ", ritNinas=" + ritNinas + ", ritMaestros=" + ritMaestros + ", ritAdministrativos=" + ritAdministrativos + ", ritUltModFecha=" + ritUltModFecha + ", ritUltModUsuario=" + ritUltModUsuario + ", ritVersion=" + ritVersion + ", ritInmuebleSede=" + ritInmuebleSede + ", ritServicioSanitario=" + ritServicioSanitario + '}';
    }

}
