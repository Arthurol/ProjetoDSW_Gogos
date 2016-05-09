package bolsaGogos.model.interfaces.DAO;

import bolsaGogos.model.CasamentoDeOferta;
import java.util.List;
import org.joda.time.DateTime;


/**
 * Interface que lista os métodos implementados pela classe CasamentoDeOfertaDAO.
 */
public interface CasamentoDeOfertaDAOInterface {
    
   public List<CasamentoDeOferta> getListaCasamento();
   
   public CasamentoDeOferta getCasamentosPorId(int id);
   
}
