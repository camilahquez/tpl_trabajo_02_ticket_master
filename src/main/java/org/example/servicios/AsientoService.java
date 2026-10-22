package org.example.servicios;

import org.example.modelo.Asiento;
import org.example.excepciones.AsientoNoDisponibleException;
import org.example.repositorios.AsientoCSVRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
// Asientos services se encarga de la gestion de los asientos de cada evento. Nos permite la busqueda, reserva, y el guardado de los aisentos
public class AsientoService {
    private List<Asiento> asientos;
    private AsientoCSVRepository csvRepository;

// inicializa el repositorio que se encarga del guardado de los asientos. En caso de error en la carga muestra un mesnaje de error y genera una lista.
    public AsientoService(String rutaCsv) {
        csvRepository = new AsientoCSVRepository(rutaCsv);
        try {
            this.asientos = csvRepository.cargarAsientos();
        } catch (IOException e) {
            System.err.println("Error al cargar los asientos desde el archivo: " + e.getMessage());
            this.asientos = new ArrayList<>();
        }
    }
//nos perimete traer todos lo asientos que pertenezcan a un evento en especifico
    public List<Asiento> obtenerAsientosPorEvento(String idEvento) {
        return asientos.stream()
                .filter(a -> a.getIdEvento().equalsIgnoreCase(idEvento))
                .collect(Collectors.toList());
    }
//hace la busqueda de un asiento en por medio de su codigo y el nombre del evento al que pertenece
    public Asiento buscarAsiento(String idEvento, String codigoAsiento) {
        return asientos.stream()
                .filter(a -> a.getIdEvento().equalsIgnoreCase(idEvento) 
                          && a.getCodigoAsiento().equalsIgnoreCase(codigoAsiento))
                .findFirst()
                .orElse(null);
    }
// Cambia el estado de un asiento pasando el atributo ocupado a true y en caso de que ya sea true nos informa que el asiento ya se encuentra reservado

    public void reservarAsiento(String idEvento, String codigoAsiento)
            throws AsientoNoDisponibleException {
        Asiento asiento = buscarAsiento(idEvento, codigoAsiento);

        if (asiento == null) {
            throw new AsientoNoDisponibleException("El asiento " + codigoAsiento + " no existe para el evento " + idEvento);
        }
        if (asiento.isOcupado()) {
            throw new AsientoNoDisponibleException("El asiento " + codigoAsiento + " ya se encuentra vendido.");
        }
        asiento.setOcupado(true);
        try {
            csvRepository.guardarAsientos(asientos);
        } catch (IOException e) {
            asiento.setOcupado(false);
            throw new IllegalStateException("No se pudo guardar el estado de los asientos.", e);
        }
    }

    public double calcularPorcentajeOcupacion(String idEvento) {
        List<Asiento> asientosEvento = obtenerAsientosPorEvento(idEvento);
        if (asientosEvento.isEmpty()) return 0.0;

        long ocupados = asientosEvento.stream().filter(Asiento::isOcupado).count();
        return ((double) ocupados / asientosEvento.size()) * 100.0;
    }
}