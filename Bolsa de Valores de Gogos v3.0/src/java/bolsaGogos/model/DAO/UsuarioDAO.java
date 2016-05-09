package bolsaGogos.model.DAO;

import bolsaGogos.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import bolsaGogos.model.interfaces.DAO.UsuarioDAOInterface;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Classe responsável pela conexão ao banco e interações com a tabela Usuarios.
 * 
 */
public class UsuarioDAO implements UsuarioDAOInterface{
    // Objeto Configurador utilizado para realizar a conexão ao banco
    public Configurador config;
    
    public UsuarioDAO(){ 
        config = new Configurador();
    }
    
    /**
    * Instancia e retorna um Usuario a partir dos dados de entrada, obtidos através de uma consulta ao banco
    * 
    */
    public Usuario montaUsuario(ResultSet rs) throws SQLException{
        
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        
        return usuario;
    }
 
    /**
    *
    * Faz consulta ao banco e retorna um Usuario de acordo com o id informado
    */    
    @Override
    public Usuario getUsuario(int id){
        
        Usuario usuario = null;
        
        try (Connection conn = config.conectar()){
            if (conn == null){
                return null;
            }
       
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            ps.setInt(1, id); 
            ResultSet rs = ps.executeQuery();
                
            if(rs.next()){
                usuario = montaUsuario(rs);
            }
            conn.close();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return usuario;
    }
    
    /**
    *
    * Através de consulta ao banco, recupera a lista que contém todos os usuarios da tabela Usuario
    */
    @Override
    public List<Usuario> getListaUsuarios(){
        
        List<Usuario> listaUsuarios = new ArrayList<Usuario>();
        
         try(Connection conn = config.conectar()){
            if (config == null){
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuarios"); 
            ResultSet rs = ps.executeQuery();
                
            while(rs.next()){
                listaUsuarios.add(montaUsuario(rs));
            }
            conn.close();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return listaUsuarios;
    }
    
    /**
    *
    * Objeto Usuario é inserido no banco através de uma Stored Procedure
     * @param usuario
    */
    @Override
    public boolean insereUsuario(Usuario usuario){
        
        try(Connection conn = config.conectar()){
            CallableStatement cs = conn.prepareCall("{call InserirUsuario(?,?,?,?,?)}");
            cs.setString(1, usuario.getNome());
            cs.setString(2, usuario.getTelefone());
            cs.setString(3, usuario.getCpf());
            cs.setString(4, usuario.getEmail());
            cs.setString(5, usuario.getSenha());
            cs.execute();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true; 
    }
    
    /**
    *
    * Usuario é alterado no banco através de uma Stored Procedure
     * @param usuario
     *  
    */
    @Override
    public boolean atualizaUsuario(Usuario usuario){
        
        try(Connection conn = config.conectar()){
            if (conn == null){
                return false;
            }
            CallableStatement cs = conn.prepareCall("{call EditarUsuario(?,?,?,?,?)}");
            cs.setInt(1, usuario.getId());
            cs.setString(2, usuario.getNome());
            cs.setString(3, usuario.getTelefone());
            cs.setString(4, usuario.getCpf());
            cs.setBlob(5, usuario.getFoto());
            cs.execute();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
    * Executa a Query que remove o usuário correspondente ao id de entrada do método. 
    * 
    */
    @Override
    public boolean removeUsuario(int id){
        
         try(Connection conn = config.conectar()){
            if (conn == null){
                return false;
            }
            
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Usuarios WHERE id=?"); 
            ps.setInt(1, id);
            ps.executeQuery();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
         return true;
    }
    
    /**
    * Através da Stored Procedure RemoverUsuarios() deleta todas as entradas da tabela usuarios
    * 
    */
    @Override
    public boolean removeTodosUsuarios(){
        
         try(Connection conn = config.conectar()){
            if (conn == null){
                return false;
            }
          
            CallableStatement cs = conn.prepareCall("{call RemoverUsuarios()}");
            cs.executeQuery();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
         return true;
    }
    
}
