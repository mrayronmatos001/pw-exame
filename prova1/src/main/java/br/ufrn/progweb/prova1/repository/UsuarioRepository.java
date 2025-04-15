package br.ufrn.progweb.prova1.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.ufrn.progweb.prova1.model.Usuario;

public class UsuarioRepository {
    private static List<Usuario> usuarios = new ArrayList<>();

    public static Optional<Usuario> encontrarPorEmail(String email) {
        return usuarios.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }

    public static void salvar(Usuario usuario) {
        usuarios.add(usuario);
    }

    public static List<Usuario> listarTodos() {
        return usuarios;
    }
}