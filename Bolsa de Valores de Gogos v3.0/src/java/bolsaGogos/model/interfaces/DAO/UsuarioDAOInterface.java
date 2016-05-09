
package bolsaGogos.model.interfaces.DAO;

import bolsaGogos.model.Usuario;
import java.util.List;


/**
 * Interface que lista os métodos implementados pela classe UsuarioDAO.
 */
public interface UsuarioDAOInterface {

    public boolean removeTodosUsuarios();

    public Usuario getUsuario(int id);

    public List<Usuario> getListaUsuarios();

    public boolean insereUsuario(Usuario usuario);

    public boolean atualizaUsuario(Usuario usuario);

    public boolean removeUsuario(int id);

}
