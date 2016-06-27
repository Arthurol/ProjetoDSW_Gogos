
package bolsaGogos.model.DAO.interfaces;

import bolsaGogos.model.LancamentoPersonagem;
import bolsaGogos.model.Personagem;
import java.util.List;

/**
 * Interface que lista os métodos implementados pela classe PersonagemDAO.
 */
public interface PersonagemDAOInterface {
 
    public Personagem getPersonagem(int id);
    
    public List<Personagem> getListaPersonagens();
    
    public int getIdByName(String nome);
     
    public boolean removePersonagem(int id);
          
}
