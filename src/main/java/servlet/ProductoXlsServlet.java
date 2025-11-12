package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
// Import necesario para la clase Cookie (aunque ya no se usa directamente)
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import service.LoginService;
import service.LoginServiceImplement;
import service.ProductoService;
import service.ProductoServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
// Import necesario para usar Arrays.stream() (aunque ya no se usa)
import java.util.Arrays;
import java.util.List;
// Import necesario para la clase Optional
import java.util.Optional;

/*
 * autor: Mathew Lara
 * fecha y version: 11/11/2025 version: 3.2 (Lógica de permisos)
 * descripcion: Servlet que muestra la lista de productos.
 * Responde en dos rutas: "/productos.html" y "/productos.xls".
 *
 * --- Lógica de Permisos (¡Nueva!) ---
 * - Utiliza LoginService para verificar si hay una sesión activa.
 * - Si hay sesión: Muestra saludo, enlaces y la tabla de productos completa.
 * - Si no hay sesión: Muestra *únicamente* el título "Listado de productos".
 */
@WebServlet({"/productos.xls", "/productos.html"})
public class ProductoXlsServlet extends HttpServlet {

    /*
     * Metodo GET: Maneja la solicitud para mostrar la lista de productos.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtener la lista de productos desde el servicio
        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listar(); //

        // 2. Obtener y verificar la sesión usando el Servicio
        LoginService auth = new LoginServiceImplement();
        Optional<String> usernameOptional = auth.getUsername(req); //
        // 'usernameOptional.isPresent()' será 'true' si el usuario inició sesión.

        // 3. Determinar el tipo de respuesta (HTML o Excel)
        resp.setContentType("text/html;charset=UTF-8");
        String servletPath = req.getServletPath();
        boolean esXls = servletPath.endsWith(".xls");

        // 4. Configurar cabeceras si la solicitud es para Excel
        if (esXls) {
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment; filename=productos.xls");
        }

        // 5. Escribir la respuesta HTML (o la tabla para Excel)
        try (PrintWriter out = resp.getWriter()) {

            // 5a. Si no es Excel, escribimos la cabecera HTML
            // (Esta parte se muestra siempre, con o sin sesión)
            if (!esXls) {
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Listado de Productos</title>");
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + req.getContextPath() + "/css/estilos.css\">"); //
                out.println("</head>");
                out.println("<body>");
                // Este título es lo único que verá un usuario no logueado
                out.println("<h1>Listado de productos</h1>");
            }

            // 6. LÓGICA DE PERMISOS PRINCIPAL
            // Preguntamos si el usuario ha iniciado sesión.
            // Todel contenido sensible va dentro de este .
            if (usernameOptional.isPresent()) {

                // 6a. SÍ HAY SESIÓN: Mostramos el saludo y los enlaces
                // (Solo si es la versión HTML)
                if (!esXls) {
                    out.println("<h1>Hola <span class=\"username\">" + usernameOptional.get() + "</span></h1>"); //
                    out.println("<p><a href=\"" + req.getContextPath() + "/productos.xls" + "\">exportar a excel</a></p>");
                    out.println("<p><a href=\"" + req.getContextPath() + "/productojson" + "\">mostrar json</a></p>");
                }

                // 6b. SÍ HAY SESIÓN: Mostramos la tabla de productos
                // (Se muestra tanto en HTML como en Excel)
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>id</th>");
                out.println("<th>nombre</th>");
                out.println("<th>tipo</th>");
                out.println("<th>precio</th>");
                out.println("</tr>");

                // 6c. Recorremos los productos
                productos.forEach(p -> { //
                    out.println("<tr>");
                    out.println("<td>" + p.getId() + "</td>"); //
                    out.println("<td>" + p.getNombre() + "</td>"); //
                    out.println("<td>" + p.getTipo() + "</td>"); //
                    // Como este bloque solo se ejecuta si hay sesión,
                    // podemos imprimir el precio directamente, sin otro 'if'.
                    out.println("<td>" + p.getPrecio() + "</td>"); //
                    out.println("</tr>");
                }); // <-- El bucle forEach termina aquí

                // 6d. Cerramos la tabla (CORREGIDO: va *después* del bucle)
                out.println("</table>");

            }
            // 7. Si NO HAY SESIÓN (else):
            // No hacemos nada. El código simplemente continúa
            // y solo se habrá mostrado el <h1> del paso 5a.

            // 8. Cierre de etiquetas HTML
            // (Esta parte se ejecuta siempre, con o sin sesión)
            if (!esXls) {
                out.println("</body>");
                out.println("</html>");
            }
        }
    }
}