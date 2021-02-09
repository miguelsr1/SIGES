/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.interfaces;

import uy.com.sofis.pfea.enums.TipoAccion;

/**
 *
 * @author Sofis Solutions
 */
public interface Auditable {
    
    public TipoAccion getTipoAccionAdd();
        
    public TipoAccion getTipoAccionEdit();
    
    public TipoAccion getTipoAccionDel();
    
    public Integer getId();
    
    public String getDatosAuditoria();
    
}
