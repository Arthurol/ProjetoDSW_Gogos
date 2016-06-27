package bolsaGogos.model;
import lombok.Getter;


/**
* Enum que representa os três estados de uma oferta no banco.
*/
public enum StatusDaOferta {
   
    aberta(0), 
    liquidada(1), 
    cancelada(2);
    
   private @Getter int codigo;
    
    private StatusDaOferta(int codigo)
    {
        this.codigo = codigo;
    }
    
     public StatusDaOferta get(int codigo)
    {
        this.codigo = codigo;
        
        switch(codigo){
            case 0:
                return StatusDaOferta.aberta;
            case 1:
                return StatusDaOferta.liquidada;
            case 2:
                return StatusDaOferta.cancelada;
            
        }
        return null;
    }
}
