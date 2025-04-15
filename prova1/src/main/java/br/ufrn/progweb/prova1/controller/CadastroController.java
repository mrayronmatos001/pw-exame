package br.ufrn.progweb.prova1.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.ufrn.progweb.prova1.model.Usuario;
import br.ufrn.progweb.prova1.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CadastroController {

    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
    public void mostrarFormularioCadastro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String erro = request.getParameter("erro");

        String html = "<html>" +
            "<head><title>Cadastro</title></head>" +
            "<body>" +
            "<h1>Cadastro de Usuário</h1>";

        if (erro != null) {
            html += "<p style='color:red;'>" + erro + "</p>";
        }

        html += "<form method='post' action='/cadastro'>" +
            "Nome: <input type='text' name='nome' required><br>" +
            "Email: <input type='email' name='email' required><br>" +
            "<button type='submit'>Cadastrar</button>" +
            "</form>" +
            "</body>" +
            "</html>";

        response.setContentType("text/html");
        response.getWriter().write(html);
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public void processarCadastro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");

        // Validação: email já existe
        if (UsuarioRepository.encontrarPorEmail(email).isPresent()) {
            response.sendRedirect("/cadastro?erro=Email já cadastrado");
            return;
        }

        // Validação simples de email
        if (!email.contains("@")) {
            response.sendRedirect("/cadastro?erro=Email inválido");
            return;
        }

        // Cria e salva o usuário
        Usuario novoUsuario = new Usuario(nome, email);
        UsuarioRepository.salvar(novoUsuario);

        // Cria a sessão
        HttpSession session = request.getSession();
        session.setAttribute("usuario", novoUsuario);

        // Redireciona para a dashboard
        response.sendRedirect("/dashboard");
    }
    
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public void mostrarDashboard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); // Não cria nova sessão

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("/cadastro");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String html = "<html>" +
            "<head><title>Dashboard</title></head>" +
            "<body>" +
            "<h1>Bem-vindo, " + usuario.getNome() + "!</h1>" +
            "<p>Email: " + usuario.getEmail() + "</p>" +
            "</body>" +
            "</html>";

        response.setContentType("text/html");
        response.getWriter().write(html);
    }
}
