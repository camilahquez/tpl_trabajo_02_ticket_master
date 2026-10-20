package org.example.servicios;
import org.example.excepciones.EventoNoEncontradoException;
import org.example.modelo.Asiento;
import org.example.modelo.Eventos;


import java.util.List;

public class ValidadorCompraService {
    private final AsientoService asientoService;
    private final  EventosService eventosService;
    public ValidadorCompraService(AsientoService asientoService, EventosService eventosService){
        this.asientoService = asientoService;
        this.eventosService = eventosService;
    }

    public  boolean validarEvento(String nombreEvento) throws EventoNoEncontradoException {
     Eventos eventos = eventosService.buscarEventoPorNombre(nombreEvento);
        return eventosService.existeEvento(eventos.getIdEvento());
    }


    public  boolean verificarCorreo(String correroComprador){
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return correroComprador != null && correroComprador.matches(regex);
    }
    public  boolean verificarSeleccionAsientos(List<Asiento> asientosSelecionados){
        if (asientosSelecionados.size() != 0){
           int contador = 0;
         for (Asiento asiento : asientosSelecionados){
             String idEvento = asiento.getIdEvento();
             String codigoAsiento = asiento.getCodigoAsiento();
             if (validarDisponibilidad(idEvento, codigoAsiento)){
               contador += 1;
              }
         }
         if (contador == asientosSelecionados.size()){
             return true;
         }
         else{
             return false;
         }
        }
        return false;
    }
    public  boolean verificarCedula(String documentoCompardor){
        return documentoCompardor != null && !documentoCompardor.isBlank() && documentoCompardor.matches("\\d{6}|\\d{8}|\\d{10}");
    }
    public  boolean verificarNombre(String nombreComprador){
        return nombreComprador != null && !nombreComprador.isBlank() && nombreComprador.matches( "[\\p{L}]+([ '-][\\p{L}]+)*");
    }
    public boolean validarDisponibilidad(String idEvento, String codigoAsiento) {
        Asiento asiento = asientoService.buscarAsiento(idEvento, codigoAsiento);
        return asiento != null && !asiento.isOcupado();
    }

}
