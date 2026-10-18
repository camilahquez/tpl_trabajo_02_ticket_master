package org.example.repositorios;
import org.example.modelo.Asiento;
import org.example.modelo.Compra;
import java.lang.reflect.Type;
import java.util.List;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.excepciones.ErrorRepositorioCompraException;
import com.google.gson.JsonParseException;
import java.util.ArrayList;

public class CompraJSONRepository implements GuardableEnRepositorio {
    private String archivo;
    private Gson gson;

    public CompraJSONRepository(String archivo) {
        this.archivo = archivo;
        this.gson = new Gson();
    }

    @Override
    public void guardar(Compra compra) {
        // guardar compra en JSON
        List<Compra> compras = obtenerTodas();
        compras.add(compra);
        try(FileWriter writer = new FileWriter(archivo)){
            gson.toJson(compra, writer);
        }catch (IOException e) {
            throw new ErrorRepositorioCompraException(
                    "No se pudo guardar la compra en el archivo JSON.", e
            );
        }
    }

    @Override
    public Compra buscarPorId(String id) {
        List<Compra> compras = obtenerTodas();
        for (Compra compra : compras){
            if (compra.getId() == id) {
                return compra;
            }
        }
        return null;
    }

    @Override
    public List<Compra> obtenerTodas() {
        // debuelve todas las compras guardadas en el archvo de compras

        try (FileReader reader = new FileReader(archivo)) {
            File archivoCompras = new File(archivo);

            if (!archivoCompras.exists()) {
                return new ArrayList<>();
            }
            Type tipoLista = new TypeToken<List<Compra>>() {}.getType();
            List<Compra> compras = gson.fromJson(reader, tipoLista);
            if (compras != null){
                return  compras;
            }
        }catch (IOException | JsonParseException e) {
            throw new ErrorRepositorioCompraException(
                    "No se pudieron leer las compras del archivo JSON.", e
            );
        }
        return null;
    }


}
