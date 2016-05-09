package bolsaGogos.model.DAO;

import bolsaGogos.model.interfaces.DAO.CasamentoDeOfertaDAOInterface;
import bolsaGogos.model.CasamentoDeOferta;
import bolsaGogos.model.DAO.Configurador;
import bolsaGogos.model.Oferta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Classe responsável pela conexão ao banco e interações com a tabela
 * Casamentosoferta.
 *
 */
public class CasamentoDeOfertaDAO implements CasamentoDeOfertaDAOInterface {

    Configurador config;
    Oferta oferta;
    Oferta ofertaVenda;

    public CasamentoDeOfertaDAO() {
        config = new Configurador();
    }
    
    /**
    * Instancia e retorna um CasamentoDeOferta a partir dos dados de entrada, obtidos através de uma consulta ao banco
    * 
    */
    public CasamentoDeOferta montaCasamento(ResultSet rs) throws SQLException {

        DateTime datetime = new DateTime(rs.getDate("data"));
        
        CasamentoDeOferta casamento = new CasamentoDeOferta();
        casamento.setIdOfertaCompra(rs.getInt("idOfertaCompra"));
        casamento.setIdOfertaVenda(rs.getInt("idOfertaVenda"));
        casamento.setDataExecucaoCasamento(datetime);

        return casamento;
    }
    
    /**
    * Recupera do banco todas as entradas da tabela casamentosoferta e as salva em uma lista. Essa lista é retornada ao fim do método.
    *
    */
    @Override
    public List<CasamentoDeOferta> getListaCasamento() {

        List<CasamentoDeOferta> listaCasamento = new ArrayList<CasamentoDeOferta>();

        try (Connection conn = config.conectar()) {
            if (config == null) {
                return null;
            }

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM casamentosoferta");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaCasamento.add(montaCasamento(rs));
            }
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return listaCasamento;

    }
    
    /**
    * Recupera do banco os dados referentes ao id de entrada e com eles monta e retorna um objeto CasamentoDeOferta.
    *
    */
    @Override
    public CasamentoDeOferta getCasamentosPorId(int id) {

        CasamentoDeOferta casamento = null;

        try (Connection conn = config.conectar()) {
            if (conn == null) {
                return null;
            }
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM casamentosoferta WHERE id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                casamento = montaCasamento(rs);
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return casamento;
    }
    
}
