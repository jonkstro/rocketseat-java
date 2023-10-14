package br.com.jonas.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.jonas.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private UserRepository repository;

    // BARRAR REQUISIÇÃO OU PERMITIR A REQUISIÇÃO
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        // Se a rota for igual a tasks:
        if (servletPath.startsWith("/tasks/")) {
            // Pegar usuário e senha
            var authorization = request.getHeader("Authorization");
            var passB64 = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(passB64);
            var authString = new String(authDecode);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            // Validar se usuário existe
            var user = this.repository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
            } else {
                // Valida a senha
                var passVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passVerify.verified == true) {
                    // Segue viagem
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            // Segue viagem
            filterChain.doFilter(request, response);
        }

    }

}
