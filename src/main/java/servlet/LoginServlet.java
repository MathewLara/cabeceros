package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import static java.lang.System.out;
/*
 * autor: Mathew Lara
 * fecha y version: 10/11/2025 version: 1.0
 * descripcion: Servlet que maneja el inicio de sesión de usuario.
 * Responde a las rutas "/login" y "/login.html".
 * - doGet: Verifica si el usuario ya inició sesión (revisando la cookie).
 * - Si ya tiene cookie: Muestra un mensaje de "ya estás logueado".
 * - Si no tiene cookie: Muestra el formulario JSP para iniciar sesión.
 * - doPost: Procesa los datos del formulario de login.
 * - Si el login es correcto: Crea una cookie "username" y redirige al inicio.
 * - Si el login es incorrecto: Envía un error 401.
 */
@WebServlet({"/login","/login.html"})
public class LoginServlet extends HttpServlet {

    // 1. Credenciales de acceso
    // Se definen como 'final static' para que sean constantes de la clase.
    final static String USERNAME = "admin";
    final static String PASSWORD = "12345";

    /*
     * Metodo que maneja las solicitudes HTTP GET.
     * Se activa cuando el usuario navega a /login.html
     * Su objetivo es verificar si el usuario ya tiene una sesión activa.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 2. Obtener las cookies que envía el navegador
        // req.getCookies() puede devolver 'null' si no hay cookies.
        // Este ternario lo protege: si es 'null', crea un array vacío para evitar errores.
        Cookie[] cookies = req.getCookies() != null ? req.getCookies() : new Cookie[0];

        // 3. Buscar la cookie específica de "username"
        // Usamos un Stream de Java 8 para procesar la lista de cookies.
        Optional<String> cookieOptional = Arrays.stream(cookies)
                // Filtramos para encontrar solo la cookie llamada "username"
                .filter(c -> "username".equals(c.getName()))
                // Convertimos la Cookie (objeto) a su valor (String)
                .map(Cookie::getValue)
                // Tomamos la primera que encuentre
                .findAny();
        // 'cookieOptional' es un 'Optional', un contenedor que puede tener
        // un valor (si se encontró la cookie) o estar vacío

        // 4. Lógica de decisión
        if (cookieOptional.isPresent()) {
            // Si encontramos la cookie (Usuario ya logueado)
            // Mostramos un mensaje de bienvenida, no el formulario.
            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                resp.setContentType("text/html;charset=UTF-8");
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Login" + cookieOptional.get() + "</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Login</h1>");
                out.println("Bienvenido al sistema " + cookieOptional.get() + " has iniciado sesion");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            // No encontramos la cookie (Usuario no logueado)
            // Redirigimos al JSP que contiene el formulario HTML.
            // El usuario no ve cambiar la URL, solo ve el formulario.
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    /*
     * Metodo que maneja las solicitudes HTTP POST.
     * Se activa cuando el usuario wnvia el formulario desde /login.jsp.
     * Su objetivo es verificar las credenciales.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 5. Obtener los parámetros (datos) enviados desde el formulario
        String username = req.getParameter("user");
        String password = req.getParameter("password");

        // 6. Lógica de validación
        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            //Si son correctas(Login exitoso)

            // Configuramos la respuesta
            resp.setContentType("text/html;charset=UTF-8");

            // Creamos la cookie "username" con el valor del usuario logueado
            Cookie cookie = new Cookie("username", username);

            // Añadimos la cookie a la respuesta. El navegador la guardará.
            resp.addCookie(cookie);

            // Generamos una página de exito
            try (PrintWriter out = resp.getWriter()) {
                resp.setContentType("text/html;charset=UTF-8");
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Login correcto</title>");
                // Enlazamos la hoja de estilos CSS
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + req.getContextPath() + "/css/estilos.css\">");
                out.println("</head>");
                out.println("<body>");
                // Usamos la clase CSS "username" para el estilo
                out.println("<h1>Bienvenido a mi aplicacion <span class=\"username\">" + username + "</span> Sesion con exito!</h1>");
                out.println("<a href='" + req.getContextPath() + "/index.html'>Volver al inicio</a>");
                out.println("</body>");
                out.println("</html>");
            }

            // 7. Redirigir al usuario al index
            // Esta línea es la que realmente ve el usuario.
            // Un sendRedirect() le dice al navegador "Ve a esta otra página".
            // Esto anula el HTML que se escribió en el 'try-with-resources' de arriba.
            resp.sendRedirect(req.getContextPath() + "/index.html");

        } else {
            //Si no son correctas (Login fallido)
            // Enviamos un error HTTP 401 (No Autorizado) con un mensaje.
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos no tiene acceso o revise las credenciales");
        }
    }
}