/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.migraciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Rodrigo
 */
public class MigrarOEG {

    public static String archivoIn="C:\\Users\\Rodrigo\\Desktop\\Nuevo clasificador presupuestario.xls";
    public static String archivoOUT="C:\\Users\\Rodrigo\\Desktop\\salida.sql";
    
    public static String insertSentence = "INSERT INTO ss_objeto_especifico_gasto (obj_clasificador, obj_padre, obj_nombre, obj_descripcion, obj_version) VALUES (%d, %d, '%s', '%s',  1);";

    public static void main(String[] param) throws FileNotFoundException, IOException {
        try {
            
            List<Integer> listaDeID = new LinkedList();
            
            FileOutputStream fos = new FileOutputStream(new File(archivoOUT));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            FileInputStream file = new FileInputStream(new File(archivoIn));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Integer clasificador = null;
                Integer padre = null;
                String nombre = null;
                String descripcion = null;

                try {

                    //For each row, iterate through each columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int iter = 0;
                    while (cellIterator.hasNext() && iter < 4 && iter >= 0) {
                        Cell cell = cellIterator.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (iter == 0) {
                            String temp = cell.getStringCellValue();
                            clasificador = Integer.valueOf(temp);
                        } else if (iter == 1) {
                            String temp = cell.getStringCellValue();
                            if (temp != null && temp.length() > 0) {
                                padre = Integer.valueOf(temp);
                            }
                        } else if (iter == 2) {
                            nombre = cell.getStringCellValue();
                        } else if (iter == 3) {
                            descripcion = cell.getStringCellValue();
                        }
                        iter++;
                    }
                    
                    String s =String.format(insertSentence, clasificador, padre, nombre, descripcion);
                    listaDeID.add(clasificador);
                    if (padre!= null && !listaDeID.contains(padre)){
                    }
                    bw.write(s);
                    bw.newLine();
                } catch (Exception e) {
                }

            }
            bw.close();
            file.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
