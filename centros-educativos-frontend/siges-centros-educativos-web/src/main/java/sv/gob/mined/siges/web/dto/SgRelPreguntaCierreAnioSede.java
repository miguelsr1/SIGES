/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.web.dto.catalogo.SgPreguntaCierreAnio;



@XmlRootElement
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rpcPk", scope = SgRelPreguntaCierreAnioSede.class)
public class SgRelPreguntaCierreAnioSede implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rpcPk;
    
    private SgCierreAnioLectivoSede rpcCierreAnioLectivoSede;
    
    private SgPreguntaCierreAnio rpcPreguntaCierreAnio;
    
    private Boolean rpcPreguntaValidada;
    
    private LocalDateTime rpcUltModFecha;
    
    private String rpcUltModUsuario;
    
    private Integer rpcVersion;
 

    public SgRelPreguntaCierreAnioSede() {
    }

    public SgRelPreguntaCierreAnioSede(Long rpcPk) {
        this.rpcPk = rpcPk;
    }

    public Long getRpcPk() {
        return rpcPk;
    }

    public void setRpcPk(Long rpcPk) {
        this.rpcPk = rpcPk;
    }
   
    public LocalDateTime getRpcUltModFecha() {
        return rpcUltModFecha;
    }

    public void setRpcUltModFecha(LocalDateTime rpcUltModFecha) {
        this.rpcUltModFecha = rpcUltModFecha;
    }

    public String getRpcUltModUsuario() {
        return rpcUltModUsuario;
    }

    public void setRpcUltModUsuario(String rpcUltModUsuario) {
        this.rpcUltModUsuario = rpcUltModUsuario;
    }

    public Integer getRpcVersion() {
        return rpcVersion;
    }

    public void setRpcVersion(Integer rpcVersion) {
        this.rpcVersion = rpcVersion;
    }

    public SgCierreAnioLectivoSede getRpcCierreAnioLectivoSede() {
        return rpcCierreAnioLectivoSede;
    }

    public void setRpcCierreAnioLectivoSede(SgCierreAnioLectivoSede rpcCierreAnioLectivoSede) {
        this.rpcCierreAnioLectivoSede = rpcCierreAnioLectivoSede;
    }

    public SgPreguntaCierreAnio getRpcPreguntaCierreAnio() {
        return rpcPreguntaCierreAnio;
    }

    public void setRpcPreguntaCierreAnio(SgPreguntaCierreAnio rpcPreguntaCierreAnio) {
        this.rpcPreguntaCierreAnio = rpcPreguntaCierreAnio;
    }

    public Boolean getRpcPreguntaValidada() {
        return rpcPreguntaValidada;
    }

    public void setRpcPreguntaValidada(Boolean rpcPreguntaValidada) {
        this.rpcPreguntaValidada = rpcPreguntaValidada;
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
        final SgRelPreguntaCierreAnioSede other = (SgRelPreguntaCierreAnioSede) obj;
        if (!Objects.equals(this.rpcPk, other.rpcPk)) {
            return false;
        }
        return true;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rpcPk != null ? rpcPk.hashCode() : 0);
        return hash;
    }

   

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelSedeServicioInfra[ rpcPk=" + rpcPk + " ]";
    }
    
}
