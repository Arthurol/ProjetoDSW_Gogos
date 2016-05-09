package bolsaGogos.model.DAO;

import bolsaGogos.model.Transferencia;
import bolsaGogos.model.interfaces.DAO.TransferenciaDAOInterface;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.joda.time.DateTime;

/**
 * Classe responsável pela conexão ao banco e interações com a tabela Transferencias.
 * 
 */
public class TransferenciaDAO implements TransferenciaDAOInterface{
    
    Configurador config;
    
    public TransferenciaDAO(){ 
           config = new Configurador();
    }
    
    /**
    * Método responsável pela inserção de entradas nas Tabelas transferencias e lancamentosdinheiro, 
    * através da chamada da Stored Procedure RegistrarTransferencia()
    */
    @Override
    public boolean creditoDinheiro(Transferencia transferencia){
        float valor = (float)transferencia.getValor();
        
        try (Connection conn = config.conectar()){
            if (conn == null){
                return false;
            }
        
            CallableStatement cs = conn.prepareCall("{call RegistrarTransferencia(?,?,?,?,?)}");
            cs.setInt(1, transferencia.getIdUsuario());
            cs.setString(2, transferencia.getBancoOrigem());
            cs.setString(3, transferencia.getAgenciaOrigem());
            cs.setString(4, transferencia.getContaOrigem());
            cs.setFloat(5, valor);
            cs.execute();
           
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;   
    }
}
