import Controllers.LivroController;
import Interfaces.LivroRepository;
import Repositories.LivroRepositoryMemoria;
import Services.LivroService;

public class Main {
    public static void main(String[] args) {
        LivroRepository repository = new LivroRepositoryMemoria();
        LivroService service = new LivroService(repository);
        LivroController controller = new LivroController(service);

        controller.executar();
    }
}