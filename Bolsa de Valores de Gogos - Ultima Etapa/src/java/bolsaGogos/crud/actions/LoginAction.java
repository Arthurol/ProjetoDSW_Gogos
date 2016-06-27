package bolsaGogos.crud.actions;

import bolsaGogos.model.DAO.DAOFactory;
import bolsaGogos.model.Usuario;
import bolsaGogos.model.Token;
import bolsaGogos.model.Transferencia;

import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.util.Map;
import lombok.Setter;
import com.opensymphony.xwork2.ActionSupport;
import java.util.regex.Pattern;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * Implementação das ações a serem usaras pelo framework STRUTS nos processos de login, cadastro, atualização de dados/senha e reset de senha.
 */
public class LoginAction extends ActionSupport implements RequestAware, SessionAware {
    
    private @Setter Map<String, Object> request;
    private @Setter Map<String, Object> session;
    
    private @Setter Usuario user;
    private @Setter int id;
    private @Setter String nome;
    private @Setter String email;
    private @Setter String cpf;
    private @Setter String telefone;
    private @Setter String token; 
    private @Setter String senhaLogin;
    private @Setter String senhaAntiga;
    private @Setter String senhaNova1;
    private @Setter String senhaNova2;
    
    
    public LoginAction(){
        id = -1;    
    }
    
    /**
     * Periodo de validade do token de troca de senha
     */
    private static final int VALIDATE_TOKEN_SENHA = 72;
    
    
    /**
     * Ação de login
     */
    public String log() throws NoSuchAlgorithmException, UnsupportedEncodingException {
         
        if (email.isEmpty() || senhaLogin.isEmpty() || email == null || senhaLogin == null) {
            addFieldError("email", getText("error.loginInfo.required"));
            return INPUT;
        }
        
        Usuario usuario = DAOFactory.getUsuarioDAO().getUsuarioEmail(email); 
        if (usuario == null){
            addFieldError("email", getText("error.loginInfo.required"));
            return INPUT;
        }
        
        if (usuario.isActive() == false)
            addFieldError("email", getText("error.user.notActive")); 
        
        if (usuario.getNumeroLoginsFalhos() >= 3){
            addFieldError("email", getText("error.password.expired"));
            return INPUT;
        }
              
        String senhaCodificada = criptografaSenha(senhaLogin); // <- vai criptografar a senha de login e comparar com a do banco 
        
        if (usuario.getSenha().compareTo(senhaCodificada) != 0){
                DAOFactory.getUsuarioDAO().indicarLoginFalha(usuario.getId());
                addFieldError("email", getText("error.loginInfo.required")); 
        }

        if (hasErrors())
            return INPUT;
        
        DateTime dataUltimoLogin = DAOFactory.getUsuarioDAO().getDataUltimoLogin(usuario.getId()); 
        
        if (dataUltimoLogin != null){
                DateTimeFormatter sdf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
                request.put("alerta2", "Seu último login no sistema foi realizado em " + usuario.getUltimoLogin().toString(sdf) );
        }        
        
        DAOFactory.getUsuarioDAO().indicarLoginSucesso(usuario.getId());
        
        session.put("user", usuario);
        request.put("user", usuario);
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
     * Encaminhamento para a página inicial
     */
    public String homepage(){
        if (verificaUsuarioLogado()) {
            return SUCCESS;
        }
        else {
            return INPUT;
        }
    }
     
    /**
     * Ação de logout 
     */ 
    public String logout(){
     
        if (verificaUsuarioLogado()){
            session.clear();
            request.put("alerta2", getText("alert.logout.success"));
            return SUCCESS;
        }
        else {
            request.put("alerta", getText("alert.user.notLogged"));
            return INPUT;
        }
            
    }
    
    /**
     * Faz a criptografia através de hashing. O algoritmo usado é o SHA-256
     */
    public String criptografaSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        String senhaCodificada = "";  
        try{
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            
            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
              
            }
            senhaCodificada = hexString.toString();
            
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
            System.out.println(e);
            return null;
        }
        
        return senhaCodificada;
    }    

    /**
    * Encaminhamento para a página de troca de senha
    */ 
    public String preparaTrocaSenha(){
        if (verificaUsuarioLogado()) {
            request.put("user", user);
            return SUCCESS;
        }
        else
            return INPUT;
    }
    
    /**
    * Ação de troca de senha
    */
    public String trocaSenha() throws NoSuchAlgorithmException, UnsupportedEncodingException{
         
        if (email.isEmpty()){
             addFieldError("email", getText("error.email.required"));
             return INPUT;
        }
        
        if (senhaAntiga.isEmpty()) {
            addFieldError("senhaAntiga", getText("error.oldPassword.required"));
            return INPUT;
        }
   
        String senhaAntigaCripto = criptografaSenha(senhaAntiga);
        
        Usuario usuario = DAOFactory.getUsuarioDAO().getUsuarioEmail(email);
        if (usuario == null){
             addFieldError("email", getText("error.email.invalid"));
             return INPUT;
        }

        if (usuario.getSenha().compareTo(senhaAntigaCripto) != 0)
            addFieldError("senhaAntiga", getText("error.oldPassword.invalid"));
        
        if (senhaNova1.isEmpty())
            addFieldError("senhaNova1", getText("error.newPassword1.required")); 
        
        if (senhaAceitavel(senhaNova1) == false)
            addFieldError("senhaNova1", getText("error.newPassword1.invalid"));
        
        if (senhaNova2.isEmpty())
            addFieldError("senhaNova2", getText("error.newPassword2.required")); 
        
        if (senhaNova1.compareTo(senhaNova2) != 0)
            addFieldError("senhaNova2", getText("error.newPassword2.invalid")); 

        if (hasErrors())
            return INPUT;
        
        String novaSenhaCodificada = criptografaSenha(senhaNova1);
        usuario.setSenha(novaSenhaCodificada);

        DAOFactory.getUsuarioDAO().atualizaSenha(usuario.getId(), novaSenhaCodificada);
        
        request.put("alerta2", getText("alert.password.changed"));
        request.put("user", usuario);
        session.clear();
        return SUCCESS;  
    }

    /**
     * Prepara o formulario de envio de token por esquecimento de senha
     */
    public String preparaEnvioToken(){
            return SUCCESS;
    }

    /**
    * Acao de envio de token por esquecimento de senha 
    */
    public String envioToken() throws NoSuchAlgorithmException, UnsupportedEncodingException{

        if (email.isEmpty()){
             addFieldError("email", getText("error.email.required"));
             return INPUT;
        }

        Usuario usuario = DAOFactory.getUsuarioDAO().getUsuarioEmail(email);
        if (usuario == null){
             addFieldError("email", getText("error.email.invalid"));
             return INPUT;
        }
        
        token = geraTokenTrocaSenha();
        
        Token tokenGerado = new Token(usuario.getId(), token);
        
        if (DAOFactory.getTokenDAO().insereToken(tokenGerado)) {
            request.put("token", token);
            request.put("user", usuario);
            return SUCCESS;
        }
        else {
            request.put("alerta", getText("error.insert.token"));
            return INPUT;
        }
    }

    /**
    * Gera um token para troca de senha
    */
    private String geraTokenTrocaSenha() {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < 64; i++) {
                char c = (char) ('A' + r.nextInt(26));
                sb.append(c);
        }
        return sb.toString();
    }

    /**
    * Acao que recebe e verifica a validade do token de troca de senha
    */
    public String verificaTokenSenha(){
        
        if (token.isEmpty()){
            addFieldError("token", getText("error.token.required"));
            return INPUT;
        }
           
        if (email.isEmpty()){
            addFieldError("email", getText("error.email.required"));
            return INPUT;
        }
        
        request.put("token", token);
        Usuario usuario = DAOFactory.getUsuarioDAO().getUsuarioEmail(email);
        
        if (usuario == null){
            addFieldError("email", getText("error.email.invalid"));
            return INPUT;
        } 
            
        boolean valido = DAOFactory.getTokenDAO().verificaTokenTrocaSenha(usuario.getId(), token, VALIDATE_TOKEN_SENHA);

        if (valido == false){
            addFieldError("token", getText("error.token.invalid"));
            request.put("email", usuario.getEmail());
            return INPUT;
        }   

        request.put("token", token);
        request.put("email", usuario.getEmail());
        
        return SUCCESS;
    }

    /**
    * Executa uma troca de senha baseada em reinicializacao
    */
    public String executaResetSenha() throws NoSuchAlgorithmException, UnsupportedEncodingException{   
        request.put("token", token);
        
        Usuario usuario = DAOFactory.getUsuarioDAO().getUsuarioEmail(email); 
        if (usuario == null){
            addFieldError("email", getText("error.invalid.info"));
            return INPUT;
        }
        boolean valido = DAOFactory.getTokenDAO().verificaTokenTrocaSenha(usuario.getId(), token, VALIDATE_TOKEN_SENHA);
        
        if (valido == false){
            addFieldError("token", getText("error.invalid.info"));
            return INPUT;
        }
        
        if (senhaNova1.isEmpty())
            addFieldError("senhaNova1", getText("error.newPassword1.required")); 
        
        if (senhaAceitavel(senhaNova1) == false)
            addFieldError("senhaNova1", getText("error.newPassword1.invalid"));
        
        if (senhaNova2.isEmpty())
            addFieldError("senhaNova2", getText("error.newPassword2.required")); 
        
        if (senhaNova1.compareTo(senhaNova2) != 0)
            addFieldError("senhaNova2", getText("error.newPassword2.invalid")); 

        if(hasErrors())
            return INPUT;

        String novaSenhaCodificada = criptografaSenha(senhaNova1);
        usuario.setSenha(novaSenhaCodificada);

        if (DAOFactory.getUsuarioDAO().atualizaSenha(usuario.getId(), novaSenhaCodificada)){
        
            request.put("alerta2", getText("alert.password.changed"));
            request.put("user", usuario);
            return SUCCESS;
        }
        else
            request.put("alerta", getText("alert.password.updateFail"));
            session.clear();
            return INPUT;     
    }

    /**
    * Acao que cria um novo usuário e o joga na memória de requisição
    */
    public String novo(){
        
        request.put("user", user);
        return SUCCESS;
    }

    /**
    * Acao que salva os dados de um novo usuario
    */
    public String salva() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setCpf(cpf);
        usuario.setTelefone(telefone);
        
        request.put("user", usuario);

        // Salva a nova senha
        String senhaCodificada = criptografaSenha(senhaNova1);
        usuario.setSenha(senhaCodificada);

        // Verifica as regras de negocio
        if (nome.isEmpty())
            addFieldError("nome", getText("error.name.required"));
        
        if (nome.length() > 80)
            addFieldError("nome", getText("error.name.invalid"));
        
        if (email.isEmpty())
            addFieldError("email", getText("error.email.required"));
       
        if (email.length() > 80)
            addFieldError("email", getText("error.email.invalid"));
        
        if (!telefoneAceitavel(telefone))
            addFieldError("telefone", getText("error.telephone.invalid"));
        
        if (!cpfAceitavel(cpf))
            addFieldError("cpf", getText("error.personId.invalid"));

        if (!senhaAceitavel(senhaNova1))
            addFieldError("senhaNova1", getText("error.newPassword1.invalid"));
            
        if(senhaNova1.compareTo(senhaNova2) != 0)
            addFieldError("senhaNova2", getText("error.newPassword2.invalid"));
        
        Usuario usuario2 = DAOFactory.getUsuarioDAO().getUsuarioEmail(usuario.getEmail());
        
        if (usuario2 != null) {
            if (usuario2.getEmail().equals(usuario.getEmail())) {
                addFieldError("email", getText("error.email.exist"));
            }
        }
        
        if (hasErrors()){
            request.put("alerta", getText("error.form.fields"));
            return INPUT;
        }
        
        if (DAOFactory.getUsuarioDAO().insereUsuario(usuario)){
            request.put("alerta2", getText("alert.insert.success"));
            return SUCCESS;
        }
        else
            return INPUT;
    }
    
    public String transferenciaInicial() {
        
        Usuario usuario = (Usuario) request.get("user");
        Usuario usuarioCheck = DAOFactory.getUsuarioDAO().getUsuarioEmail(usuario.getEmail());
        
        if (usuarioCheck != null) {
            Transferencia transferencia = new Transferencia();
            transferencia.setIdUsuario(usuarioCheck.getId());
            transferencia.setBancoOrigem("001");
            transferencia.setAgenciaOrigem("111111");
            transferencia.setContaOrigem("0001111111");
            transferencia.setValor(1000.0);
            
            if (DAOFactory.getTransferenciaDAO().creditoDinheiro(transferencia))
                request.put("alerta2", "O saldo inicial de R$ 1000,00 foi adicionado com a criaçao da conta!");
            
            return SUCCESS;
        }    
        return SUCCESS;
    }
    
    /**
    * Ação que inicia o processo de edição das informações do usuário
    */
    public String preparaEdicaoUsuario(){
        if (verificaUsuarioLogado()) {
            Usuario usuario = (Usuario) session.get("user");
            Usuario usuarioCheck = DAOFactory.getUsuarioDAO().getUsuarioEmail(usuario.getEmail());
            
            if (usuarioCheck != null){
                request.put("user", usuarioCheck);
            }
            else {
                request.put("alerta", getText("error.update.error"));
                return INPUT;
            } 
            return SUCCESS;
        }
        request.put("alerta", getText("error.user.notLogged"));
        return INPUT;
    }
    
    /**
    * Acao que edita os dados de um usuario existente
    */
    public String atualiza() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.setCpf(cpf);
        
        request.put("user", usuario);

        // Verifica as regras de negocio
        if (nome.isEmpty())
            addFieldError("nome", getText("error.name.required"));
        
        if (nome.length() > 80)
            addFieldError("nome", getText("error.name.invalid"));
        
         if (!telefoneAceitavel(telefone))
            addFieldError("telefone", getText("error.telephone.invalid"));
        
        if (!cpfAceitavel(cpf))
            addFieldError("cpf", getText("error.personId.invalid"));
        
        
        if (hasErrors()){
            request.put("alerta", getText("error.form.fields"));
            return INPUT;
        }
        
        if (DAOFactory.getUsuarioDAO().atualizaUsuario(usuario)){
            request.put("alerta2", getText("alert.userEdit.success"));
            return SUCCESS;
        }
        else
            return INPUT;
    }
    
    
    
    /**
     * Verifica se uma senha é aceitável, checando se ela tem pelo menos 8 caracteres, uma letra e um numero
     */
    private boolean senhaAceitavel(String senha){
        return (senha.length() >= 8) && senha.matches(".*[a-zA-Z].*") && senha.matches(".*[0-9].*");
    }
    
    /**
     * Verifica se um telefone é aceitável, checando se ele tem pelo menos 7 caracteres, uma letra e um numero
     */
    private boolean telefoneAceitavel(String telefone){
        return (telefone.length() >= 7) && (telefone.length() <= 20) && telefone.matches(".*[0-9].*") && !Pattern.matches("[a-zA-Z]+", telefone);
    }
    
    /**
     * Verifica se um cpf é aceitável, checando se ele tem pelo menos 11 caracteres, e apenas numeros
     */
    private boolean cpfAceitavel(String cpf){
        return (cpf.length() >= 11) && (cpf.length() <= 14) && (cpf.matches(".*[0-9].*")) && !Pattern.matches("[a-zA-Z]+", cpf) ;
    }
    
}