package org.example.archivos;
import org.example.modelo.Compras;
import java.util.List;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CompraJSONRepository implements GuardableEnRepositorio {
    private String archivo;

    public CompraJSONRepository(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public void guardar(Compras compra) {
        // guardar compra en JSON
        List<Compras> compras = obtenerTodas();
        compras.add(compra);
        try(FileWriter writer = new FileWriter(archivo)){
            gson.toJson(compras, writer);

        }
    }

    @Override
    public Compras buscarPorId(int id) {
        // buscar compra en JSON
        return null;
    }

    @Override
    public List<Compras> obtenerTodas() {
        // leer todas las compras
        return null;
    }

}
