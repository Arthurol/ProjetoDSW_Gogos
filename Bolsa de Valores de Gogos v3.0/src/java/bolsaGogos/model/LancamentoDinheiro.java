package bolsaGogos.model;
import org.joda.time.DateTime;
import lombok.Getter;
import lombok.Setter;


/**
* Classe responsável por instanciar objetos do tipo LancamentoDinheiro
*/
public @Getter @Setter class LancamentoDinheiro {
    private TipoDeOperacao operacao;
    private int id;
    private int idUsuario;
    private DateTime data;
    private String historico;
    private double valor;
    
}
