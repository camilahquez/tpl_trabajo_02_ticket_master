package org.example.repositorios;
import org.example.modelo.Compra;
import java.util.List;


public interface GuardableEnRepositorio {
    void guardar(Compra compra);

    Compra buscarPorId(String id);

    List<Compra> obtenerTodas();
}
