package org.example.repositorios.;
import org.example.modelo.Compra;
import java.lang.reflect.Type;
import java.util.List;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


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
        }catch (Exception e){
            throw new RuntimeException("Error al leer las compras", e);
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
            Type tipoLista = new TypeToken<List<Compra>>() {}.getType();
            List<Compra> compras = gson.fromJson(reader, tipoLista);
            if (compras != null){
                return  compras;
            }
        }catch (Exception e){
            throw new RuntimeException("Error al leer las compras", e);
        }
        return null;
    }

}
