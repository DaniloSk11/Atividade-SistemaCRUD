package Services;

import Entities.Livro;
import Interfaces.LivroRepository;
import java.util.List;
import java.util.Optional;

public class LivroService {
    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public Livro cadastrarLivro(String titulo, String autor, String isbn, int anoPublicacao) {
        try {
            Livro livro = new Livro(titulo, autor, isbn, anoPublicacao);
            return repository.salvar(livro);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Erro ao cadastrar livro: " + e.getMessage(), e);
        }
    }

    public List<Livro> listarLivros() {
        return repository.listarTodos();
    }

    public Optional<Livro> buscarLivroPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        return repository.buscarPorId(id);
    }

    public Optional<Livro> buscarLivroPorIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio");
        }
        return repository.buscarPorIsbn(isbn);
    }

    public boolean atualizarLivro(int id, String titulo, String autor, String isbn, int anoPublicacao) {
        Optional<Livro> livroExistente = repository.buscarPorId(id);
        if (livroExistente.isEmpty()) {
            return false;
        }

        try {
            Livro livro = livroExistente.get();
            boolean disponivel = livro.isDisponivel();

            Livro livroAtualizado = new Livro(id, titulo, autor, isbn, anoPublicacao, disponivel);
            return repository.atualizar(livroAtualizado);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

    public boolean removerLivro(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }

        Optional<Livro> livro = repository.buscarPorId(id);
        if (livro.isEmpty()) {
            return false;
        }

        return repository.deletar(id);
    }

    public boolean marcarComoIndisponivel(int id) {
        Optional<Livro> livroOpt = repository.buscarPorId(id);
        if (livroOpt.isEmpty()) {
            return false;
        }

        Livro livro = livroOpt.get();
        if (!livro.isDisponivel()) {
            throw new RuntimeException("Livro já está marcado como indisponível");
        }

        livro.setDisponivel(false);
        return repository.atualizar(livro);
    }

    public boolean marcarComoDisponivel(int id) {
        Optional<Livro> livroOpt = repository.buscarPorId(id);
        if (livroOpt.isEmpty()) {
            return false;
        }

        Livro livro = livroOpt.get();
        if (livro.isDisponivel()) {
            throw new RuntimeException("Livro já está marcado como disponível");
        }

        livro.setDisponivel(true);
        return repository.atualizar(livro);
    }

    public int contarLivros() {
        return repository.contar();
    }

    public List<Livro> listarLivrosDisponiveis() {
        return repository.listarTodos().stream()
                .filter(Livro::isDisponivel)
                .toList();
    }

    public List<Livro> listarLivrosIndisponiveis() {
        return repository.listarTodos().stream()
                .filter(livro -> !livro.isDisponivel())
                .toList();
    }
}