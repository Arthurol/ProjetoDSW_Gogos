package bolsaGogos.model.DAO;

/**
 * Classe que concentra o acesso aos DAOs do sistema
 */
public class DAOFactory
{
    private static UsuarioDAO usuarioDAO;
    private static TokenDAO tokenDAO;
    private static LancamentoPersonagemDAO lancamentoPersonagemDAO;
    private static LancamentoDinheiroDAO lancamentoDinheiroDAO;
    private static PersonagemDAO personagemDAO;
    private static OfertaDAO ofertaDAO;
    private static CasamentoDeOfertaDAO casamentoDeOfertaDAO;
    private static TransferenciaDAO transferenciaDAO;
    
    /**
    * Retorna uma instância de UsuarioDAO
    */
    public static UsuarioDAO getUsuarioDAO(){
        if (usuarioDAO == null)
            usuarioDAO = new UsuarioDAO();
        
        return usuarioDAO;
    }
    
    /**
    * Retorna uma instância de TokenDAO
    */
    public static TokenDAO getTokenDAO(){
        if (tokenDAO == null)
            tokenDAO = new TokenDAO();
        
        return tokenDAO;
    }
    
    /**
    * Retorna uma instância de LancamentoPersonagemDAO
    */
    public static LancamentoPersonagemDAO getLancamentoPersonagemDAO(){
        if (lancamentoPersonagemDAO == null)
            lancamentoPersonagemDAO = new LancamentoPersonagemDAO();
        
        return lancamentoPersonagemDAO;
    }
    
    /**
    * Retorna uma instância de PersonagemDAO
    */
    public static PersonagemDAO getPersonagemDAO(){
        if (personagemDAO == null)
            personagemDAO = new PersonagemDAO();
        
        return personagemDAO;
    }
    
    /**
    * Retorna uma instância de OfertaDAO
    */
    public static OfertaDAO getOfertaDAO(){
        if (ofertaDAO == null)
            ofertaDAO = new OfertaDAO();
        
        return ofertaDAO;
    }
    
    /**
    * Retorna uma instância de LancamentoDinheiroDAO
    */
    public static LancamentoDinheiroDAO getLancamentoDinheiroDAO(){
        if (lancamentoDinheiroDAO == null)
            lancamentoDinheiroDAO = new LancamentoDinheiroDAO();
        
        return lancamentoDinheiroDAO;
    }
    
    /**
    * Retorna uma instância de CasamentoDeOfertaDAO
    */
    public static CasamentoDeOfertaDAO getCasamentoDeOfertaDAO(){
        if (casamentoDeOfertaDAO == null)
            casamentoDeOfertaDAO = new CasamentoDeOfertaDAO();
        
        return casamentoDeOfertaDAO;
    }
    
    /**
    * Retorna uma instância de TransferenciaDAO
    */
    public static TransferenciaDAO getTransferenciaDAO(){
        if (transferenciaDAO == null)
            transferenciaDAO = new TransferenciaDAO();
        
        return transferenciaDAO;
    }
    
}
	