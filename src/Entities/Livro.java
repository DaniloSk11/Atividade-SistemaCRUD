package Entities;

import java.time.Year;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Livro {
    private static int contadorid = 1;
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int anoPublicacao;
    private boolean disponivel;

    public static int getContadorid() {
        return contadorid;
    }

    public static void setContadorid(int contadorid) {
        Livro.contadorid = contadorid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo != null && (titulo.trim().length() >= 3)){
                this.titulo = titulo;
        }



    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor != null && (autor.trim().length() >= 3)){
            this.autor = autor;
        }
    }

    public String getIsbn() {
        return isbn;
    }


    public void setIsbn(String isbn) {
        Random r = new Random();
        String alph = "0123456789abcdefghijklmnopqrsuvwxyz";
        for (int i = 0; i <= 13; i++) {
            
        }
        this.isbn = isbn;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        if (anoPublicacao >= 1500 && anoPublicacao <= Year.now().getValue())
            this.anoPublicacao = anoPublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Livro(int id, String titulo, String autor, String isbn, int anoPublicacao, boolean disponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", isbn='" + isbn + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", disponivel=" + disponivel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return id == livro.id && anoPublicacao == livro.anoPublicacao && disponivel == livro.disponivel && Objects.equals(titulo, livro.titulo) && Objects.equals(autor, livro.autor) && Objects.equals(isbn, livro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, autor, isbn, anoPublicacao, disponivel);
    }
}
