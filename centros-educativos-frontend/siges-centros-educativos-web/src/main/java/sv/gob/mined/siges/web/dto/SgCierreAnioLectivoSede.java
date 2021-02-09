/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import sv.gob.mined.siges.web.dto.catalogo.SgPreguntaCierreAnio;

/**
 *
 * @author bruno
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "calPk", scope = SgCierreAnioLectivoSede.class)
public class SgCierreAnioLectivoSede implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long calPk;
    
    private SgAnioLectivo calAnioLectivo;
    
    private SgSede calSede;
   
    private LocalDateTime calUltModFecha;
    
    private String calUltModUsuario;

    private Integer calVersion;
    
    private List<SgRelPreguntaCierreAnioSede> calRelPreguntaCierreAnio;
    
    private List<SgResumenCierreSeccion> calResumenSecciones;

    private Integer calPromocionesPendientesMasc;

    private Integer calPromocionesPendientesFem;

    private Integer calPromovidosMasc;

    private Integer calPromovidosFem;

    private Integer calNoPromovidosMasc;

    private Integer calNoPromovidosFem;

    private Integer calAsistencias;

    private Integer calInasistencias;

    private Integer calInasistenciasJust;

    private Integer calSolicitudesTitulosMasc;

    private Integer calSolicitudesTitulosFem;
       
    private LocalDateTime calFechaCierre;
    
    private String calUsuarioCierre;
    
    private SgArchivo calArchivoFirmado;
    
    private Boolean calFirmado;
    
    private String calFirmaTransactionId; // Transaction id
    
    //Auxiliares firma
    
    private String calTransactionSignatureUrl; // Url a donde redirigir para firmar
    
    private String calTransactionReturnUrl; // Url a donde redirigir luego de firma

    public SgCierreAnioLectivoSede() {
    }
    
    
    public void actualizarPreguntas(List<SgPreguntaCierreAnio> preguntas) {
        if (preguntas == null){
            return;
        }
        if (this.calRelPreguntaCierreAnio == null) {
            this.calRelPreguntaCierreAnio = new ArrayList<>();
        }
        if (this.calPk == null) {
            for (SgPreguntaCierreAnio p : preguntas) {
                SgRelPreguntaCierreAnioSede rel = new SgRelPreguntaCierreAnioSede();
                rel.setRpcPreguntaCierreAnio(p);
                this.getCalRelPreguntaCierreAnio().add(rel);
            }
        } else {
            List<SgPreguntaCierreAnio> preguntasExistentes = this.calRelPreguntaCierreAnio.stream().map(r -> r.getRpcPreguntaCierreAnio()).collect(Collectors.toList());
            for (SgPreguntaCierreAnio p : preguntas) {
                if (!preguntasExistentes.contains(p)) {
                    SgRelPreguntaCierreAnioSede rel = new SgRelPreguntaCierreAnioSede();
                    rel.setRpcPreguntaCierreAnio(p);
                    this.getCalRelPreguntaCierreAnio().add(rel);
                }
            }
        }
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public List<SgRelPreguntaCierreAnioSede> getCalRelPreguntaCierreAnio() {
        return calRelPreguntaCierreAnio;
    }

    public void setCalRelPreguntaCierreAnio(List<SgRelPreguntaCierreAnioSede> calRelPreguntaCierreAnio) {
        this.calRelPreguntaCierreAnio = calRelPreguntaCierreAnio;
    }

    public SgAnioLectivo getCalAnioLectivo() {
        return calAnioLectivo;
    }

    public void setCalAnioLectivo(SgAnioLectivo calAnioLectivo) {
        this.calAnioLectivo = calAnioLectivo;
    }

    public SgSede getCalSede() {
        return calSede;
    }

    public void setCalSede(SgSede calSede) {
        this.calSede = calSede;
    }

    public LocalDateTime getCalUltModFecha() {
        return calUltModFecha;
    }

    public void setCalUltModFecha(LocalDateTime calUltModFecha) {
        this.calUltModFecha = calUltModFecha;
    }

    public String getCalUltModUsuario() {
        return calUltModUsuario;
    }

    public void setCalUltModUsuario(String calUltModUsuario) {
        this.calUltModUsuario = calUltModUsuario;
    }

    public Integer getCalVersion() {
        return calVersion;
    }

    public void setCalVersion(Integer calVersion) {
        this.calVersion = calVersion;
    }

    public List<SgResumenCierreSeccion> getCalResumenSecciones() {
        return calResumenSecciones;
    }

    public void setCalResumenSecciones(List<SgResumenCierreSeccion> calResumenSecciones) {
        this.calResumenSecciones = calResumenSecciones;
    }

    public Integer getCalPromocionesPendientesMasc() {
        return calPromocionesPendientesMasc;
    }

    public void setCalPromocionesPendientesMasc(Integer calPromocionesPendientesMasc) {
        this.calPromocionesPendientesMasc = calPromocionesPendientesMasc;
    }

    public Integer getCalPromocionesPendientesFem() {
        return calPromocionesPendientesFem;
    }

    public void setCalPromocionesPendientesFem(Integer calPromocionesPendientesFem) {
        this.calPromocionesPendientesFem = calPromocionesPendientesFem;
    }

    public Integer getCalPromovidosMasc() {
        return calPromovidosMasc;
    }

    public void setCalPromovidosMasc(Integer calPromovidosMasc) {
        this.calPromovidosMasc = calPromovidosMasc;
    }

    public Integer getCalPromovidosFem() {
        return calPromovidosFem;
    }

    public void setCalPromovidosFem(Integer calPromovidosFem) {
        this.calPromovidosFem = calPromovidosFem;
    }

    public Integer getCalNoPromovidosMasc() {
        return calNoPromovidosMasc;
    }

    public void setCalNoPromovidosMasc(Integer calNoPromovidosMasc) {
        this.calNoPromovidosMasc = calNoPromovidosMasc;
    }

    public Integer getCalNoPromovidosFem() {
        return calNoPromovidosFem;
    }

    public void setCalNoPromovidosFem(Integer calNoPromovidosFem) {
        this.calNoPromovidosFem = calNoPromovidosFem;
    }

    public Integer getCalSolicitudesTitulosMasc() {
        return calSolicitudesTitulosMasc;
    }

    public void setCalSolicitudesTitulosMasc(Integer calSolicitudesTitulosMasc) {
        this.calSolicitudesTitulosMasc = calSolicitudesTitulosMasc;
    }

    public Integer getCalSolicitudesTitulosFem() {
        return calSolicitudesTitulosFem;
    }

    public void setCalSolicitudesTitulosFem(Integer calSolicitudesTitulosFem) {
        this.calSolicitudesTitulosFem = calSolicitudesTitulosFem;
    }

    public Integer getCalAsistencias() {
        return calAsistencias;
    }

    public void setCalAsistencias(Integer calAsistencias) {
        this.calAsistencias = calAsistencias;
    }

    public Integer getCalInasistencias() {
        return calInasistencias;
    }

    public void setCalInasistencias(Integer calInasistencias) {
        this.calInasistencias = calInasistencias;
    }

    public Integer getCalInasistenciasJust() {
        return calInasistenciasJust;
    }

    public void setCalInasistenciasJust(Integer calInasistenciasJust) {
        this.calInasistenciasJust = calInasistenciasJust;
    }

    public LocalDateTime getCalFechaCierre() {
        return calFechaCierre;
    }

    public void setCalFechaCierre(LocalDateTime calFechaCierre) {
        this.calFechaCierre = calFechaCierre;
    }

    public String getCalUsuarioCierre() {
        return calUsuarioCierre;
    }

    public void setCalUsuarioCierre(String calUsuarioCierre) {
        this.calUsuarioCierre = calUsuarioCierre;
    }

    public SgArchivo getCalArchivoFirmado() {
        return calArchivoFirmado;
    }

    public void setCalArchivoFirmado(SgArchivo calArchivoFirmado) {
        this.calArchivoFirmado = calArchivoFirmado;
    }

    public Boolean getCalFirmado() {
        return calFirmado;
    }

    public void setCalFirmado(Boolean calFirmado) {
        this.calFirmado = calFirmado;
    }

    public String getCalFirmaTransactionId() {
        return calFirmaTransactionId;
    }

    public void setCalFirmaTransactionId(String calFirmaTransactionId) {
        this.calFirmaTransactionId = calFirmaTransactionId;
    }

    public String getCalTransactionSignatureUrl() {
        return calTransactionSignatureUrl;
    }

    public void setCalTransactionSignatureUrl(String calTransactionSignatureUrl) {
        this.calTransactionSignatureUrl = calTransactionSignatureUrl;
    }

    public String getCalTransactionReturnUrl() {
        return calTransactionReturnUrl;
    }

    public void setCalTransactionReturnUrl(String calTransactionReturnUrl) {
        this.calTransactionReturnUrl = calTransactionReturnUrl;
    }
      
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.calPk);
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
        final SgCierreAnioLectivoSede other = (SgCierreAnioLectivoSede) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgCierreAnioLectivoSede{" + "calPk=" + calPk + '}';
    }
}
