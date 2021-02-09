/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpcPk", scope = SgControlAsistenciaPersonalCabezal.class)
public class SgControlAsistenciaPersonalCabezal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long cpcPk;
    
    private SgSede cpcSede;
    
    private LocalDate cpcFecha;
    
    private Integer cpcPersonalEnLista;
    
    private Integer cpcPersonalPresente;
    
    private Integer cpcPersonalAusentesJustificados;
    
    private Integer cpcPersonalAusentesSinJustificar;
    
    private LocalDateTime cpcUltModFecha;
    
    private String cpcUltModUsuario;
    
    private Integer cpcVersion;
    
    private List<SgAsistenciaPersonal> cpcAsistenciaPersonal;

    public SgControlAsistenciaPersonalCabezal() {
        this.cpcAsistenciaPersonal = new ArrayList<>();
        this.cpcPersonalAusentesJustificados = 0;
        this.cpcPersonalAusentesSinJustificar = 0;
    }

    public Long getCantidadPersonalPresentes() {
        if (this.cpcAsistenciaPersonal != null) {
            return this.cpcAsistenciaPersonal.stream().filter(a -> BooleanUtils.isFalse(a.getApeInasistencia())).count();
        }
        return null;
    }

    public SgControlAsistenciaPersonalCabezal(Long cpcPk) {
        this.cpcPk = cpcPk;
    }

    public Long getCpcPk() {
        return cpcPk;
    }

    public void setCpcPk(Long cpcPk) {
        this.cpcPk = cpcPk;
    }

    public SgSede getCpcSede() {
        return cpcSede;
    }

    public void setCpcSede(SgSede cpcSede) {
        this.cpcSede = cpcSede;
    }

    public LocalDate getCpcFecha() {
        return cpcFecha;
    }

    public void setCpcFecha(LocalDate cpcFecha) {
        this.cpcFecha = cpcFecha;
    }

    public Integer getCpcPersonalEnLista() {
        return cpcPersonalEnLista;
    }

    public void setCpcPersonalEnLista(Integer cpcPersonalEnLista) {
        this.cpcPersonalEnLista = cpcPersonalEnLista;
    }

    public Integer getCpcPersonalPresente() {
        return cpcPersonalPresente;
    }

    public void setCpcPersonalPresente(Integer cpcPersonalPresente) {
        this.cpcPersonalPresente = cpcPersonalPresente;
    }

    public Integer getCpcPersonalAusentesJustificados() {
        return cpcPersonalAusentesJustificados;
    }

    public void setCpcPersonalAusentesJustificados(Integer cpcPersonalAusentesJustificados) {
        this.cpcPersonalAusentesJustificados = cpcPersonalAusentesJustificados;
    }

    public Integer getCpcPersonalAusentesSinJustificar() {
        return cpcPersonalAusentesSinJustificar;
    }

    public void setCpcPersonalAusentesSinJustificar(Integer cpcPersonalAusentesSinJustificar) {
        this.cpcPersonalAusentesSinJustificar = cpcPersonalAusentesSinJustificar;
    }

    public LocalDateTime getCpcUltModFecha() {
        return cpcUltModFecha;
    }

    public void setCpcUltModFecha(LocalDateTime cpcUltModFecha) {
        this.cpcUltModFecha = cpcUltModFecha;
    }

    public String getCpcUltModUsuario() {
        return cpcUltModUsuario;
    }

    public void setCpcUltModUsuario(String cpcUltModUsuario) {
        this.cpcUltModUsuario = cpcUltModUsuario;
    }

    public Integer getCpcVersion() {
        return cpcVersion;
    }

    public void setCpcVersion(Integer cpcVersion) {
        this.cpcVersion = cpcVersion;
    }

    public List<SgAsistenciaPersonal> getCpcAsistenciaPersonal() {
        return cpcAsistenciaPersonal;
    }

    public void setCpcAsistenciaPersonal(List<SgAsistenciaPersonal> cpcAsistenciaPersonal) {
        this.cpcAsistenciaPersonal = cpcAsistenciaPersonal;
    }


    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpcPk != null ? cpcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgControlAsistenciaPersonalCabezal)) {
            return false;
        }
        SgControlAsistenciaPersonalCabezal other = (SgControlAsistenciaPersonalCabezal) object;
        if ((this.cpcPk == null && other.cpcPk != null) || (this.cpcPk != null && !this.cpcPk.equals(other.cpcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaPersonalCabezal[ cpcPk=" + cpcPk + " ]";
    }
    
}
