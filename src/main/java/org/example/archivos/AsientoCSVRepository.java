package org.example.archivos;

import org.example.modelo.Asiento;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsientoCSVRepository {
    private String rutaArchivo;

    public AsientoCSVRepository(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public List<Asiento> cargarAsientos() throws IOException {
        List<Asiento> listaAsientos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean esEncabezado = true;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // Omitir la primera línea de la cabecera del CSV
                if (esEncabezado && (linea.contains("idEvento") || linea.contains("codigoAsiento"))) {
                    esEncabezado = false;
                    continue;
                }

                String[] datos = linea.split(";");
                if (datos.length >= 6) {
                    try {
                        String idEvento = datos[0].trim();
                        String codigo = datos[1].trim();
                        String localidad = datos[2].trim();
                        String fila = datos[3].trim();
                        int numero = Integer.parseInt(datos[4].trim());
                        double precio = Double.parseDouble(datos[5].trim());

                        Asiento asiento = new Asiento(idEvento, codigo, localidad, fila, numero, precio);
                        listaAsientos.add(asiento);
                    } catch (NumberFormatException e) {
                        System.err.println("Fila omitida por formato numérico inválido: " + linea);
                    }
                }
            }
        }
        return listaAsientos;
    }
}