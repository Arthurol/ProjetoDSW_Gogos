
package bolsaGogos.model.interfaces.DAO;

import bolsaGogos.model.Oferta;
import java.util.List;

/**
 * Interface que lista os métodos implementados pela classe OfertaDAO.
 */
public interface OfertaDAOInterface {
    
    public Oferta getOferta(int id);
    
    public List<Oferta> getListaOfertas();
    
    public boolean removeOferta(int id);
    
    public int insereOfertaCompra(Oferta oferta);
    
    public int insereOfertaVenda(Oferta oferta);

    public Oferta getByIdPersonagem(int idPersonagem);
    
    public Oferta getByIdOferta(int idOferta);
    
    public Oferta getByIdUsuario(int idUsuario);
    
}
