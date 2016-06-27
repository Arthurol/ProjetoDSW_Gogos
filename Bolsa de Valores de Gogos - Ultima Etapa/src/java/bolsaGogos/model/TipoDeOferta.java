package bolsaGogos.model;
import lombok.Getter;


/**
* Enum que indica se uma oferta existente é de compra ou de venda
*/
public enum TipoDeOferta {
    venda(0),
    compra(1);
    
    private @Getter int codigo;
    
    private TipoDeOferta(int codigo)
    {
        this.codigo = codigo;
    }
    
     public TipoDeOferta get(int codigo)
    {
        this.codigo = codigo;
        
        switch(codigo){
            case 0:
                return TipoDeOferta.venda;
            case 1:
                return TipoDeOferta.compra;
        }
        return null;
    }
}
