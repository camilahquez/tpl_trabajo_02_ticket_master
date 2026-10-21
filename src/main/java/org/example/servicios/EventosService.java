package org.example.servicios;

import org.example.modelo.Eventos;
import org.example.excepciones.EventoNoEncontradoException;
import org.example.repositorios.EventosCSVRepository;

import java.util.List;
//Gestiona la consulta y búsqueda de los eventos registrados, permitiendo obtener la lista de eventos, buscar eventos por identificador o nombre, verificar su existencia y consultar la ruta de su imagen.
public class EventosService {

    private final List<Eventos> eventos;

    //Inicializa el servicio con una lista de eventos y crea una copia inmodificable de dicha lista para evitar que se modifique directamente desde el exterior.
    public EventosService(List<Eventos> eventos) {
        this.eventos = List.copyOf(eventos);
    }

    //Obtiene y devuelve la lista de eventos registrados en el servicio.
    public List<Eventos> obtenerEventos() {
        return eventos;
    }

    //Busca un evento mediante su identificador, ignorando espacios al inicio y al final y diferencias entre mayúsculas y minúsculas. Devuelve el evento encontrado o lanza una excepción EventoNoEncontradoException si el identificador es nulo o no existe un evento que coincida.
    public Eventos buscarEvento(String idEvento) throws EventoNoEncontradoException {
        if (idEvento !=null) {
            for (Eventos e: eventos) {
                if (e.getIdEvento().equalsIgnoreCase(idEvento.trim())) {
                    return e;
                }
            }
        }
        throw new EventoNoEncontradoException(
                "No existe el evento con id: " + idEvento);
    }
//Verifica si existe un evento con el identificador proporcionado. Devuelve true si el evento se encuentra registrado o false si no existe.
    public boolean existeEvento(String idEvento) {
        try {
            buscarEvento(idEvento);
            return true;
        }
        catch (EventoNoEncontradoException e) {
            return false;
        }
    }
    //Busca un evento por su nombre, ignorando espacios al inicio y al final y diferencias entre mayúsculas y minúsculas. Devuelve el evento encontrado o lanza una excepción EventoNoEncontradoException si el nombre es nulo o no corresponde a ningún evento registrado.
    public Eventos buscarEventoPorNombre(String nombreEvento) throws EventoNoEncontradoException {
        if (nombreEvento != null) {
            for (Eventos evento : eventos) {
                if (evento.getNombreEvento().equalsIgnoreCase(nombreEvento.trim())) {
                    return evento;
                }
            }
        }
        throw new EventoNoEncontradoException("No existe el evento con nombre: " + nombreEvento);
    }
    //Busca un evento por su nombre y devuelve la ruta de la imagen asociada a dicho evento. Si no encuentra el evento, lanza una excepción EventoNoEncontradoException.
    public String buscarRutaImagen(String nombreEvento) throws EventoNoEncontradoException {
        Eventos evento = buscarEventoPorNombre(nombreEvento);
        return evento.getRutaImagen();
    }
}
