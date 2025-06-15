package es.ufv.dis.back.Examen2.DOL.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import es.ufv.dis.back.Examen2.DOL.model.Usuario;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final String jsonFile = "usuarios.json";
    private final Gson gson = new Gson();

    public List<Usuario> getAllUsuarios() throws IOException {
        Reader reader = new FileReader(jsonFile);
        Type listType = new TypeToken<List<Usuario>>() {
        }.getType();
        return gson.fromJson(reader, listType);
    }

    public Usuario getUsuarioById(String id) throws IOException {
        List<Usuario> usuarios = getAllUsuarios();
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addUsuario(Usuario usuario) throws IOException {
        List<Usuario> usuarios = getAllUsuarios();
        usuario.setId(UUID.randomUUID().toString());
        usuarios.add(usuario);
        saveUsuarios(usuarios);
    }

    public void updateUsuario(String id, Usuario usuario) throws IOException {
        List<Usuario> usuarios = getAllUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(id)) {
                usuario.setId(id);
                usuarios.set(i, usuario);
                break;
            }
        }
        saveUsuarios(usuarios);
    }

    private void saveUsuarios(List<Usuario> usuarios) throws IOException {
        try (Writer writer = new FileWriter(jsonFile)) {
            gson.toJson(usuarios, writer);
        }
    }

    public String generarPDF() {
        try {
            Document doc = new Document(PageSize.A4, 50, 50, 100, 72);
            PdfWriter.getInstance(doc, new FileOutputStream("info.pdf"));
            doc.open();

            // Leer usuarios
            List<Usuario> usuarios = getAllUsuarios();

            // Título
            doc.add(new Paragraph("Listado de Usuarios"));
            doc.add(new Paragraph(" ")); // Salto de línea

            for (Usuario user : usuarios) {
                doc.add(new Paragraph("ID: " + user.getId()));
                doc.add(new Paragraph("Nombre: " + user.getNombre() + " " + user.getApellidos()));
                doc.add(new Paragraph("NIF: " + user.getNif()));
                doc.add(new Paragraph("Email: " + user.getEmail()));
                doc.add(new Paragraph("Dirección: " +
                        user.getDireccion().getCalle() + ", " +
                        user.getDireccion().getNumero() + ", " +
                        user.getDireccion().getCodigoPostal() + ", " +
                        user.getDireccion().getPisoLetra() + ", " +
                        user.getDireccion().getCiudad()));
                doc.add(new Paragraph("Método de Pago: " +
                        user.getMetodoPago().getNumeroTarjeta() + " (" +
                        user.getMetodoPago().getNombreAsociado() + ")"));
                doc.add(new Paragraph(" ")); // Salto de línea
            }

            doc.close();
            return "PDF generado correctamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al generar el PDF.";
        }
    }
}