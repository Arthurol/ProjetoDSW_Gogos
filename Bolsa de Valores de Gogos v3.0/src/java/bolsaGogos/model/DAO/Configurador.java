package bolsaGogos.model.DAO;
import lombok.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *  Classe responsável por estabelecer a conexão com o banco, através da função conectar().
 *
 */
public @Data class Configurador {
    
    public Connection conectar() throws SQLException{
        Connection conexao;
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "dsw123");
            conexao.setCatalog("bolsadegogos");
            return conexao;

        } catch (SQLException e)
        {
            System.out.println("Não foi possível estabelecer uma conexão com o banco de dados - erro de SQL");
            System.out.println(e.getMessage());
            return null;
        } catch (ClassNotFoundException e)
        {
            System.out.println("Não foi possível estabelecer uma conexão com o banco de dados - driver não encontrado");
            return null;
        } catch (InstantiationException e)
        {
            System.out.println("Não foi possível estabelecer uma conexão com o banco de dados - erro de instanciação do driver");
            return null;
        } catch (IllegalAccessException e)
        {
            System.out.println("Não foi possível estabelecer uma conexão com o banco de dados - acesso ilegal no driver");
            return null;
        }
    
    
    }
   
}

