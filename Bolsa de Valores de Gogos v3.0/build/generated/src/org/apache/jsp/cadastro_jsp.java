package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import bolsaGogos.modelo.DAO.UsuarioDAO;
import bolsaGogos.modelo.Usuario;
import java.util.List;

public final class cadastro_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>Bolsa de Valores de Gogos - Teste do BD</title>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        \n");
      out.write("        <h3>Teste do Header:</h3>\n");
      out.write("        \n");
      out.write("        <header>\n");
      out.write("            <a style=\"color:blue\" href=\"index.jsp\">Home</a>\n");
      out.write("        </header>\n");
      out.write("        \n");
      out.write("        <h1 style=\"text-align:center\"> Bolsa de Valores de Gogos</h1>\n");
      out.write("        \n");
      out.write("        <h2>Recuperação de todos os usuários do banco</h2>\n");
      out.write("        ");

            UsuarioDAO acesso = new UsuarioDAO();
            
            Usuario cobaia;
            List<Usuario> lista = acesso.getListaUsuarios();
                     
            
            for(int i=0; i < lista.size(); i++){
                cobaia = lista.get(i);
            
      out.write("\n");
      out.write("            <p>O usuário ");
      out.print(cobaia.getNome());
      out.write(", de ID ");
      out.print(cobaia.getId());
      out.write(",  possui o CPF -");
      out.print(cobaia.getCpf());
      out.write("- e email -");
      out.print(cobaia.getEmail());
      out.write("-. \n");
      out.write("            \n");
      out.write("            <br><br>\n");
      out.write("        ");

            }
            
      out.write("\n");
      out.write("            \n");
      out.write("            <br><h2>Inserção de usuário no banco</h2>\n");
      out.write("            \n");
      out.write("        ");
  
            int random = (int)(Math.random() * 10000);
            String result = Integer.toString(random);
            String email = "arthur" + result + "@uniriotec.br";
            Usuario user = new Usuario(1, "Arthur", result , result, email, "123");
            if(acesso.insereUsuario(user)){

            
      out.write(" \n");
      out.write("            \n");
      out.write("            <h3>Usuário inserido com sucesso!!</h3>\n");
      out.write("            <p>Bem vindo ");
      out.print(user.getEmail());
      out.write(".</p> \n");
      out.write("            ");

                
            }else{
            
                
            
      out.write("\n");
      out.write("             \n");
      out.write("            <h3>Falha na Inserção do</h3>\n");
      out.write("            \n");
      out.write("            <br><br><br>\n");
      out.write("            \n");
      out.write("        ");

            }
            
      out.write("    \n");
      out.write("            ");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
