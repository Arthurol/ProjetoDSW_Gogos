package bolsaGogos.model.DAO;

import bolsaGogos.model.DAO.interfaces.PersonagemDAOInterface;
import bolsaGogos.model.Personagem;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela conexão ao banco e interações com a tabela Personagens.
 * 
 */
public class PersonagemDAO implements PersonagemDAOInterface {

    public Configurador config;

    public PersonagemDAO() {
        config = new Configurador();
    }

    /**
    * Instancia e retorna um Personagem a partir dos dados de entrada, obtidos através de uma consulta ao banco
    * 
    */
    public Personagem montaPersonagem(ResultSet rs) throws SQLException {
        Personagem personagem = new Personagem();
        personagem.setId(rs.getInt("id"));
        personagem.setNome(rs.getString("nome"));

        return personagem;
    }

    /**
     * Faz consulta ao banco e retorna um Personagem de acordo com o id informado
     */
    @Override
    public Personagem getPersonagem(int id) {
        Personagem personagem = null;
        Connection conn = null;
        try {
            conn = config.conectar();
            if (conn == null) {
                return null;
            }

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM personagens WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                personagem = montaPersonagem(rs);
            }
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return personagem;
    }

    /**
     * Através de consulta ao banco, recupera a lista que contém todos os personagems da tabela Personagem
     */
    @Override
    public List<Personagem> getListaPersonagens() {
        List<Personagem> listaPersonagems = new ArrayList<Personagem>();

        try {
            Connection conn = config.conectar();
            if (config == null) {
                return null;
            }

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM personagens");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaPersonagems.add(montaPersonagem(rs));
            }
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaPersonagems;
    }
    
    /**
     * Faz consulta ao banco e retorna um id de Personagem de acordo com o nome informado
     */
    @Override
    public int getIdByName(String nome) {
        int idPersonagem = -1;
        Connection conn = null;
        try {
            conn = config.conectar();
            if (conn == null) {
                return -1;
            }

            PreparedStatement ps = conn.prepareStatement("SELECT id FROM personagens WHERE nome = ?");
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idPersonagem = rs.getInt("id");
            }
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return idPersonagem;
    }


    /**
     * Remove a entrada da tabela personagens correspondente ao id de entrada
     */
    @Override
    public boolean removePersonagem(int id) {

        try (Connection conn = config.conectar()) {
            if (conn == null) {
                return false;
            }

            PreparedStatement ps = conn.prepareStatement("DELETE FROM Personagens WHERE id=?");
            ps.setInt(1, id);
            ps.executeQuery();
            conn.close();   
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

}
