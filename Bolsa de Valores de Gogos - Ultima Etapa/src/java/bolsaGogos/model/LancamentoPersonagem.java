package bolsaGogos.model;
import org.joda.time.DateTime;
import lombok.Getter;
import lombok.Setter;


/**
* Classe responsável por instanciar objetos do tipo LancamentoPersonagem
*/
public @Getter @Setter class LancamentoPersonagem {
    private TipoDeOperacao operacao;
    private int id;
    private int idUsuario;
    private int idPersonagem;
    private int quantidade;
    private double precoUnitario;
    private DateTime data;
    private String historico;
    
      
    public LancamentoPersonagem(TipoDeOperacao operacao, int id, int idUsuario, int idPersonagem, int quantidade, double precoUnitario, DateTime data, String historico){
        this.operacao = operacao;
        this.id = id;
        this.idUsuario = idUsuario;
        this.idPersonagem = idPersonagem;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.data = data;
        this.historico = historico;
    }
    
    /*
    * Construtor que monta o LancamentoPersonagem com os dados necessários para inserção ou remoção do banco 
    *
    */
    public LancamentoPersonagem(int idUsuario, int idPersonagem, int quantidade){
        this.idUsuario = idUsuario;
        this.idPersonagem = idPersonagem;
        this.quantidade = quantidade;
    }
    
    public LancamentoPersonagem(){
        operacao = null;
        id = -1;
        idUsuario = -1;
        idPersonagem = -1;
        quantidade = -1;
        precoUnitario = 0.0;
        data = null;
        historico = "";
    }
    
}
