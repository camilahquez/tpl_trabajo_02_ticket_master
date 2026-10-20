package org.example.servicios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorCompraServiceTest {
    private ValidadorCompraService validador;

    @BeforeEach
    void crearValidadorConUnAsiento(@TempDir Path carpeta) throws IOException {
        Path csv = carpeta.resolve("asientos.csv");
        Files.writeString(csv, """
                idEvento;codigoAsiento;localidad;fila;numero;precio
                EV001;VIP-A-01;VIP;A;1;350000
                """);
        AsientoService asientoService = new AsientoService(csv.toString());

        validador = new ValidadorCompraService(asientoService);
    }
    @ParameterizedTest
    @ValueSource(strings = {"laura@correo.com", "laura.gomez@mail.co", "l_g-1@uni.edu.co"})
    void correoValidoEsAceptado(String correo) {
        assertTrue(validador.verificarCorreo(correo));
    }
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "sin-arroba.com", "laura@", "laura@correo", "@correo.com"})
    void correoInvalidoOVacioEsRechazado(String correo) {
        assertFalse(validador.verificarCorreo(correo));
    }
    @Test
    void seleccionDeAsientosVaciaEsRechazada() {
        assertFalse(validador.verificarSeleccionAsientos(List.of()));
    }
}
