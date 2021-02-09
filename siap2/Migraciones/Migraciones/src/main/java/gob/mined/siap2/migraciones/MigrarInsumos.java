/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.migraciones;

import static gob.mined.siap2.migraciones.MigrarOEG.archivoIn;
import static gob.mined.siap2.migraciones.MigrarOEG.archivoOUT;
import static gob.mined.siap2.migraciones.MigrarOEG.insertSentence;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Rodrigo
 */
public class MigrarInsumos {

    public static String archivoIn="C:\\Users\\Rodrigo\\Desktop\\catalogo modificado.xls";
    public static String archivoOUT="C:\\Users\\Rodrigo\\Desktop\\salidaInsertInsumos.sql";
   
    public static String insertSentence = "INSERT INTO ss_insumo (ins_cod, ins_nombre, ins_articulo, ins_oeg_cod, ins_version) VALUES ('%s', '%s', %d, %d, ins_version);";

        
    public static void main(String[] argv) {
        System.out.println("-------- Oracle JDBC Connection Testing ------");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection(
                    "jjdbc:oracle:thin:@192.168.1.122:1521:SOFIS",
                    "siap",
                    "sofis2uy");

            
            
            
            
            FileOutputStream fos = new FileOutputStream(new File(archivoOUT));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            FileInputStream file = new FileInputStream(new File(archivoIn));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Integer idOnu = null;
                Integer idOEG = null;
                String nombre = null;
                String codigoManual = null;

                try {

                    //For each row, iterate through each columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int iter = 0;
                    while (cellIterator.hasNext() && iter < 6 && iter >= 0) {
                        Cell cell = cellIterator.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (iter == 0) {
                            String temp = cell.getStringCellValue();
                            idOnu = Integer.valueOf(temp);
                            idOnu= getArticuloONU(connection, idOnu );
                        } else if (iter == 1) {
                            nombre = cell.getStringCellValue();
                        } else if (iter == 2) {
                            String temp = cell.getStringCellValue();
                            idOEG = Integer.valueOf(temp);
                            idOEG= getOEGByCode(connection, idOEG );
                        } else if (iter == 4) {
                            codigoManual = cell.getStringCellValue();
                        }
                        iter++;
                    }
                    
                    String s =String.format(insertSentence, codigoManual, nombre, idOnu, idOEG);
//                    listaDeID.add(clasificador);
//                    if (padre!= null && !listaDeID.contains(padre)){
//                           System.out.println("-----   ERROR NO EXISTE EL PADRE PARA EL CLASIFICADOR: "+ clasificador + ", PADRE: " + padre);
//                    }
                     //System.out.println("crea el clasificador: "+ s);
                    bw.write(s);
                    bw.newLine();
                } catch (Exception e) {
                    System.out.println("-----   ERROR AL PROCESAR CELDA insumo: idOnu: "+idOnu+", idOEG: "+idOEG+", nombre: "+nombre+", codigoManual: "+codigoManual+"");
                }
            }
            bw.close();
            file.close();
            
            
            
            
            
            
            
            
            
            
            
            System.out.println("-------- termino corrrectamente ------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    //chequea la existencia del oeg
    public static Integer getOEGByCode(Connection connection, Integer code) throws Exception {
        String sql = "select o.obj_clasificador as resultado from ss_objeto_esp_gasto o where o.obj_clasificador = " +code;
        PreparedStatement preStatement = connection.prepareStatement(sql);
        ResultSet result = preStatement.executeQuery();
        while (result.next()) {
            return result.getInt("resultado");
        }
        //throw new Exception("No se encontro OEG con id " + code);
        System.out.println("- No se encontro OEG con id " + code);
        return null;

    }
    
    
    
        //chequea la existencia del oeg
    public static Integer getArticuloONU(Connection connection, Integer code) throws Exception {
        String sql = "select o.cod_id as resultado from ss_cod_ins_articulo o where o.cod_codigo = '" +code + "'";
        PreparedStatement preStatement = connection.prepareStatement(sql);
        ResultSet result = preStatement.executeQuery();
        while (result.next()) {
            return result.getInt("resultado");
        }
        //throw new Exception("No se articulo onu con id " + code);
        System.out.println("* No se encontro articulo con id " + code);
        return null;

    }

}
