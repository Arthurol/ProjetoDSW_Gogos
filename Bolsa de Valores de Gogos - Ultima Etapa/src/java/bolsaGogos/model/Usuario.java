package bolsaGogos.model;
import java.sql.Blob;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
* Classe responsável por instanciar objetos do tipo Usuario
*/
public @Getter @Setter class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String senha;
    private String email;
    private Blob foto;
    private int numeroLoginsFalhos;
    private DateTime ultimoLogin;
    
    public Usuario(int id, String nome, String cpf, String telefone, String email, String senha){
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }
    
    public Usuario(){
        id = -1;
        nome = "";
        cpf = "";
        telefone = "";
        email = "";
        senha = "";
        numeroLoginsFalhos = -1; 
        ultimoLogin = null;
        foto = null;
    }
    
    public boolean isActive(){
        return true;
    }
}
