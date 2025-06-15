package es.ufv.dis.back.Examen2.DOL.controller;

import com.google.gson.Gson;
import es.ufv.dis.back.Examen2.DOL.model.Usuario;
import es.ufv.dis.back.Examen2.DOL.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private final Gson gson = new Gson();

    @GetMapping
    public List<Usuario> getAllUsuarios() throws IOException {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable String id) throws IOException {
        return usuarioService.getUsuarioById(id);
    }

    @PostMapping
    public void addUsuario(@RequestBody Usuario usuario) throws IOException {
        usuarioService.addUsuario(usuario);
    }

    @PutMapping("/{id}")
    public void updateUsuario(@PathVariable String id, @RequestBody Usuario usuario) throws IOException {
        usuarioService.updateUsuario(id, usuario);
    }

    @GetMapping("/generarPDF")
    public String generarPDF() throws IOException {
        return usuarioService.generarPDF();
    }
}