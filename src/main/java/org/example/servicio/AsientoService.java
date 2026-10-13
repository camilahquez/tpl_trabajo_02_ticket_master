package org.example.servicio;

import org.example.modelo.Asiento;
import org.example.archivos.AsientoCSVRepository;
import org.example.excepciones.AsientoNoDisponibleException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AsientoService {
    private List<Asiento> asientos;
    private AsientoCSVRepository csvRepository;

    public AsientoService(String rutaCsv) {
        csvRepository = new AsientoCSVRepository(rutaCsv);
        try {
            this.asientos = csvRepository.cargarAsientos();
        } catch (IOException e) {
            System.err.println("Error al cargar los asientos desde el archivo: " + e.getMessage());
            this.asientos = new ArrayList<>();
        }
    }

    public List<Asiento> obtenerAsientosPorEvento(String idEvento) {
        return asientos.stream()
                .filter(a -> a.getIdEvento().equalsIgnoreCase(idEvento))
                .collect(Collectors.toList());
    }

    public Asiento buscarAsiento(String idEvento, String codigoAsiento) {
        return asientos.stream()
                .filter(a -> a.getIdEvento().equalsIgnoreCase(idEvento) 
                          && a.getCodigoAsiento().equalsIgnoreCase(codigoAsiento))
                .findFirst()
                .orElse(null);
    }

    public void reservarAsiento(String idEvento, String codigoAsiento) throws AsientoNoDisponibleException {
        Asiento asiento = buscarAsiento(idEvento, codigoAsiento);
        if (asiento == null) {
            throw new AsientoNoDisponibleException("El asiento " + codigoAsiento + " no existe para el evento " + idEvento);
        }
        if (asiento.isOcupado()) {
            throw new AsientoNoDisponibleException("El asiento " + codigoAsiento + " ya se encuentra vendido.");
        }
        asiento.setOcupado(true);
    }

    public double calcularPorcentajeOcupacion(String idEvento) {
        List<Asiento> asientosEvento = obtenerAsientosPorEvento(idEvento);
        if (asientosEvento.isEmpty()) return 0.0;

        long ocupados = asientosEvento.stream().filter(Asiento::isOcupado).count();
        return ((double) ocupados / asientosEvento.size()) * 100.0;
    }
}