package Entities;

import java.time.Year;
import java.util.Objects;

public class Livro {
    private static int contadorId = 1;
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int anoPublicacao;
    private boolean disponivel;

    public Livro(String titulo, String autor, String isbn, int anoPublicacao) {
        this.id = contadorId++;
        setTitulo(titulo);
        setAutor(autor);
        setIsbn(isbn);
        setAnoPublicacao(anoPublicacao);
        this.disponivel = true;
    }

    public Livro(int id, String titulo, String autor, String isbn, int anoPublicacao, boolean disponivel) {
        this.id = id;
        setTitulo(titulo);
        setAutor(autor);
        setIsbn(isbn);
        setAnoPublicacao(anoPublicacao);
        this.disponivel = disponivel;
        if (id >= contadorId) {
            contadorId = id + 1;
        }
    }

    public static int getContadorId() {
        return contadorId;
    }

    public static void setContadorId(int contadorId) {
        Livro.contadorId = contadorId;
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
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
        }
        if (titulo.trim().length() < 2) {
            throw new IllegalArgumentException("Título deve ter pelo menos 2 caracteres");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("Autor não pode ser nulo ou vazio");
        }
        if (autor.trim().length() < 2) {
            throw new IllegalArgumentException("Nome do autor deve ter pelo menos 2 caracteres");
        }
        this.autor = autor.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio");
        }
        String isbnLimpo = isbn.replaceAll("[^0-9X]", "");
        if (isbnLimpo.length() != 10 && isbnLimpo.length() != 13) {
            throw new IllegalArgumentException("ISBN deve ter 10 ou 13 dígitos");
        }
        this.isbn = isbn.trim();
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        int anoAtual = Year.now().getValue();
        if (anoPublicacao < 1400 || anoPublicacao > anoAtual) {
            throw new IllegalArgumentException("Ano de publicação deve estar entre 1400 e " + anoAtual);
        }
        this.anoPublicacao = anoPublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getIsbnFormatado() {
        if (isbn == null) {
            return null;
        }

        String isbnLimpo = isbn.replaceAll("[^0-9X]", "");

        if (isbnLimpo.length() == 10) {
            return isbnLimpo.substring(0, 1) + "-" +
                    isbnLimpo.substring(1, 4) + "-" +
                    isbnLimpo.substring(4, 9) + "-" +
                    isbnLimpo.substring(9);
        } else if (isbnLimpo.length() == 13) {
            return isbnLimpo.substring(0, 3) + "-" +
                    isbnLimpo.substring(3, 4) + "-" +
                    isbnLimpo.substring(4, 6) + "-" +
                    isbnLimpo.substring(6, 12) + "-" +
                    isbnLimpo.substring(12);
        }

        return isbn;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", isbn='" + getIsbnFormatado() + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", disponivel=" + disponivel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return id == livro.id &&
                anoPublicacao == livro.anoPublicacao &&
                disponivel == livro.disponivel &&
                Objects.equals(titulo, livro.titulo) &&
                Objects.equals(autor, livro.autor) &&
                Objects.equals(isbn, livro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, autor, isbn, anoPublicacao, disponivel);
    }
}