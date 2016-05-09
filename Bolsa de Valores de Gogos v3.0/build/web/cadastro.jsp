<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="bolsaGogos.model.DAO.UsuarioDAO"%>
<%@page import="bolsaGogos.model.Usuario"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolsa de Valores de Gogos - Teste do BD</title>
    </head>
    <body>
           
        <header>
            <a style="color:blue" href="index.jsp">Home</a>
        </header>
        
        <h1 style="text-align:center"> Bolsa de Valores de Gogos</h1>
        
        <br><h2>Inserção de usuário no banco</h2>
            
        <%  
            UsuarioDAO acesso = new UsuarioDAO();
            
            int random = (int)(Math.random() * 10000);
            String result = Integer.toString(random);
            String email = "arthur" + result + "@uniriotec.br";
            Usuario user = new Usuario(1, "Arthur", result , result, email, "123");
            if(acesso.insereUsuario(user)){

            %> 
            
            <h3>Usuário inserido com sucesso!!</h3>
            <p>Bem vindo <%=user.getEmail()%>.</p><br> 
            <%
                
            }else{
            
                
            %>
             
            <h3>Falha na Inserção do</h3>
            
            <br><br><br>
            
        <%
            }
            %>  
        
        <h2>Recuperação de todos os usuários do banco</h2>
        <%
            Usuario cobaia;
            List<Usuario> lista = acesso.getListaUsuarios();
                     
            
            for(int i=0; i < lista.size(); i++){
                cobaia = lista.get(i);
            %>
            <p>O usuário <%=cobaia.getNome()%>, de ID <%=cobaia.getId()%>,  possui o CPF -<%=cobaia.getCpf()%>- e email -<%=cobaia.getEmail()%>-. 
            
            <br><br>
        <%
            }
            %>
            
              
   </body>
</html>       