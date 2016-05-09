package bolsaGogos.model.DAO;

import bolsaGogos.model.interfaces.DAO.LancamentoPersonagemDAOInterface;
import bolsaGogos.model.TipoDeOperacao;
import bolsaGogos.model.LancamentoPersonagem;
import bolsaGogos.model.DAO.Configurador;
import bolsaGogos.model.DAO.Configurador;
import bolsaGogos.model.LancamentoPersonagem;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;


/**
 * Classe responsável pela conexão ao banco e interações com a tabela Lancamentospersonagem.
 * 
 */
public class LancamentoPersonagemDAO implements LancamentoPersonagemDAOInterface {
     Configurador config;
    
    public LancamentoPersonagemDAO(){ 
           config = new Configurador();
    }
    
    /**
    * Instancia e retorna um LancamentoPersonagem a partir dos dados de entrada, obtidos através de uma consulta ao banco
    * 
    */
    public LancamentoPersonagem montaLancamentoPersonagem(ResultSet rs) throws SQLException{
        
        //tipo é iniciado com um valor default(debito) apenas para evitar excessões de ponteiro null
        TipoDeOperacao tipo = TipoDeOperacao.debito; 
        //metodo get que atribui a tipo o real Enum correspondente à operação do registro recuperado do banco
        tipo = tipo.get(rs.getInt("operacao"));
        
        LancamentoPersonagem lancamento = new LancamentoPersonagem();
        lancamento.setId(rs.getInt("id"));
        lancamento.setIdUsuario(rs.getInt("idUsuario"));
        lancamento.setIdPersonagem(rs.getInt("idPersonagem"));
        lancamento.setQuantidade(rs.getInt("quantidade"));
        lancamento.setPrecoUnitario((double)rs.getFloat("precoUnitario"));
        lancamento.setHistorico(rs.getString("historico"));
        lancamento.setData(new DateTime(rs.getDate("data")));
        lancamento.setOperacao(tipo);
        
        return lancamento;
    }
    
    /*
    * Faz o lançamento do crédito de uma determinada quantidade personagens no banco 
    *
    */
    @Override
    public boolean creditaPersonagem(LancamentoPersonagem lancamento){
        
        try(Connection conn = config.conectar()){
             
            CallableStatement cs = conn.prepareCall("{call AdicionarPersonagem(?, ?, ?)}");
            cs.setInt(1, lancamento.getIdUsuario());
            cs.setInt(2, lancamento.getIdPersonagem());
            cs.setInt(3, lancamento.getQuantidade());
            cs.execute();
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;  
    }
    
    /*
    *Faz o lançamento do débito de uma determinada quantidade de personagens no banco
    *
    */
    @Override
    public boolean debitaPersonagem(LancamentoPersonagem lancamento){
        
        try(Connection conn = config.conectar()){
            if (conn == null){
                return false;
            }
            int erro = 0;
            CallableStatement cs = conn.prepareCall("{call RemoverPersonagem(?, ?, ?, ?)}"); 
            cs.setInt(1, lancamento.getIdUsuario());
            cs.setInt(2, lancamento.getIdPersonagem());
            cs.setInt(3, lancamento.getQuantidade());
            cs.setInt(4, erro);
            cs.executeQuery();
            
            if (erro == 1){
                System.out.println("Erro na remoção do personagem.");
                return false;
            }
                
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
         return true;
    }
    
    /*
    * Retorna a lista com todos os lançamentos de personagem realizados por um determinado usuário.
    *
    */
    @Override
    public List<LancamentoPersonagem> lancamentosPorUsuario(int idUsuario){   
        
        List<LancamentoPersonagem> lancamentos = new ArrayList<LancamentoPersonagem>();
        
        try (Connection conn = config.conectar()){
            if (conn == null) {
                return null;
            }
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM lancamentospersonagem WHERE idUsuario = ?");
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lancamentos.add(montaLancamentoPersonagem(rs));
            }
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lancamentos;
        
    }
     
    /*
    * Junta os extratos e as datas de todas os lançamentos da lista de entrada em uma única String e a retorna.
    *
    */
    @Override
    public String consultaExtrato(List<LancamentoPersonagem> lancamentos){  
        
        String extrato = "";
        
        for (int i = 0; i < lancamentos.size(); i++){
            String date = lancamentos.get(i).getData().toString();
            extrato = extrato + "\n" + lancamentos.get(i).getHistorico() + ", na data " + date;
        }
        
        return extrato;
    }
    
    /*
    * Retorna a quantidade de um determinado personagem que o usuário especificado tem.
    *
    */
    @Override
    public int calculaSaldoPersonagens(int idUsuario, int idPersonagem){
        
        int saldoPersonagens = 0;
        
        try (Connection conn = config.conectar()){
            if (conn == null){
                return -1;
            }
            
            CallableStatement cs = conn.prepareCall("{call CalculaSaldoDisponivelPersonagem(?, ?, ?)}"); 
            cs.setInt(1, idUsuario);
            cs.setInt(2, idPersonagem);
            cs.setInt(3, saldoPersonagens);
            
            cs.executeQuery();
            
                
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return -1;
        }
        
        return saldoPersonagens;
    }
}