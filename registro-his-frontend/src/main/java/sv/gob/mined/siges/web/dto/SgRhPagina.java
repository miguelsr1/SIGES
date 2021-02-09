/*  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import sv.gob.mined.siges.web.dto.centroseducativos.SgNivel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pagPk", scope = SgRhPagina.class) 
public class SgRhPagina implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pagPk;
    
    private Integer pagLibro;
    
    private Integer pagPagina;
        
    private Integer pagAnio;

    private SgNivel pagNivel;
      
    private String pagRuta;
    
    private String pagNombreLibro;
 
    private Boolean pagHabilitado;

    private LocalDateTime pagUltModFecha;

    private String pagUltModUsuario;

    private Integer pagVersion;

    public SgRhPagina() {
        pagHabilitado=Boolean.TRUE;
    }

    public Long getPagPk() {
        return pagPk;
    }

    public void setPagPk(Long pagPk) {
        this.pagPk = pagPk;
    }

    public Integer getPagLibro() {
        return pagLibro;
    }

    public void setPagLibro(Integer pagLibro) {
        this.pagLibro = pagLibro;
    }

    public Integer getPagPagina() {
        return pagPagina;
    }

    public void setPagPagina(Integer pagPagina) {
        this.pagPagina = pagPagina;
    }

    public Integer getPagAnio() {
        return pagAnio;
    }

    public void setPagAnio(Integer pagAnio) {
        this.pagAnio = pagAnio;
    }

    public SgNivel getPagNivel() {
        return pagNivel;
    }

    public void setPagNivel(SgNivel pagNivel) {
        this.pagNivel = pagNivel;
    }

    public String getPagRuta() {
        return pagRuta;
    }

    public void setPagRuta(String pagRuta) {
        this.pagRuta = pagRuta;
    }

    public String getPagNombreLibro() {
        return pagNombreLibro;
    }

    public void setPagNombreLibro(String pagNombreLibro) {
        this.pagNombreLibro = pagNombreLibro;
    }

    public Boolean getPagHabilitado() {
        return pagHabilitado;
    }

    public void setPagHabilitado(Boolean pagHabilitado) {
        this.pagHabilitado = pagHabilitado;
    }

    public LocalDateTime getPagUltModFecha() {
        return pagUltModFecha;
    }

    public void setPagUltModFecha(LocalDateTime pagUltModFecha) {
        this.pagUltModFecha = pagUltModFecha;
    }

    public String getPagUltModUsuario() {
        return pagUltModUsuario;
    }

    public void setPagUltModUsuario(String pagUltModUsuario) {
        this.pagUltModUsuario = pagUltModUsuario;
    }

    public Integer getPagVersion() {
        return pagVersion;
    }

    public void setPagVersion(Integer pagVersion) {
        this.pagVersion = pagVersion;
    }


     
}
