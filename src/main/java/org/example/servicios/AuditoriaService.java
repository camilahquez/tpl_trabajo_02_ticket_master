
package org.example.servicios;
import  org.example.excepciones.ErrorAuditoriaException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
//gestiona el guardado de las transaciones, guarda en un  archivo cada transaccion ya se una rechazada o una comfirmada, asi se conservsa un historial.
public class AuditoriaService {
    private final Path archivo;
    public AuditoriaService(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
    }

    //muestra el listado de todos las transaciones en el historial, en caso e que no se tengan transacines en el registro nos informa que todavia no hay registros en el hitorial. SI exite un error a la hora de abrir el archivo se informa que no se puedo hacer la consulta.
    public String mostrarRegistros(){
       try {
           if (!Files.exists(archivo)) {
               return "Todavía no hay registros de auditoría.";
           }

           return Files.readString(archivo);

       } catch (IOException e) {
           throw new ErrorAuditoriaException("No se pudo consultar la auditoría.", e);
       }
   }
//hace el guardado de las transaciones en el archivo con la hora en que se hizo, el aguardo simpre se hace al final de la lista. Cuando  no existe la carpeta donde se encuentra el archivo crea una. Al presentarse errores en el momento e escribir el nuervo registro en el archivo nos informa.
     public void registrar(String mensaje) {

        try {
            Path carpeta = archivo.getParent();

            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }

            String registro = LocalDateTime.now()
                    + " | " + mensaje;

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                writer.write(registro);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ErrorAuditoriaException(
                    "No se pudo registrar la operación en la auditoría.", e
            );
        }
    }

}
