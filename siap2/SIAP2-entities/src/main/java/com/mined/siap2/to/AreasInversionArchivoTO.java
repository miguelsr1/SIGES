/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mined.siap2.to;

import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;

/**
 *
 * @author bruno
 */
public class AreasInversionArchivoTO implements Serializable {
    private DataFile file;
    private AreasInversion area;
    private AreasInversion subArea;

    public DataFile getFile() {
        return file;
    }

    public void setFile(DataFile file) {
        this.file = file;
    }

    public AreasInversion getArea() {
        return area;
    }

    public void setArea(AreasInversion area) {
        this.area = area;
    }

    public AreasInversion getSubArea() {
        return subArea;
    }

    public void setSubArea(AreasInversion subArea) {
        this.subArea = subArea;
    }
    
    
}
