package org.example.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Eventos {
    private static final DateTimeFormatter formatoFecha=
            DateTimeFormatter.ofPattern( "d/M/yyyy" );
    private static final DateTimeFormatter formatoHora=
            DateTimeFormatter.ofPattern( "H:mm" );

    private final String  idEvento;
    private final String nombreEvento;
    private final LocalDate fechaEvento;
    private final LocalTime horaEvento;
    private final String lugarEvento;
    private final String tipoEvento;

    public Eventos(String idEvento, String nombreEvento, LocalDate fechaEvento, LocalTime horaEvento, String lugarEvento, String tipoEvento) {
        this.idEvento = idEvento;
        this.nombreEvento = nombreEvento;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
        this.tipoEvento = tipoEvento;
    }

    public String getIdEvento() {return idEvento;}
    public String getNombreEvento() {return nombreEvento;}
    public LocalDate getFechaEvento() {return fechaEvento;}
    public LocalTime getHoraEvento() {return horaEvento;}
    public String getLugarEvento() {return lugarEvento;}
    public String getTipoEvento() {return tipoEvento;}

    @Override
    public String toString() {
        return "Datos sobre el evento{" +
                "\n Id del Evento: " + getIdEvento() +
                "\n Nombre del Evento: " + getNombreEvento() +
                "\n Fecha y Hora del Evento: " + getFechaEvento().format(formatoFecha) + ", a las " + getHoraEvento().format(formatoHora) +
                "\n Lugar del Evento: " + getLugarEvento() +
                "\n Tipo de Evento: " + getTipoEvento() +
                "\n}";

    }
}

