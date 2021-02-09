/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumTipoComponentePentaho;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mpePk", scope = SgMenuPentaho.class)
public class SgMenuPentaho implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long mpePk;

    private EnumTipoComponentePentaho mpeTipo;

    private String mpeNombre;

    private String mpeRuta;
    
    
    private SgOperacion mpeOperacionFk;

    private Boolean mpeHabilitado;

    private LocalDateTime mpeUltModFecha;

    private String mpeUltModUsuario;

    private Integer mpeVersion;


    public SgMenuPentaho() {
        this.mpeHabilitado = Boolean.TRUE;
    }    

    public Long getMpePk() {
        return mpePk;
    }

    public void setMpePk(Long mpePk) {
        this.mpePk = mpePk;
    }

    public EnumTipoComponentePentaho getMpeTipo() {
        return mpeTipo;
    }

    public void setMpeTipo(EnumTipoComponentePentaho mpeTipo) {
        this.mpeTipo = mpeTipo;
    }

    public String getMpeNombre() {
        return mpeNombre;
    }

    public void setMpeNombre(String mpeNombre) {
        this.mpeNombre = mpeNombre;
    }

    public String getMpeRuta() {
        return mpeRuta;
    }

    public void setMpeRuta(String mpeRuta) {
        this.mpeRuta = mpeRuta;
    }
    
    public Boolean getMpeHabilitado() {
        return mpeHabilitado;
    }

    public void setMpeHabilitado(Boolean mpeHabilitado) {
        this.mpeHabilitado = mpeHabilitado;
    }

    public LocalDateTime getMpeUltModFecha() {
        return mpeUltModFecha;
    }

    public void setMpeUltModFecha(LocalDateTime mpeUltModFecha) {
        this.mpeUltModFecha = mpeUltModFecha;
    }

    public String getMpeUltModUsuario() {
        return mpeUltModUsuario;
    }

    public void setMpeUltModUsuario(String mpeUltModUsuario) {
        this.mpeUltModUsuario = mpeUltModUsuario;
    }

    public Integer getMpeVersion() {
        return mpeVersion;
    }

    public void setMpeVersion(Integer mpeVersion) {
        this.mpeVersion = mpeVersion;
    }
    
    public SgOperacion getMpeOperacionFk() {
        return mpeOperacionFk;
    }

    public void setMpeOperacionFk(SgOperacion mpeOperacionFk) {
        this.mpeOperacionFk = mpeOperacionFk;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpePk != null ? mpePk.hashCode() : 0);
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
        final SgMenuPentaho other = (SgMenuPentaho) obj;
        if (!Objects.equals(this.mpePk, other.mpePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgMenuPentaho[ mpePk=" + mpePk + " ]";
    }    
}
