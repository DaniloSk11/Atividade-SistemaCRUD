package Repositories;

import Entities.Livro;
import Interfaces.LivroRepository;
import java.util.*;

public class LivroRepositoryMemoria implements LivroRepository {
    private final Map<Integer, Livro> livros = new HashMap<>();

    @Override
    public Livro salvar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        if (buscarPorIsbn(livro.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Já existe um livro com o ISBN: " + livro.getIsbn());
        }

        livros.put(livro.getId(), livro);
        return livro;
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(livros.values());
    }

    @Override
    public Optional<Livro> buscarPorId(int id) {
        return Optional.ofNullable(livros.get(id));
    }

    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return Optional.empty();
        }

        String isbnLimpo = isbn.replaceAll("[^0-9X]", "");

        return livros.values().stream()
                .filter(livro -> {
                    String livroIsbnLimpo = livro.getIsbn().replaceAll("[^0-9X]", "");
                    return livroIsbnLimpo.equals(isbnLimpo);
                })
                .findFirst();
    }

    @Override
    public boolean atualizar(Livro livro) {
        if (livro == null) {
            return false;
        }

        if (!livros.containsKey(livro.getId())) {
            return false;
        }

        Optional<Livro> livroComMesmoIsbn = buscarPorIsbn(livro.getIsbn());
        if (livroComMesmoIsbn.isPresent() && livroComMesmoIsbn.get().getId() != livro.getId()) {
            throw new IllegalArgumentException("Já existe outro livro com o ISBN: " + livro.getIsbn());
        }

        livros.put(livro.getId(), livro);
        return true;
    }

    @Override
    public boolean deletar(int id) {
        return livros.remove(id) != null;
    }

    @Override
    public int contar() {
        return livros.size();
    }
}