package org.example.servicios;

import org.example.modelo.Asiento;
import org.example.repositorios.AsientoCSVRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AsientoCSVRepositoryTest {
    @Test
    void filaValidaSeConvierteEnAsiento(@TempDir Path carpeta) throws IOException {
        Path csv = carpeta.resolve("asientos.csv");
        Files.writeString(csv, """
                idEvento;codigoAsiento;localidad;fila;numero;precio;ocupado
                EV001;VIP-A-01;VIP;A;1;350000;false
                """);
        List<Asiento> asientos = new AsientoCSVRepository(csv.toString()).cargarAsientos();

        assertEquals(1, asientos.size());
        Asiento asiento = asientos.get(0);
        assertEquals("EV001", asiento.getIdEvento());
        assertEquals("VIP-A-01", asiento.getCodigoAsiento());
        assertEquals("VIP", asiento.getLocalidad());
        assertEquals(1, asiento.getNumero());
        assertEquals(350000.0, asiento.getPrecio(), 0.001);
        assertFalse(asiento.isOcupado());
    }
    @Test
    void filaConPrecioInvalidoSeOmiteYLasDemasSeCargan(@TempDir Path carpeta) throws IOException {
        Path csv = carpeta.resolve("asientos.csv");
        Files.writeString(csv, """
                idEvento;codigoAsiento;localidad;fila;numero;precio;ocupado
                EV001;VIP-A-01;VIP;A;1;350000;false
                EV001;VIP-A-02;VIP;A;2;abc;false
                EV001;VIP-A-03;VIP;A;3;350000;false
                """);
        List<Asiento> asientos = new AsientoCSVRepository(csv.toString()).cargarAsientos();

        assertEquals(2, asientos.size());
        assertEquals("VIP-A-01", asientos.get(0).getCodigoAsiento());
        assertEquals("VIP-A-03", asientos.get(1).getCodigoAsiento());
    }
}
