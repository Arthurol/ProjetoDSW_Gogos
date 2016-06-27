package bolsaGogos.model;
import lombok.Getter;


/**
* Enum que representa as quatro operações possíveis a serem executadas por um lançamento de dinheiro ou de personagem
*/
public enum TipoDeOperacao {
    credito(0), 
    debito(1), 
    bloqueio(2), 
    liberacao(3);

    private @Getter int codigo;
    
    private TipoDeOperacao(int codigo)
    {
        this.codigo = codigo;
    }
    
    public TipoDeOperacao get(int codigo)
    {
        this.codigo = codigo;
        
        switch(codigo){
            case 0:
                return TipoDeOperacao.credito;
            case 1:
                return TipoDeOperacao.debito;
            case 2:
                return TipoDeOperacao.bloqueio;
            case 3:
                return TipoDeOperacao.liberacao;
        }
        return null;
    }
}
