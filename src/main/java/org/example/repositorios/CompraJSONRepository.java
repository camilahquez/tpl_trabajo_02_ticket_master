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
    public Compras buscarPorId(String id) {
        List<Compras> compras = obtenerTodas();
        for (Compras compra : compras){
            if compra.getId() == id {
                return compra
            }
        }
        return null;
    }

    @Override
    public List<Compras> obtenerTodas() {
        // debuelve todas las compras guardadas en el archvo de compras
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<List<Compras>>() {}.getType();
            List<Compras> compras = gson.fromJson(reader, tipoLista);
            if compras =! null{
                return  compras
            }
        }
        return null;
    }

}
