package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession; // Import clave
import java.util.Optional;

/*
 * autor: Mathew Lara
 * fecha y version: 11/11/2025 version: 2.0
 * descripcion: Implementación de la interfaz LoginService.
 * Esta clase centraliza la lógica de cómo obtener la sesión.
 * Su única responsabilidad es leer de la HttpSession.
 */
public class LoginServiceImplement implements LoginService {

    /*
     * Metodo que obtiene el "username" desde la HttpSession.
     * Abstrae la lógica para que los Servlets no tengan que
     * interactuar directamente con la sesión, solo con este servicio.
     */
    @Override
    public Optional<String> getUsername(HttpServletRequest request) {

        // 1. Obtiene la sesión actual asociada a la solicitud.
        HttpSession session = request.getSession();

        // 2. Lee el atributo "username" que guardamos durante el login
        String username = (String) session.getAttribute("username");

        // 3. Comprueba si el nombre existe (no es null)
        if (username != null) {
            // 3a. Si se encontró, lo envolvemos en un Optional y lo devolvemos
            return Optional.of(username);
        }

        // 3b. Si no se encontró (username es null), devolvemos un Optional vacío
        return Optional.empty();
    }
}