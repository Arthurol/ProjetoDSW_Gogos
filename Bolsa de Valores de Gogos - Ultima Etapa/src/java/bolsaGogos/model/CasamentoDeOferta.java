package bolsaGogos.model;
import org.joda.time.DateTime;


/**
* Classe responsável por instanciar objetos do tipo CasamentoDeOferta
*/
public class CasamentoDeOferta {
    private int IdOfertaVenda;
    private int idOfertaCompra;
    private DateTime  dataExecucaoCasamento;
    

    public int getIdOfertaVenda() {
        return IdOfertaVenda;
    }

    public void setIdOfertaVenda(int IdOfertaVenda) {
        this.IdOfertaVenda = IdOfertaVenda;
    }

    public int getIdOfertaCompra() {
        return idOfertaCompra;
    }

    public void setIdOfertaCompra(int idOfertaCompra) {
        this.idOfertaCompra = idOfertaCompra;
    }

    public DateTime getDataExecucaoCasamento() {
        return dataExecucaoCasamento;
    }

    public void setDataExecucaoCasamento(DateTime dataExecucaoCasamento) {
        this.dataExecucaoCasamento = dataExecucaoCasamento;
    }
    
}
