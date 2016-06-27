package bolsaGogos.model.DAO;

import bolsaGogos.model.DAO.interfaces.OfertaDAOInterface;
import bolsaGogos.model.Oferta;
import bolsaGogos.model.StatusDaOferta;
import bolsaGogos.model.TipoDeOferta;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Classe responsável pela conexão ao banco e interações com a tabela Ofertas.
 * 
 */
public class OfertaDAO implements OfertaDAOInterface {
    Configurador config;

    public OfertaDAO(){ 
        config = new Configurador();
    }
    
    /**
    * Instancia e retorna uma Oferta a partir dos dados de entrada, obtidos através de uma consulta ao banco
    * 
    */
    private Oferta montaOferta(ResultSet rs) throws SQLException
    {
        //tipo e status são iniciados com valores quaisquer(compra e aberta) apenas para evitar excessões de ponteiro null
        TipoDeOferta tipo = TipoDeOferta.compra; 
        StatusDaOferta status = StatusDaOferta.aberta;
        
        //metodo get que atribui a tipo o real Enum correspondente ao tipo/status do registro recuperado do banco
        tipo = tipo.get(rs.getInt("tipo"));
        status = status.get(rs.getInt("status"));
        
        Oferta oferta = new Oferta();
        oferta.setId(rs.getInt("id"));
        oferta.setIdOfertaOriginal(rs.getInt("idOrdemOriginal"));
        oferta.setIdPersonagem(rs.getInt("idPersonagem"));
        oferta.setPrecoUnitario(rs.getDouble("precoUnitario"));
        oferta.setQuantidade(rs.getInt("quantidade"));
        oferta.setQuantidadeOriginal(rs.getInt("quantidadeOriginal"));
        oferta.setIdUsuario(rs.getInt("idUsuario"));
        oferta.setData(new DateTime (rs.getTimestamp("data")));
        oferta.setTipoOferta(tipo);
        oferta.setStatusOferta(status);
        
        return oferta;
    }
    
    /**
     * Junta todas as entradas da tabela ofertas em uma lista e a retorna
     */
    @Override       
    public List<Oferta> getListaOfertas() {
        
        List<Oferta> lista = new ArrayList<Oferta>();
        
        try (Connection conn = config.conectar()){
        if (conn == null) {
            return null;
        }
        
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM ofertas");
        ResultSet rs = ps.executeQuery();
            
        while (rs.next()) {
            lista.add(montaOferta(rs));
        }
        
        conn.close();
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return lista;
    }
    
    /**
     * Recupera Oferta do Banco de acordo com o id informado     
     */
    @Override
    public Oferta getOferta(int id) {
        
        Oferta oferta = null;
        try (Connection conn = config.conectar()){
            if (config == null){
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ofertas WHERE id = ?");
            ps.setInt(1, id); 
            ResultSet rs = ps.executeQuery();
                
            if(rs.next()){
                oferta = montaOferta(rs);
            }
            conn.close();   
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return oferta;
        
    }

    /**
     * Cancela Oferta do Banco de acordo com o id informado     
     */
    @Override
    public boolean removeOferta(int id) {
        
         try (Connection conn = config.conectar()){
            if (conn == null){
                return false;
            }
            Oferta offer = DAOFactory.getOfertaDAO().getOferta(id);
            if (offer.getTipoOferta() == TipoDeOferta.compra) {
                
                CallableStatement cs = conn.prepareCall("{call CancelaOrdemCompra(?)}");
                cs.setInt(1, offer.getId());
                cs.execute();
                
            conn.close();
            return true;    
            }
            else if (offer.getTipoOferta() == TipoDeOferta.venda) {
                CallableStatement cs = conn.prepareCall("{call CancelaOrdemVenda(?)}");
                cs.setInt(1, offer.getId());
                cs.execute(); 
                conn.close();
                return true;
            }
        
         
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
         return true;
    }

    /**
     * Insere oferta na tabela do banco, checando se ela é de compra e chamando a SP adequada
     */
    @Override
    public int insereOfertaCompra(Oferta oferta) {
        
        int erro = 0;
        try (Connection conn = config.conectar()){
            if (conn == null){
                return -1;
            }
            
            CallableStatement cs = conn.prepareCall("{call RegistraOrdemCompra(?,?,?,?,?)}");
          
            cs.setInt(1, oferta.getIdUsuario());
            cs.setInt(2, oferta.getIdPersonagem()); 
            cs.setInt(3, oferta.getQuantidade());
            cs.setFloat(4, (float)oferta.getPrecoUnitario());
            cs.registerOutParameter(5, java.sql.Types.INTEGER);
            cs.execute();
            
            erro = cs.getInt(5);
            
            conn.close();   
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return -1;
        } 
        return erro;
        
    }
    /**
     * Insere oferta na tabela do banco, checando se ela é de venda e chamando a SP adequada
     */
    @Override
    public int insereOfertaVenda(Oferta oferta) {
        
        int erro = 0;
        try (Connection conn = config.conectar()){
            if (conn == null){
                return -1;
            }
            
            CallableStatement cs = conn.prepareCall("{call RegistraOrdemVenda(?,?,?,?,?)}");
            
            cs.setInt(1, oferta.getIdUsuario());
            cs.setInt(2, oferta.getIdPersonagem());
            cs.setInt(3, oferta.getQuantidade());
            cs.setDouble(4, (double)oferta.getPrecoUnitario()); 
            cs.registerOutParameter(5, java.sql.Types.INTEGER);
            cs.execute();
            
            erro = cs.getInt(5);
            
            conn.close();                
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return -1;
        }  
        return erro;
    }

    /**
     * Recupera a oferta do banco, de acordo com o id de Personagem informado
     */
    @Override
    public Oferta getByIdPersonagem(int idPersonagem) {
        
        Oferta oferta = null;
        try (Connection conn = config.conectar()) {
            if (config == null){
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ofertas WHERE idPersonagem = ?");
            ps.setInt(1, idPersonagem); 
            ResultSet rs = ps.executeQuery();
                
            if(rs.next()){
                oferta = montaOferta(rs);
            }
            conn.close();   
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return oferta;
        
    }

    /**
    * Recupera a oferta do banco, de acordo com o id de Usuario informado
    */
  @Override
    public List<Oferta> getByIdUsuario(int idUsuario) {
        
        List<Oferta> ofertas = new ArrayList<>();
        try (Connection conn = config.conectar()) {
            if (config == null){
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ofertas WHERE idUsuario = ?");
            ps.setInt(1, idUsuario); 
            ResultSet rs = ps.executeQuery();
                
            while (rs.next()) {
            ofertas.add(montaOferta(rs));
        }
            conn.close();   
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return ofertas;
    }
    
    /**
     * Recupera a oferta do banco, de acordo com o id de oferta informado
     */
    @Override
    public Oferta getByIdOferta(int idOferta){
        
        Oferta oferta = null;
        try (Connection conn = config.conectar()) {
            if (config == null){
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ofertas WHERE idOferta = ?");
            ps.setInt(1, idOferta); 
            ResultSet rs = ps.executeQuery();
                
            if(rs.next()){
                oferta = montaOferta(rs);
            }
            conn.close();   
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return oferta;
    }
}
