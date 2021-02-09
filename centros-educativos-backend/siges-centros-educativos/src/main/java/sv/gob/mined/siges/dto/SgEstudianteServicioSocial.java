/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "estPk", scope = SgEstudianteServicioSocial.class)
public class SgEstudianteServicioSocial implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "est_pk")
    private Long estPk;
    
    @Column(name = "est_realizo_servicio_social")
    private Boolean estRealizoServicioSocial;
    
    @Column(name = "est_fecha_servicio_social")
    private LocalDate estFechaServicioSocial;
    
    @Column(name = "est_cantidad_horas_servicio_social")
    private Integer estCantidadHorasServicioSocial;
    
    @Column(name = "est_descripcion_servicio_social")
    private String estDescripcionServicioSocial;
          
  
    public SgEstudianteServicioSocial() {
    }
    
    public SgEstudianteServicioSocial(Long estPk) {
        this.estPk = estPk;
    }

    public Long getEstPk() {
        return estPk;
    }

    public void setEstPk(Long estPk) {
        this.estPk = estPk;
    }

    public Boolean getEstRealizoServicioSocial() {
        return estRealizoServicioSocial;
    }

    public void setEstRealizoServicioSocial(Boolean estRealizoServicioSocial) {
        this.estRealizoServicioSocial = estRealizoServicioSocial;
    }

    public LocalDate getEstFechaServicioSocial() {
        return estFechaServicioSocial;
    }

    public void setEstFechaServicioSocial(LocalDate estFechaServicioSocial) {
        this.estFechaServicioSocial = estFechaServicioSocial;
    }

    public Integer getEstCantidadHorasServicioSocial() {
        return estCantidadHorasServicioSocial;
    }

    public void setEstCantidadHorasServicioSocial(Integer estCantidadHorasServicioSocial) {
        this.estCantidadHorasServicioSocial = estCantidadHorasServicioSocial;
    }

    public String getEstDescripcionServicioSocial() {
        return estDescripcionServicioSocial;
    }

    public void setEstDescripcionServicioSocial(String estDescripcionServicioSocial) {
        this.estDescripcionServicioSocial = estDescripcionServicioSocial;
    }


    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estPk != null ? estPk.hashCode() : 0);
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
        final SgEstudianteServicioSocial other = (SgEstudianteServicioSocial) obj;
        if (!Objects.equals(this.estPk, other.estPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudianteServicioSocial[ estPk=" + estPk + " ]";
    }

    

    

}
