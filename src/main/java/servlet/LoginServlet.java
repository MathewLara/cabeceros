package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.LoginService;
import service.LoginServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

/*
 * autor: Mathew Lara
 * fecha y version: 11/11/2025 version: 2.0
 * descripcion: Servlet que maneja el inicio de sesión.
 * - Toda la lógica de sesión se maneja con HttpSession (username y contador).
 * - doGet: Verifica si existe una sesión activa (usando LoginService).
 * - Si existe: Muestra bienvenida y el contador de inicios de sesión.
 * - Si no existe: Muestra el formulario login.jsp.
 *
 * - doPost: Valida las credenciales.
 * - Si son correctas: Crea la sesión, guarda "username" y actualiza
 * el "sessionCounter" dentro de la sesión.
 * - Si son incorrectas: Envía error 401.
 */
@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {

    // --- Credenciales de acceso (constantes) ---
    final static String USERNAME = "admin";
    final static String PASSWORD = "12345";

    /*
     * Metodo GET: Maneja la navegación a la página de login.
     * Su objetivo es mostrar el formulario o la bienvenida si ya hay sesión.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Usamos el Servicio para ver si ya hay un "username" en la sesión
        LoginService auth = new LoginServiceImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        // 2. Lógica de decisión: ¿Existe el username en la sesión?
        if (usernameOptional.isPresent()) {
            // 2a. Sí hay sesión: El usuario ya está logueado.

            // Obtenemos la sesión (sabemos que existe)
            HttpSession session = req.getSession();

            // Leemos el contador de la sesión
            Integer counter = (Integer) session.getAttribute("sessionCounter");
            if (counter == null) {
                counter = 0; // Seguridad por si algo falla
            }

            // Lógica simple para "vez" o "veces"
            String veces = (counter == 1) ? "vez" : "veces";

            // Mostramos la página de bienvenida
            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Hola " + usernameOptional.get() + "</title>");
                // Enlazamos la hoja de estilos CSS
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + req.getContextPath() + "/css/estilos.css\">");
                out.println("</head>");
                out.println("<body>");
                // Saludamos y mostramos el contador de la sesión
                out.println("<h1>Bienvenido a mi aplicacion <span class=\"username\">" + usernameOptional.get() + "</span>. Has iniciado sesión " + counter + " " + veces + "!</h1>");
                out.println("<p><a href='" + req.getContextPath() + "/logout'>Cerrar sesión</a></p>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            // 2b. No hay sesión: El usuario no está logueado.
            // Mostramos el formulario .jsp para que inicie sesión.
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    /*
     * Metodo POST: Maneja el envío del formulario de login.
     * Su objetivo es validar las credenciales.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtener los parámetros (datos) enviados desde el formulario
        String username = req.getParameter("user");
        String password = req.getParameter("password");

        // 2. Lógica de validación
        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            // 2a. Si son correctas (Login exitoso)

            // 3. Crear la sesión y guardar el username
            HttpSession session = req.getSession();
            session.setAttribute("username", username);

            // 4. LÓGICA DEL CONTADOR
            int counter = 0; // Inicia el contador en 0

            // 5. Leer todas las cookies que envía el navegador
            Cookie[] cookies = req.getCookies() != null ? req.getCookies() : new Cookie[0];

            // 6. Buscar la cookie "loginCounter"
            Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                    .filter(c -> "loginCounter".equals(c.getName()))
                    .findAny();

            // 7. Si la cookie existe, leemos su valor
            if (cookieOptional.isPresent()) {
                try {
                    // Convertimos el valor de la cookie (String) a un número (int)
                    String counterStr = cookieOptional.get().getValue();
                    counter = Integer.parseInt(counterStr);
                } catch (NumberFormatException e) {
                    // Si el valor no es un número, lo reseteamos a 0
                    counter = 0;
                }
            }

            // 8. Incrementamos el contador (sea 0 o el valor que traía)
            counter++;

            // 9. Creamos la nueva cookie (o la actualizamos) con el valor incrementado
            Cookie loginCookie = new Cookie("loginCounter", Integer.toString(counter));
            loginCookie.setMaxAge(60 * 60 * 24 * 365); // Hacemos que dure 1 año
            resp.addCookie(loginCookie); // La enviamos al navegador

            // 10. Guardamos el contador en la SESIÓN SÓLO para esta sesión actual
            // (Así el 'doGet' puede leerlo fácilmente sin procesar cookies)
            session.setAttribute("sessionCounter", counter);

            // 11. Redirigimos a la página de bienvenida (donde está el 'doGet')
            resp.sendRedirect(req.getContextPath() + "/login.html");

        } else {
            // 2b. Si no son correctas (Login fallido)
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos no tiene acceso o revise las credenciales");
        }
    }
}