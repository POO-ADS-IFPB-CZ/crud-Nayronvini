import java.io.*;
import java.util.*;

public class GenericDao<T extends Serializable> {

    private final String arquivo;

    public GenericDao(String arquivo) {
        this.arquivo = arquivo;
    }

    public void salvar(List<T> lista) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            out.writeObject(lista);
        }
    }

    public List<T> listar() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
