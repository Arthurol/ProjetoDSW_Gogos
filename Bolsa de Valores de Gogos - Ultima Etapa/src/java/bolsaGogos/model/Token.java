package bolsaGogos.model;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
* Classe responsável por instanciar objetos do tipo Token
*/
public @Getter @Setter class Token {
    private int id;
    private int idUsuario;
    private String token;
    private DateTime dataValidade;
    

    public Token(int idUsuario, String token){
        this.idUsuario = idUsuario;
        this.token = token;
    }
    
    public Token(){
        id = -1;
        idUsuario = -1;
        token = "";
        dataValidade = null;
    }
}
