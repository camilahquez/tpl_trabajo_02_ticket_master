
package org.example.servicios;
import  org.example.excepciones.ErrorAuditoriaException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

public class AuditoriaService {
    private final Path archivo;

    public AuditoriaService(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
    }
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
