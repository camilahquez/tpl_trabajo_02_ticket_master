package org.example;

import org.example.repositorios.EventosCSVRepository;
import org.example.excepciones.ArchivoDatosException;
import org.example.excepciones.EventoNoEncontradoException;
import org.example.modelo.Eventos;
import org.example.servicios.EventosService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Cargar los eventos desde el CSV
            EventosCSVRepository repositorio = new EventosCSVRepository("data/eventos.csv");
            List<Eventos> eventos = repositorio.cargarEventos();

            // 2. Crear el servicio con la lista ya cargada
            EventosService eventoService = new EventosService(eventos);

            // 3. Listar todos los eventos
            System.out.println("=== Eventos disponibles ===");
            for (Eventos e : eventoService.obtenerEventos()) {
                System.out.println(e);
            }

            // 4. Buscar un evento que existe (cambia el id por uno de tu CSV)
            System.out.println("=== Búsqueda de un evento existente ===");
            System.out.println(eventoService.buscarEvento("EV001"));

            // 5. Buscar uno que no existe: debe lanzar la excepción
            System.out.println("=== Búsqueda de un evento inexistente ===");
            eventoService.buscarEvento("EV999");

        } catch (ArchivoDatosException e) {
            System.out.println("Error de archivo: " + e.getMessage());
        } catch (EventoNoEncontradoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }
    }
}