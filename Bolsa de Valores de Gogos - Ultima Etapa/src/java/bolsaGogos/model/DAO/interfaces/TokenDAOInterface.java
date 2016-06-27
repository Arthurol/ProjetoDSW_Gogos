
package bolsaGogos.model.DAO.interfaces;

import bolsaGogos.model.Token;


/**
 * Interface que lista os métodos implementados pela classe TokenDAO.
 */
public interface TokenDAOInterface {
    
    public Token getToken(int id);
    
    public boolean insereToken(Token token);
    
    public boolean verificaTokenTrocaSenha(int idUsuario, String token, int maxHoras);
    
}
