/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadiccionario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sofis
 */
public class GeneraDiccionario {

    private static final String esquema="EDUARDO_DESAROLLO";
    private static final String user="EDUARDO_DESAROLLO";
    private static final String owner = "EDUARDO_DESAROLLO";
    
    private static final String password="sofis2uy";
    private static final String url="jdbc:oracle:thin:@192.168.1.122:1521:SOFIS";
    
    
    private static final String TEMPLATE_FILA="/home/rgonzalez/documentacion/salvador/generacionDoc/fila.xml";
    private static final String TEMPLATE_TABLA="/home/rgonzalez/documentacion/salvador/generacionDoc/tabla.xml";
    private static final String TEMPLATE_CONTENT="/home/rgonzalez/documentacion/salvador/generacionDoc/content.xml";
    
    private static final String SALIDA ="/home/rgonzalez/documentacion/salvador/generacionDoc/content2.xml";
    
    
    /**
     * @param args the command line arguments
     * 
     * Trabaja con las plantillas ubicadas en el directorio: C:\ElSalvador\generacionDoc
     * Genera content2.xml hay que copiarlo a C:\ElSalvador\generacionDoc\kk y renombrarlo en kk para que se llame content2.xml
     * Luego hay que sobrescribir el content.xml del documento DOC-DSI-006.odt ubicado en kk
     * 
     */
    public static void main(String[] args) {

        try {
            GeneraDiccionario generaDiccionario = new GeneraDiccionario();
            Connection con = generaDiccionario.getConnection();
            Statement stmt = con.createStatement();
            
            String stQuery = "select table_name,column_name,data_type,data_length, data_precision,nullable,data_default "+
                             "from all_tab_columns where owner = '"+ owner +"' and table_name like 'SS%' and table_name not like '%_HIST' " +
                             "order by table_name,column_id";
            
            String stQuery2 = "select comments from all_tab_comments where table_name = ?";
            PreparedStatement pstmt = con.prepareStatement(stQuery2);
            
            String stQuery3 = "select comments from all_col_comments where owner = '"+ owner +"' and table_name = ? and column_name = ?";
            PreparedStatement pstmt2 = con.prepareStatement(stQuery3);
            
            String stQuery4 = "select unique column_name, all_constraints.table_name " +
                              "from all_constraints, all_cons_columns " +
                              "where all_constraints.constraint_name = all_cons_columns.constraint_name and "+
                              "all_constraints.table_name = ? and " +
                              "constraint_type = ?";
            PreparedStatement pstmt3 = con.prepareStatement(stQuery4);
            
            List<TablaTO> listaTablas = new ArrayList<TablaTO>();
            ResultSet rs = stmt.executeQuery(stQuery);
            String tableNameAnterior = "UNA_QUE_NO_ESTA";
            TablaTO tablaTO = null;
            List<String> llavesPrimarias = new ArrayList<String>();
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (!tableName.equals(tableNameAnterior)){//Cargo datos de tabla
                    tableNameAnterior = tableName;
                    tablaTO = new TablaTO();
                    tablaTO.setEsquema(esquema);
                    tablaTO.setNombre(tableName);
                    //Busco el comentario de la tabla
                    pstmt.setString(1, tableName);
                    ResultSet rs2 = pstmt.executeQuery();
                    if (rs2.next()){
                        tablaTO.setComentario(rs2.getString("COMMENTS"));
                    }
                    rs2.close();
                    listaTablas.add(tablaTO);
                    //Busco llaves primarias
                    llavesPrimarias.clear();
                    pstmt3.setString(1, tableName);
                    pstmt3.setString(2, "P");//constraint_type Primary
                    ResultSet rs4 = pstmt3.executeQuery();
                    while (rs4.next()){
                        llavesPrimarias.add(rs4.getString("COLUMN_NAME"));
                    }
                }
                ColumnaTO columnaTO = new ColumnaTO();
                columnaTO.setNombre(rs.getString("COLUMN_NAME"));
                columnaTO.setTipo(rs.getString("DATA_TYPE"));
                columnaTO.setLongitud(rs.getString("DATA_LENGTH"));
                if (rs.getString("DATA_PRECISION") != null && rs.getString("DATA_PRECISION").trim().length()>0){
                    columnaTO.setLongitud(columnaTO.getLongitud().trim()+","+rs.getString("DATA_PRECISION").trim());
                }
                if (rs.getString("NULLABLE").equals("Y")){
                    columnaTO.setPermiteNulos("SI");
                }else{
                    columnaTO.setPermiteNulos("NO");
                }
                columnaTO.setValorPorDefecto(rs.getString("DATA_DEFAULT"));
                //Me fijo si la columna es clave primaria
                if (generaDiccionario.esClavePrimaria(llavesPrimarias, columnaTO.getNombre())){
                    columnaTO.setLlavePrimaria("SI");
                }else{
                    columnaTO.setLlavePrimaria("NO");
                }
                
                //Busco el comentario de la columna
                pstmt2.setString(1, tableName);
                pstmt2.setString(2, columnaTO.getNombre());
                ResultSet rs3 = pstmt2.executeQuery();
                if (rs3.next()){
                    columnaTO.setDescripcion(rs3.getString("COMMENTS"));
                }
                rs3.close();
                tablaTO.getColumnas().add(columnaTO);
            }
            
            rs.close();
            con.close();

            String txtPlantillaFila = generaDiccionario.leoPlantilla(TEMPLATE_FILA);
            String txtPlantillaTabla = generaDiccionario.leoPlantilla(TEMPLATE_TABLA);
            String txtPlantillaContent = generaDiccionario.leoPlantilla(TEMPLATE_CONTENT);
            
            //Hasta aca tengo listaTablas con la informacion de la base de datos
            String stTablas = "";
            for (TablaTO tablaTO2: listaTablas){
                String stFilas = "";
                for(ColumnaTO columnaTO: tablaTO2.getColumnas()){
                    String filaColumna = generaDiccionario.armoFila(txtPlantillaFila, columnaTO.getNombre(),columnaTO.getTipo(),
                            columnaTO.getLongitud(),columnaTO.getValorPorDefecto(),columnaTO.getPermiteNulos(),columnaTO.getLlavePrimaria(),
                            columnaTO.getDescripcion());
                    stFilas = stFilas+filaColumna;
                }
                String stTabla = txtPlantillaTabla.replaceFirst("REPLACE_FILAS", stFilas);
                stTabla = stTabla.replaceFirst("REPLACE_NOMBRE_TABLA", generaDiccionario.valor(tablaTO2.getNombre()));
                stTabla = stTabla.replaceFirst("REPLACE_NOMBRE_ESQUEMA", generaDiccionario.valor(tablaTO2.getEsquema()));
                stTabla = stTabla.replaceFirst("REPLACE_DESCRIPCION_TABLA", generaDiccionario.valor(tablaTO2.getComentario()));
                stTablas = stTablas + stTabla;
                
            }
            String txtContent = txtPlantillaContent.replaceFirst("REPLACE_TABLAS", stTablas);
            generaDiccionario.escriboArchivo(SALIDA, txtContent);
            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
    
    private String armoFila(String textoFila, String nombreCampo, String tipoCampo, String longitud, String valorPorDefecto,
            String permiteNulos, String llavePrimaria, String descripcionCampo) {
        
        String texto = textoFila;
        texto = texto.replaceFirst("REPLACE_NOMBRE_CAMPO", valor(nombreCampo));
        texto = texto.replaceFirst("REPLACE_TIPO_CAMPO", valor(tipoCampo));
        texto = texto.replaceFirst("REPLACE_LONGITUD", valor(longitud));
        texto = texto.replaceFirst("REPLACE_VALOR_POR_DEFECTO", valor(valorPorDefecto));
        texto = texto.replaceFirst("REPLACE_PERMITE_NULOS", valor(permiteNulos));
        texto = texto.replaceFirst("REPLACE_LLAVE_PRIMARIA", valor(llavePrimaria));
        texto = texto.replaceFirst("REPLACE_DESCRIPCION_CAMPO", valor(descripcionCampo));

        return texto;
    }
    
    private String valor(String txt){
        if (txt != null){
            return txt;
        }else{
            return "";
        }
    }
    
    private String leoPlantilla(String archivo) {
        String respuesta = "";
        try {
            String linea;
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            while ((linea = b.readLine()) != null) {
                respuesta = respuesta + linea;
            }
            b.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }
    
    private boolean esClavePrimaria(List<String> llavesPrimarias, String campo){
        boolean respuesta = false;
        for(String clave: llavesPrimarias){
            if (campo.equals(clave)){
                respuesta = true;
            }
        }
        return respuesta;
    }
    
    private void escriboArchivo(String nombreArchivo, String txt){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(nombreArchivo);
            pw = new PrintWriter(fichero);
 
            pw.println(txt);
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }

}
