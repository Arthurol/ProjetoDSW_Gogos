
package bolsaGogos.model.DAO.interfaces;

import bolsaGogos.model.LancamentoDinheiro;
import java.util.List;


/**
 * Interface que lista os métodos implementados pela classe LancamentoDinheiroDAO.
 */
public interface LancamentoDinheiroDAOInterface {
   
    public int calculaSaldoDinheiro(int idUsuario);
 
    public String consultaExtrato(int idUsuario);
    
    public List<LancamentoDinheiro> lancamentosDinheiroUsuario(int idUsuario);
}


