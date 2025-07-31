import java.io.IOException;
import java.util.List;

public class ProdutoDao {
    private GenericDao<Produto> dao = new GenericDao<>("produtos.dat");

    public void salvarTodos(List<Produto> lista) throws IOException {
        dao.salvar(lista);
    }

    public List<Produto> listarTodos() {
        return dao.listar();
    }
}
