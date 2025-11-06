package servlet;

/*
Autor: Mathew Lara
Fecha: 05/11/2025
Descripcion: Este servlet maneja peticiones GET para obtener y mostrar
             todas las cabeceras (headers) de la solicitud HTTP
             que envía el navegador del cliente.
*/

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

// La anotación @WebServlet mapea este servlet a la URL "/cabeceros-request"
// Cualquier solicitud a esa ruta será manejada por esta clase.
@WebServlet("/cabeceros-request")
public class CabecerasHttpRequestServlet extends HttpServlet {

    /**
     * Maneja las solicitudes HTTP por el metodo GET.
     *
     * @param req  El objeto HttpServletRequest que contiene la solicitud del cliente
     * (incluyendo las cabeceras).
     * @param resp El objeto HttpServletResponse que se usará para enviar la respuesta
     * al cliente.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Configurar el tipo de contenido de la respuesta
        // Le decimos al navegador que la respuesta es HTML con codificación UTF-8
        resp.setContentType("text/html;charset=UTF-8");

        // 2. Obtener información de la solicitud (request)
        String metodoHttp = req.getMethod(); // Obtiene el método (GET, POST, etc.)
        String requestURI = req.getRequestURI(); // Obtiene la URI (parte de la URL después del host)
        String requestURL = req.getRequestURL().toString(); // Obtiene la URL completa
        String contextPath = req.getContextPath(); // Obtiene la ruta de la aplicación (ej: /cabeceros)
        String servletPath = req.getServletPath(); // Obtiene la ruta del servlet (ej: /cabeceros-request)
        String ip = req.getRemoteAddr(); // Obtiene la dirección IP del cliente
        int port = req.getServerPort(); // Obtiene el puerto del servidor (ej: 8080)

        // 3. Obtener el objeto PrintWriter para escribir la respuesta HTML
        // Se usa try-with-resources para asegurar que 'out.close()' se llame automáticamente
        try (PrintWriter out = resp.getWriter()) {

            // 4. Generar la plantilla HTML de respuesta
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            // Título de la pestaña del navegador
            out.println("<title>Manejo de cabeceros 2025-2026</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Manejo de cabeceros</h1>");

            // 5. Imprimir la información de la solicitud en una lista
            out.println("<ul>");
            out.println("<li>Obteniendo el método de petición: " + metodoHttp + "</li>");
            out.println("<li>Obteniendo la URI: " + requestURI + "</li>"); // Corregí un typo "Obtniendo"
            out.println("<li>Obteniendo la URL: " + requestURL + "</li>");
            out.println("<li>Obteniendo el contexto: " + contextPath + "</li>");
            out.println("<li>Obteniendo el servlet: " + servletPath + "</li>"); // Corregí un typo "servlet;"
            out.println("<li>Obteniendo la ip: " + ip + "</li>");
            out.println("<li>Obteniendo el port: " + port + "</li>");

            // 6. Obtener y recorrer todas las cabeceras (headers)
            // getHeaderNames() devuelve una Enumeration con los nombres de todas las cabeceras
            Enumeration<String> headersNames = req.getHeaderNames();
            // Iterar mientras haya más cabeceras
            while (headersNames.hasMoreElements()) {
                // Obtener el nombre de la siguiente cabecera
                String cabecera = headersNames.nextElement();
                // Imprimir el nombre de la cabecera y su valor (obtenido con req.getHeader())
                out.println("<li>" + cabecera + " : " + req.getHeader(cabecera) + "</li>");
            }

            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
        } // El 'try-with-resources' cierra 'out' aquí automáticamente

    }
}