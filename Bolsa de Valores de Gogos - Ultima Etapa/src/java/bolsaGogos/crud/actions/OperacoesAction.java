package bolsaGogos.crud.actions;

import bolsaGogos.model.DAO.DAOFactory;
import bolsaGogos.model.LancamentoPersonagem;
import bolsaGogos.model.Oferta;
import bolsaGogos.model.Personagem;
import bolsaGogos.model.StatusDaOferta;
import bolsaGogos.model.Usuario;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * Implementação das ações a serem usaras pelo framework STRUTS nos processos de adição e remoção de personagens, criação de ofertas de compra e venda, e casamento de ofertas
 */
public class OperacoesAction extends ActionSupport implements RequestAware, SessionAware {
    LoginAction login;
    private @Setter Map<String, Object> request;
    private @Setter Map<String, Object> session;
    private @Setter String personagem;
    private @Setter int idUsuario;
    private @Setter int quantidade;
    private @Setter int idOfertaRm;
    private @Setter @Getter List<String> nomePersonagens;
    private @Setter String tipoOferta;
    private @Setter float preco;
    private @Setter float saldo; 
    
    /**
    * Recupera a lista com o nome dos personagens e a coloca na memoria de requisição. A lista sera exibida num menu drop down na pagina jsp destino.
    */
    public String preparaAdicaoPersonagem(){
        if (verificaUsuarioLogado()) {
            Usuario usuario = (Usuario) session.get("user");
            request.put("idUsuario", usuario.getId());
            
            nomePersonagens = getNomePersonagens();
            request.put("nomePersonagens", nomePersonagens);
            
            return SUCCESS;  
        } 
        request.put("alerta", getText("error.user.notLogged"));
        return INPUT;
    }
    
    /**
    * Faz um lançamento de crédito de personagem no banco, com a quantidade e o personagem informados
    */
    public String adicionaPersonagem() {
        if (verificaUsuarioLogado()) {
            
            request.put("nomePersonagens", nomePersonagens);
            if (personagem.equals("Escolha um Personagem"))
                return INPUT;
            
            int idPersonagem = DAOFactory.getPersonagemDAO().getIdByName(personagem);
            
            if (idPersonagem == -1) {
                request.put("alerta", getText("error.add.gogo"));
                return INPUT;
            }
            
            Usuario usuario = (Usuario) session.get("user");
            LancamentoPersonagem lancamento = new LancamentoPersonagem(usuario.getId(), idPersonagem, quantidade);
            System.out.println("Teste lancamento, userId:" + lancamento.getIdUsuario() + "  gogoId: " + lancamento.getIdPersonagem() + " preco: " + lancamento.getPrecoUnitario());
            
            if (DAOFactory.getLancamentoPersonagemDAO().creditaPersonagem(lancamento)) {
                request.put("alerta2", getText("alert.gogoInsert.success"));
                return SUCCESS;
            }
            else {
                request.put("alerta", getText("error.add.gogo"));
                return INPUT;
            }   
        }
        return INPUT;
    }
    
    /**
    * Prepara uma lista com os personagens possuidos pelo usuario
    */
    public String preparaRemocaoPersonagem() {
        
        return SUCCESS;
    }
    
    /**
    * Faz um lançamento de débito de personagem no banco, com a quantidade e o personagem informados
    */
    public String removePersonagem() {
        return SUCCESS;
    }
    
    public String preparaCriacaoOferta() {
        if (verificaUsuarioLogado()) {
            Usuario usuario = (Usuario) session.get("user");
            request.put("idUsuario", usuario.getId());
            
            nomePersonagens = getNomePersonagens();    
            request.put("nomePersonagens", nomePersonagens);
            
            float saldo = DAOFactory.getLancamentoDinheiroDAO().calculaSaldoDinheiro(usuario.getId());
            request.put("saldo", saldo);
            
            return SUCCESS;  
        } 
        request.put("alerta", getText("error.user.notLogged"));
        return INPUT;
    }
    
    /**
    * Processa os dados da oferta selecionada no jsp (compra ou venda)
    */
    public String processaOferta() {
        if (verificaUsuarioLogado()) {
            
            nomePersonagens = getNomePersonagens(); 
            request.put("nomePersonagens", nomePersonagens);
            
            if (personagem.equals("Escolha um Personagem")) {
                request.put("alert", getText("error.char.notSelected"));
                return INPUT;
            }
            
            int idPersonagem = DAOFactory.getPersonagemDAO().getIdByName(personagem);
            
            if (idPersonagem == -1) {
                request.put("alerta", getText("error.add.offer"));
                return INPUT;
            }
            
            if (preco <= 0) {
                request.put("alerta", getText("error.price.invalid"));
                return INPUT;
            }
            
            if (tipoOferta == null || tipoOferta.isEmpty()) {
                request.put("alerta", getText("error.offerType.empty"));
                return INPUT;
            }
                       
            double precoDouble = (double) preco;
            Usuario usuario = (Usuario) session.get("user");
            Oferta oferta = new Oferta(idPersonagem, usuario.getId(), quantidade, precoDouble);
            
            int resultado = -1;
            
            if (tipoOferta.equals("Oferta de Compra"))
                resultado = DAOFactory.getOfertaDAO().insereOfertaCompra(oferta);   
            
            else if (tipoOferta.equals("Oferta de Venda"))
                resultado = DAOFactory.getOfertaDAO().insereOfertaVenda(oferta);
                
            if (resultado == -1) {
                request.put("alerta", getText("error.add.offer"));
                return INPUT;
            }
            else if (resultado == 1) {
                request.put("alerta", getText("error.insufficient.balance"));
                return INPUT;
            }
        } 
        request.put("alerta2", getText("alert.createOffer.success"));
        return SUCCESS;
    }
    
    public String exibeOfertas() {
        Usuario usuario = (Usuario) session.get("user");
        List<Oferta> listaOfertas = DAOFactory.getOfertaDAO().getByIdUsuario(usuario.getId());
        
        if (listaOfertas.isEmpty() || listaOfertas == null) {
            request.put("alerta", "Não existem ofertas para exibição");
            return INPUT;
        }
        request.put("aberta", StatusDaOferta.aberta.toString());
        request.put("listaOfertas", listaOfertas);
        return SUCCESS;
    }
    
    public String removeOferta() {
        if (verificaUsuarioLogado()) {
            
            if (idOfertaRm == -1) {
                request.put("alerta", "idOfertaRm sendo passada com id -1");
            }
            else 
                if (DAOFactory.getOfertaDAO().removeOferta(idOfertaRm))
                    request.put("alerta2", "Oferta deletada com sucesso!"); System.out.println("id: " + idOfertaRm);  
            
            return SUCCESS;
        }
        request.put("alerta", getText("error.user.notLogged"));
        return INPUT;
    }
    
    /**
    * Gera o relatório de todos os lançamentos de moeda feitos usando o Id do usuário
    */
    public String imprimeExtratoDinheiro() {
        
        if(verificaUsuarioLogado()) {
            Usuario usuario = (Usuario) session.get("user");
            String extrato = DAOFactory.getLancamentoDinheiroDAO().consultaExtrato(usuario.getId());
            
            if (extrato.isEmpty()) {
                request.put("alerta", getText("error.registry.notFound"));
                return SUCCESS;
            }
            request.put("extratoDinheiro", extrato);
            return SUCCESS;
        }
        request.put("alerta", getText("error.user.notLogged"));
        return SUCCESS;
    }
    
    /**
    * Gera o relatório de todos os lançamentos de personagens feitos usando o Id do usuário
    */
    public String imprimeExtratoPersonagens() {
        
        if(verificaUsuarioLogado()) {
            Usuario usuario = (Usuario) session.get("user");
            String extrato = DAOFactory.getLancamentoPersonagemDAO().consultaExtrato(usuario.getId());
            
            if (extrato.isEmpty()) {
                request.put("alerta", getText("error.registry.notFound"));
                return SUCCESS;
            }
            
            request.put("extratoPersonagens", extrato);
            return SUCCESS;
        }
        request.put("alerta", getText("error.user.notLogged"));
        return SUCCESS;
    }
    
    /**
    * Verifica, através da memória de sessão, se o usuário está logado
    */
    public boolean verificaUsuarioLogado() {
        if (session.get("user")!= null) {
            
            Usuario usuario = (Usuario) session.get("user");
            
            if (usuario.getEmail() != null) {

                Usuario usuarioCheck = DAOFactory.getUsuarioDAO().getUsuarioEmail(usuario.getEmail());

                if (usuarioCheck == null)
                    return false;
                else
                    return true;
            }
        }     
        return false;
    }
    
    /**
    * Recupera do banco uma lista com todos os Personagens que participam da bolsa
    */
    public List<String> getNomePersonagens() {
        
        List<Personagem> listaPersonagens = new ArrayList<>();
        listaPersonagens = DAOFactory.getPersonagemDAO().getListaPersonagens();
        List<String> nomePersonagens = new ArrayList<>();

        for (int i = 0; i < listaPersonagens.size(); i++) {
            String nome = listaPersonagens.get(i).getNome();
            nomePersonagens.add(nome);
        }
        
        return nomePersonagens;

    }
    
}