package bolsaGogos.model.DAO;

import bolsaGogos.model.Token;
import bolsaGogos.model.DAO.interfaces.TokenDAOInterface;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.joda.time.DateTime;

/**
* Classe responsável pela conexão ao banco e interações com a tabela Tokens.
* 
*/
public class TokenDAO implements TokenDAOInterface{
    Configurador config;
    
    TokenDAO(){ 
           config = new Configurador();
    }
    
    /**
    * Instancia e retorna um Token a partir dos dados de entrada, obtidos através de uma consulta ao banco
    * 
    */
    private Token montaToken(ResultSet rs) throws SQLException{
        Token token = new Token();
        DateTime validade = new DateTime(rs.getDate("validade"));
        token.setId(rs.getInt("id"));
        token.setIdUsuario(rs.getInt("idUsuario"));
        token.setToken(rs.getString("token"));
        token.setDataValidade(validade);
        return token;
    }
    
    /**
    * Retorna um objeto Token composto dos dados obtidos do banco
    */
    @Override 
    public Token getToken(int id){
        Token token = null;
        try{
            Connection conn = config.conectar();
            if (config == null){
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tokens WHERE id = ?");
            ps.setInt(1, id); 
            ResultSet rs = ps.executeQuery();
                
            if(rs.next()){
                token = montaToken(rs);
            }
            conn.close();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return token;
    }
    
    /**
    * Cria uma entrada na tabela Tokens contendo os dados obtidos do objeto Token
    */
    @Override
    public boolean insereToken(Token token){
        
       try (Connection conn = config.conectar()){
            if (conn == null){
                return false;
            }
        
            CallableStatement cs = conn.prepareCall("{call InserirToken(?,?)}");
            cs.setInt(1, token.getIdUsuario());
            cs.setString(2, token.getToken());    
            cs.execute();
            
            conn.close();
            return true;
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        } 
    }
    
    /**
    * Checa se o token informado está dentro da data de validade
    */
    @Override
    public boolean verificaTokenTrocaSenha(int idUsuario, String token, int maxHoras){
        String SQL = "select token, validade, NOW() " +
                        "from tokens " + 
                            "where idUsuario = ? " + 
                                "order by validade desc";

        try (Connection conn = config.conectar()){
                PreparedStatement ps = conn.prepareStatement(SQL);
                ps.setInt(1, idUsuario);

                ResultSet rs = ps.executeQuery();

                if (rs.next()){
                    String tokenBanco = rs.getString(1);
                    DateTime dataToken = new DateTime(rs.getTimestamp(2));
                    DateTime dataAgora = new DateTime(rs.getTimestamp(3));

                    long horas = ( dataToken.getMillis() - dataAgora.getMillis()) / (60 * 60 * 1000);

                    if (horas > maxHoras)
                            return false;

                    if (token.compareTo(tokenBanco) != 0)
                            return false;
                }

                conn.close();

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
