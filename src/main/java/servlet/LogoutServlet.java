package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.LoginService;
import service.LoginServiceImplement;

import java.io.IOException;
import java.util.Optional;

/*
 * autor: Mathew Lara
 * fecha y version: 10/11/2025 version: 1.0
 * descripcion: Servlet que maneja el cierre de sesión (logout).
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Usamos el Servicio para ver si hay una sesión activa
        LoginService auth = new LoginServiceImplement();
        Optional<String> username = auth.getUsername(req);

        // 2. Si hay un 'username' (es decir, si hay una sesión activa)
        if (username.isPresent()) {
            // 2a. Obtenemos la sesión actual
            HttpSession session = req.getSession();
            // 2b. Invalidamos la sesión
            // Esto destruye la sesión en el servidor y borra todos
            // los atributos guardados (username, sessionCounter, etc.).
            session.invalidate();
        }

        // 3. Redirigimos al usuario a la página de login
        resp.sendRedirect(req.getContextPath() + "/login.html");
    }
}
