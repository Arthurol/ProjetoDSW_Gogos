package bolsaGogos.model;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;


/**
* Classe responsável por instanciar objetos do tipo Oferta
*/
public @Getter @Setter class Oferta {
    private TipoDeOferta tipoOferta;
    private DateTime data;
    private int id;
    private int idPersonagem;
    private int idUsuario;
    private int quantidade;
    private double precoUnitario;
    private int quantidadeOriginal;
    private int idOfertaOriginal;
    private StatusDaOferta statusOferta;

    public Oferta(int idPersonagem, int idUsuario, int quantidade, double precoUnitario) {
        this.idPersonagem = idPersonagem;
        this.idUsuario = idUsuario;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }
    public Oferta() {
        id = -1;
        idPersonagem = -1;
        idUsuario = -1;
        quantidade = 0;
        precoUnitario = 0.0;
        quantidadeOriginal = 0;
        idOfertaOriginal = -1;
        statusOferta = null;
        tipoOferta = null;
        data = null;
    }
    
}
