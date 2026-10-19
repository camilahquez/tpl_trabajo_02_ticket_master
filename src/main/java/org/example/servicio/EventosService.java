package org.example.servicio;

import org.example.modelo.Eventos;
import org.example.excepciones.EventoNoEncontradoException;

import java.util.List;
public class EventosService {

    private final List<Eventos> eventos;

    public EventosService(List<Eventos> eventos) {
        this.eventos = List.copyOf(eventos);
    }

    public List<Eventos> obtenerEventos() {
        return eventos;
    }
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
    public boolean existeEvento(String idEvento) {
        try {
            buscarEvento(idEvento);
            return true;
        }
        catch (EventoNoEncontradoException e) {
            return false;
        }
    }
}
