import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Arthur
 */
public class LoginServiceTest {
    
    
    @Test
    public void testCriptografaSenha() throws Exception {
        String test="";
        for(int i = 0; i < 6; i++)
            test += i;
        String testCripto = criptografaSenha(test);
        
                
        assertEquals(criptografaSenha("012345"), testCripto);
    }

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
    
    
}
