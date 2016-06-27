package bolsaGogos.model;
import org.joda.time.DateTime;
import lombok.Getter;
import lombok.Setter;

/**
* Classe responsável por instanciar objetos do tipo Transferencia
*/
public @Getter @Setter class Transferencia {
    private int id;
    private int idUsuario;
    private DateTime data;
    private double valor;
    private String bancoOrigem;
    private String agenciaOrigem;
    private String contaOrigem;

    public Transferencia(int idUsuario, double valor, String bancoOrigem, String agenciaOrigem, String contaOrigem){
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.bancoOrigem = bancoOrigem;
        this.agenciaOrigem = agenciaOrigem;         
        this.contaOrigem = contaOrigem;
    }
    
    public Transferencia(){
        id = -1;
        idUsuario = -1;
        data = null;
        valor = 0.0;
        bancoOrigem = "";
        agenciaOrigem = "";         
        contaOrigem = "";        
    }

}