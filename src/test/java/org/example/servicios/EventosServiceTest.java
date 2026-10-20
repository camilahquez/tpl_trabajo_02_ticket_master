package org.example.servicios;

import org.example.excepciones.EventoNoEncontradoException;
import org.example.modelo.Eventos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventosServiceTest {
    private EventosService eventosService;

    @BeforeEach
    void crearServicioConDosEventos() {
        Eventos rock = new Eventos("EV001", "Rock Medellin 2026",
                LocalDate.of(2026, 10, 20), LocalTime.of(19, 0),
                "Estadio Atanasio Girardot", "Concierto",null);
        Eventos teatro = new Eventos("EV003", "Noche de Teatro",
                LocalDate.of(2026, 12, 3), LocalTime.of(20, 0),
                "Teatro Metropolitano", "Teatro",null);
        eventosService = new EventosService(List.of(rock, teatro));
    }
    @Test
    void buscarEventoExistenteDevuelveElEvento() throws EventoNoEncontradoException {
        Eventos evento = eventosService.buscarEvento("EV001");

        assertEquals("EV001", evento.getIdEvento());
        assertEquals("Rock Medellin 2026", evento.getNombreEvento());
    }
    @Test
    void buscarEventoInexistenteLanzaExcepcion() {
        assertThrows(EventoNoEncontradoException.class,
                () -> eventosService.buscarEvento("EV999"));
    }
    @Test
    void existeEventoConNullDevuelveFalse() {
        assertFalse(eventosService.existeEvento(null));
    }
}
