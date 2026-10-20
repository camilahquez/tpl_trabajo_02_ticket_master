
package org.example.repositorios;

import com.google.gson.Gson;
import org.example.modelo.Compra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompraJSONRepositoryTest {

    @TempDir
    Path directorioTemporal;

    @Test
    void debeSerializarYDeserializarUnaCompraEnJSON() throws Exception {

        Path archivoJson = directorioTemporal.resolve("compras.json");
        Files.writeString(archivoJson, "[]");
        String jsonOriginal = """
                {
                  "id": "COMPRA-001",
                  "nombreComprador": "camila",
                  "documentoComprador": "1039468510",
                  "correoComprador": "camila@gmail.com",
                  "nombreEvento": "Rock Medellin 2026",
                  "asientos": [
                    {
                      "idEvento": "EV001",
                      "codigoAsiento": "GEN-C-04",
                      "localidad": "General",
                      "fila": "C",
                      "numero": 4,
                      "precio": 150000.0,
                      "ocupado": true
                    }
                  ],
                  "subtotal": 150000.0,
                  "servicio": 15000.0,
                  "total": 165000.0
                }
                """;

        Gson gson = new Gson();
        Compra compraOriginal = gson.fromJson(jsonOriginal, Compra.class);
        CompraJSONRepository repositorio = new CompraJSONRepository(archivoJson.toString());
        repositorio.guardar(compraOriginal);
        String contenidoJson = Files.readString(archivoJson);
        assertFalse(contenidoJson.isBlank(), "El archivo JSON no debería estar vacío.");
        assertTrue(contenidoJson.contains("camila"), "El JSON debería contener el nombre del comprador.");
        List<Compra> comprasRecuperadas = repositorio.obtenerTodas();
        assertEquals(1, comprasRecuperadas.size(), "Debería recuperarse una sola compra.");
        Compra compraRecuperada = comprasRecuperadas.get(0);
        assertEquals(compraOriginal.getNombreComprador(), compraRecuperada.getNombreComprador());
        assertEquals(compraOriginal.getNombreEvento(), compraRecuperada.getNombreEvento());
        assertEquals(compraOriginal.getAsientos().size(), compraRecuperada.getAsientos().size());
        assertEquals(compraOriginal.getTotal(), compraRecuperada.getTotal(), 0.001);
    }
}