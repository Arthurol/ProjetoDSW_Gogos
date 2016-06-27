
package bolsaGogos.model.DAO.interfaces;

import bolsaGogos.model.LancamentoPersonagem;
import java.util.List;


/**
 * Interface que lista os métodos implementados pela classe LancamentoPersonagemDAO.
 */
public interface LancamentoPersonagemDAOInterface {
   
    public int debitaPersonagem(LancamentoPersonagem lancamento);
    
    public boolean creditaPersonagem(LancamentoPersonagem lancamento);
    
    public String consultaExtrato(int idUsuario);
    
    public int calculaSaldoPersonagem(int idUsuario, int idPersonagem);
    
    
    public List<LancamentoPersonagem> lancamentosPersonagemUsuario(int idUsuario);

}
