
package bolsaGogos.model.interfaces.DAO;

import bolsaGogos.model.LancamentoPersonagem;
import java.util.List;


/**
 * Interface que lista os métodos implementados pela classe LancamentoPersonagemDAO.
 */
public interface LancamentoPersonagemDAOInterface {
   
    public boolean debitaPersonagem(LancamentoPersonagem lancamento);
    
    public boolean creditaPersonagem(LancamentoPersonagem lancamento);
    
    public String consultaExtrato(List<LancamentoPersonagem> lancamentos);
    
    public int calculaSaldoPersonagens(int idUsuario, int idPersonagem);
    
    public List<LancamentoPersonagem> lancamentosPorUsuario(int idUsuario);

}
