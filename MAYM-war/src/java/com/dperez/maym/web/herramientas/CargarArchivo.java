package com.dperez.maym.web.herramientas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import javax.ejb.Stateless;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;

@Named
@Stateless
@SessionScoped
public class CargarArchivo implements Serializable {

    private final String homeDir = System.getProperty("user.home");
    private final String separator = System.getProperty("file.separator");

    public CargarArchivo() {
    }

    /**
     * Devuelve el valor correspondiente al directorio del usuario
     *
     * @return directorio de usuario (home)
     */
    public String getHome() {
        return homeDir;
    }

    /**
     * Devuelve el valor del caracter separador ("/"-"\") del sistema operativo
     * del host
     *
     * @return caracter separador
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Crea el directorio (si no existe) y guarda el archivo. <b>Si existe el
     * archivo se sobreescribe</b>
     *
     * @param Archivo
     * @param DirectorioArchivo
     * @param NombreArchivo Nombre del archivo + extension
     * @param NombreEmpresa
     * @return Devuelve un arreglo arr[0] es la ubicacion del archivo guardado,
     * arr[1] es la extension. Retorna un arreglo vacio si no se pudo guardar.
     */
    public String[] guardarArchivo(String DirectorioArchivo, Part Archivo, String NombreArchivo, String NombreEmpresa) {
        String[] realPath = new String[2];
        if (Archivo != null) {
            try {
                String resPath = "MAYMWEB" + separator + "Adjuntos" + separator + NombreEmpresa + separator + DirectorioArchivo + separator;
                String extensionArchivo = FilenameUtils.getExtension(Archivo.getSubmittedFileName());
                realPath[1] = extensionArchivo;
                NombreArchivo = NombreArchivo + "." + extensionArchivo;

                realPath[0] = homeDir + separator + resPath + NombreArchivo;
                File fileSaveDir = new File(homeDir + separator + "MAYMWEB" + separator + "Adjuntos" + separator + NombreEmpresa + separator + DirectorioArchivo);
                
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }
                fileSaveDir = new File(realPath[0]);
                if(fileSaveDir.exists()){
                    fileSaveDir.delete();
                }
                String[] baseDatos = {separator + "adjuntos" + separator + NombreEmpresa + separator + DirectorioArchivo + separator + NombreArchivo, extensionArchivo};
                Archivo.write(realPath[0]);
                return baseDatos;
            } catch (FileAlreadyExistsException ex) {                
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
                System.out.println("Error IO: " + ex.getMessage());
                realPath = new String[2];
            }
        }
        return realPath;
    }

    /**
     * Crea el directorio (si no existe) y guarda el logo ded la empresa. <b>Si
     * existe el archivo se sobreescribe</b>
     *
     * @param Archivo
     * @param NombreEmpresa
     * @return Devuelve un arreglo arr[0] es la ubicacion del archivo guardado,
     * arr[1] es la extension. Retorna un arreglo vacio si no se pudo guardar.
     */
    public String guardarLogoEmpresa(Part Archivo, String NombreEmpresa) {
        String realPath = "";
        String ubicacion = "";
        if (Archivo != null) {
            try {
                String resPath = "MAYMWEB" + separator + "Configuracion" + separator;
                String extensionArchivo = FilenameUtils.getExtension(Archivo.getSubmittedFileName());

                String NombreArchivo = "logo_" + NombreEmpresa + "." + extensionArchivo;
                ubicacion = separator + "Configuracion" + separator + NombreArchivo;

                realPath = homeDir + separator + resPath + NombreArchivo;
                File fileSaveDir = new File(homeDir + separator + resPath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }
                fileSaveDir = new File(realPath);
                
                if(fileSaveDir.exists()){
                    fileSaveDir.delete();
                }
                Archivo.write(realPath);
                return ubicacion;

            } catch (FileAlreadyExistsException ex) {
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
                ubicacion = "";
            }
        }
        return ubicacion;
    }
    /**
     * Elimina el archivo especificado.
     *
     * @param NombreUbicacionArchivo El nombre del archivo debe incluir
     * Directorio/NombreArchivo.extension
     * @return Retorna <b>true</b> si se elimino correctamente de lo contrario
     * retorna <b>False</b>.
     */
    public boolean BorrarArchivo(String NombreUbicacionArchivo) {
        if (!NombreUbicacionArchivo.isEmpty()) {
            String resPath = homeDir + separator + "MAYMWEB" + NombreUbicacionArchivo;
            File archivo = new File(resPath);
            return archivo.delete();
        }
        return false;
    }
}
