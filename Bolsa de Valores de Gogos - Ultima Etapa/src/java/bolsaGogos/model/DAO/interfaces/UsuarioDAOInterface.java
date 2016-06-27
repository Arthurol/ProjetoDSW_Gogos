
package bolsaGogos.model.DAO.interfaces;

import bolsaGogos.model.Usuario;
import java.util.List;
import org.joda.time.DateTime;


/**
 * Interface que lista os métodos implementados pela classe UsuarioDAO.
 */
public interface UsuarioDAOInterface {

    public boolean removeTodosUsuarios();

    public Usuario getUsuarioId(int id);
    
    public Usuario getUsuarioEmail(String email);
    
    public boolean indicarLoginFalha(int idUsuario);
    
    public boolean indicarLoginSucesso(int idUsuario);
    
    public DateTime getDataUltimoLogin(int idUsuario);

    public List<Usuario> getListaUsuarios();

    public boolean insereUsuario(Usuario usuario);

    public boolean atualizaUsuario(Usuario usuario);
    
    public boolean atualizaSenha(int idUsuario, String senha);

    public boolean removeUsuario(int id);
    
    

}
