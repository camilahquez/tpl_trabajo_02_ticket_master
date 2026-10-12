package org.example.archivos;
import org.example.modelo.Compras;
import java.util.List;

public interface GuardableEnRepositorio {
    void guardar(Compras compra);

    Compras buscarPorId(int id);

    List<Compras> obtenerTodas();
}
