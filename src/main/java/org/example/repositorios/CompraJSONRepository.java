package org.example.repositorios;

import org.example.modelo.Compra;
import java.lang.reflect.Type;
import java.util.List;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.excepciones.ErrorRepositorioCompraException;
import com.google.gson.JsonParseException;
import java.util.ArrayList;

/**
 * Lee y guarda las Compras en el archivo JSON
 */
public class CompraJSONRepository implements GuardableEnRepositorio {
    private final String archivo;
    private final Gson gson;

    /**
     * Crea el repositorio con la ruta del rutaArchivo JSON.
     * @param rutaArchivo ruta del archivo
     */
    public CompraJSONRepository(String rutaArchivo) {
        this.archivo = rutaArchivo;
        this.gson = new Gson();
    }

    /**
     * Agrega una compra al archivo, conservando las que ya guardadas.
     * @param compra la compra que se va a guardar
     * @throws ErrorRepositorioCompraException por si no se puede escribir dentro del archivo
     */
    @Override
    public void guardar(Compra compra) {
        List<Compra> compras = obtenerTodas();
        compras.add(compra);
        try(FileWriter writer = new FileWriter(archivo)){
            gson.toJson(compras, writer);
        }catch (IOException e) {
            throw new ErrorRepositorioCompraException(
                    "No se pudo guardar la compra en el archivo JSON.", e
            );
        }
    }

    /**
     * Busca una compra por su id
     * @param id el id de la compra
     * @return la compra encontrada o null si no se encuentra
     */
    @Override
    public Compra buscarPorId(String id) {
        List<Compra> compras = obtenerTodas();
        for (Compra compra : compras){
            if (compra.getId().equals(id))
                return compra;
        }
        return null;
    }

    /** Lee todas la compras actuales dentro del archivo JSON
     * @throws ErrorRepositorioCompraException por si no se puede leer el archivo JSON
     * @return la lista con todas las compras guardadas en el JSON o lista vacia
     */
    @Override
    public List<Compra> obtenerTodas() {
        File archivoCompras = new File(archivo);
        if (!archivoCompras.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<List<Compra>>() {}.getType();
            List<Compra> compras = gson.fromJson(reader, tipoLista);

            if (compras != null) {
                return compras;
            }
            return new ArrayList<>();

        }catch (IOException | JsonParseException e) {
            throw new ErrorRepositorioCompraException(
                    "No se pudo leer el archivo compras.JSON.", e
            );
        }
    }


}
