package bolsaGogos.model.DAO;

import bolsaGogos.model.LancamentoDinheiro;
import bolsaGogos.model.TipoDeOperacao;
import bolsaGogos.model.DAO.interfaces.LancamentoDinheiroDAOInterface;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Classe responsável pela conexão ao banco e interações com a tabela Lancamentosdinheiro.
 * 
 */
public class LancamentoDinheiroDAO implements LancamentoDinheiroDAOInterface {
    // Objeto Configurador utilizado para realizar a conexão ao banco
    Configurador config;
    
    public LancamentoDinheiroDAO(){ 
        config = new Configurador();
    }

    /**
    * Instancia e retorna um LancamentoDinheiro a partir dos dados de entrada, obtidos através de uma consulta ao banco
    * 
    */
    private LancamentoDinheiro montaLancamentoDinheiro(ResultSet rs) throws SQLException{
        //tipo é iniciado com um valor default(debito) apenas para evitar excessões de ponteiro null
        TipoDeOperacao tipo = TipoDeOperacao.debito; 
        //metodo get que atribui a tipo o real Enum correspondente à operação do registro recuperado do banco
        tipo = tipo.get(rs.getInt("operacao"));
                
        LancamentoDinheiro lancamento = new LancamentoDinheiro();
        lancamento.setId(rs.getInt("id"));
        lancamento.setIdUsuario(rs.getInt("idUsuario"));
        lancamento.setHistorico(rs.getString("historico"));
        lancamento.setValor((double)rs.getFloat("valor"));
        lancamento.setData(new DateTime (rs.getTimestamp("data")));
        lancamento.setOperacao(tipo);
        
        return lancamento;
    }

    /*
    * Chama a Stored Procedure de cálculo de saldo e retorna o valor recuperado. O saldo é sempre inteiro.
    * 
    */
    @Override
    public int calculaSaldoDinheiro(int idUsuario){
        
        int saldo = 0;
        
        try(Connection conn = config.conectar()){
        if (conn == null){
            return -1;
        }

        CallableStatement cs = conn.prepareCall("{call CalculaSaldoDisponivelDinheiro(?,?)}");
        cs.setInt(1, idUsuario);
        cs.registerOutParameter(2, java.sql.Types.INTEGER);
        cs.execute();
        saldo = cs.getInt(2);
        
        conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return -1;
        }
        return saldo; 
    }
    
    /*
    * Retorna a lista com todos os lançamentos de personagem realizados por um determinado usuário.
    *
    */
    @Override
    public List<LancamentoDinheiro> lancamentosDinheiroUsuario(int idUsuario){   
        
        List<LancamentoDinheiro> lancamentos = new ArrayList<LancamentoDinheiro>();
        
        try (Connection conn = config.conectar()){
            if (conn == null) {
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM lancamentosdinheiro WHERE idUsuario = ?");
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lancamentos.add(montaLancamentoDinheiro(rs));
            }
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lancamentos;
        
    }
    
    /*
    * Junta os registros do banco, atributos e as datas de todas os lançamentos de moeda feitos pelo usuário em uma única String e a retorna.
    *
    */
    @Override
    public String consultaExtrato(int idUsuario){  
        List<LancamentoDinheiro> lancamentos = lancamentosDinheiroUsuario(idUsuario);
        String extrato = "";
        
        for (int i = 0; i < lancamentos.size(); i++){
            LancamentoDinheiro lancamento = lancamentos.get(i);
            DateTimeFormatter sdf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
            String dataString =  lancamento.getData().toString(sdf);
            
            extrato =  extrato + "<br><p>" + lancamento.getHistorico() + ". " + lancamento.getOperacao().toString().toUpperCase() +  " no valor de R$ " 
                    + lancamento.getValor() +  "0, em " + dataString + "</p>";
        }
        
        return extrato;
    }
    
}
