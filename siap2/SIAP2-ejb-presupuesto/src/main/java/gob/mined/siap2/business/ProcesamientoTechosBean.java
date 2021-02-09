/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless(name = "procesamientoTechosBean")
public class ProcesamientoTechosBean {
    @Inject
    private ProcesamientoTechosAsincBean procesamientoTechosAsincBean;
}
