
package com.dperez.maym.web.herramientas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;


@Named
@Stateless
@SessionScoped
public class CargarArchivo implements Serializable{
    
    private String homeDir = System.getProperty("user.home");
    private String separator = System.getProperty("file.separator");
    
    public CargarArchivo() {}
    
    /**
     * Devuelve el valor correspondiente al directorio del usuario
     * @return directorio de usuario (home)
     */
    public String getHome(){return homeDir;}
    
    /**
     * Devuelve el valor del caracter separador ("/"-"\") del sistema operativo del host
     * @return caracter separador
     */
    public String getSeparator(){return separator;}
    
    /**
     * Crea el directorio (si no existe) y guarda el archivo. <b>Si existe el archivo se sobreescribe</b>
     * @param Archivo
     * @param DirectorioArchivo
     * @param NombreArchivo Nombre del archivo + extension
     * @param NombreEmpresa
     * @return Devuelve un arreglo arr[] es la ubicacion del archivo guardado, arr[1] es la extension. Retorna un arreglo vacio si no se pudo guardar.
     */
    public String[] guardarArchivo(String DirectorioArchivo, Part Archivo, String NombreArchivo, String NombreEmpresa){
        String[] realPath = new String[2];
        if (Archivo != null) {
            try{
                String resPath = "MAyM_Web"+separator+"Adjuntos"+separator+NombreEmpresa+separator+DirectorioArchivo+separator;                
                String extensionArchivo = FilenameUtils.getExtension(Archivo.getSubmittedFileName());
                realPath[1] = extensionArchivo;
                NombreArchivo = NombreArchivo + "."+extensionArchivo;
                //String baseDatos = "/img/"+DirectorioArchivo+"/"+NombreArchivo;
                realPath[0] = homeDir+separator+resPath+NombreArchivo;                
                String[] baseDatos = {"/adjuntos/"+NombreEmpresa+"/"+DirectorioArchivo+"/"+NombreArchivo, extensionArchivo};
                Archivo.write(realPath[0]);
                return baseDatos;
            }catch(FileNotFoundException ex){
                System.out.println("Archivo no econtrado: " + ex.getMessage());
                realPath = new String[2];
            }catch(IOException ex){
                System.out.println("Error IO: " + ex.getMessage());
                realPath = new String[2];
            }
        }
        return realPath;
    }
    
    /**
     * Elimina el archivo especificado.
     * @param NombreUbicacionArchivo El nombre del archivo debe incluir Directorio/NombreArchivo.extension
     * @return Retorna <b>true</b> si se elimino correctamente de lo contrario retorna <b>False</b>.
     */
    public boolean BorrarArchivo(String NombreUbicacionArchivo){
        if(!NombreUbicacionArchivo.isEmpty()){
            String resPath = homeDir+separator+"MAyM_Web"+separator+NombreUbicacionArchivo;    
            File archivo = new File(resPath);
            archivo.delete();
            return true;
        }
        return false;
    }
    
}
