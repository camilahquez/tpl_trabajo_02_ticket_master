package org.example.servicios;
import org.example.excepciones.EventoNoEncontradoException;
import org.example.modelo.Asiento;
import org.example.modelo.Eventos;


import java.util.List;
//con esta clase se verifica si los datos del comprador son validos, tambien verifica que no se haga una compra sin asientos y que estsos esten disponibles para la venta. verifica la existencia del eventop.
public class ValidadorCompraService {
    private final AsientoService asientoService;
    private final  EventosService eventosService;
    public ValidadorCompraService(AsientoService asientoService, EventosService eventosService){
        this.asientoService = asientoService;
        this.eventosService = eventosService;
    }
//busca si el vetnto proporcionado si existe.
    public  boolean validarEvento(String nombreEvento) throws EventoNoEncontradoException {
     Eventos eventos = eventosService.buscarEventoPorNombre(nombreEvento);
        return eventosService.existeEvento(eventos.getIdEvento());
    }

// se asegura que sea un correo valido
    public  boolean verificarCorreo(String correroComprador){
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return correroComprador != null && correroComprador.matches(regex);
    }
    //revisa si los asientos estan disponibles, en caso de enviarle una lista vacia o que algun aisentos esten reservados devuelve false
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
    //verifica que la cedula sea un valor valido.
    public  boolean verificarCedula(String documentoCompardor){
        return documentoCompardor != null && !documentoCompardor.isBlank() && documentoCompardor.matches("\\d{6,10}");
    }
    //verifica que el nombre sea un valor valido.
    public  boolean verificarNombre(String nombreComprador){
        return nombreComprador != null && !nombreComprador.isBlank() && nombreComprador.matches( "[\\p{L}]+([ '-][\\p{L}]+)*");
    }
    //retorna false si un asiento esta ocupado y true si esta disponible.
    public boolean validarDisponibilidad(String idEvento, String codigoAsiento) {
        Asiento asiento = asientoService.buscarAsiento(idEvento, codigoAsiento);
        return asiento != null && !asiento.isOcupado();
    }

}
