package org.example.repositorios;

import org.example.modelo.Eventos;
import org.example.excepciones.ArchivoDatosException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class EventosCSVRepository {
    private static final String separador=";";
    private static final int columnasEsperadas= 6;

    private static final DateTimeFormatter formatoFecha=
            DateTimeFormatter.ofPattern( "d/M/yyyy" );
    private static final DateTimeFormatter formatoHora=
            DateTimeFormatter.ofPattern( "H:mm" );

    private final Path rutaArchivo;

    public EventosCSVRepository(String rutaArchivo){
        this.rutaArchivo= Path.of(rutaArchivo);
    }
    public List<Eventos> cargarEventos() throws ArchivoDatosException {
        List<Eventos> listaEventos = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(rutaArchivo)){
            br.readLine();
            String linea;
            int numeroLinea=1;

            while((linea=br.readLine()) != null){
                numeroLinea++;
                if (linea.isBlank()){
                    continue;
                }
                listaEventos.add(convertirFila(linea, numeroLinea));
            }

        } catch (IOException e) {
            throw new ArchivoDatosException("no se pudo leer el archivo"+ rutaArchivo,e);
        }
        return listaEventos;

    }
    private Eventos convertirFila(String linea,int numeroLinea)
            throws ArchivoDatosException {
        String[] datos = linea.split(separador);

        if (datos.length < columnasEsperadas) {
            throw new ArchivoDatosException(
                    "fila incompleta en " + rutaArchivo + ", linea " + numeroLinea);
        }

        try {
            String idEvento = datos[0].trim();
            String nombreEvento = datos[1].trim();
            LocalDate fechaEvento = LocalDate.parse(datos[2].trim(), formatoFecha);
            LocalTime horaEvento = LocalTime.parse(datos[3].trim(), formatoHora);
            String lugarEvento = datos[4].trim();
            String tipoEvento = datos[5].trim();

            return new Eventos(idEvento, nombreEvento, fechaEvento, horaEvento, lugarEvento, tipoEvento);

        } catch (DateTimeParseException e) {
            throw new ArchivoDatosException(
                    "fecha u hora invalida en " + rutaArchivo + ",linea " + numeroLinea, e);
        }
    }
}
