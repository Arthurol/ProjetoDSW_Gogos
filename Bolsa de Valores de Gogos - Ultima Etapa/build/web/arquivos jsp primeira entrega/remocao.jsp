<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="bolsaGogos.model.DAO.UsuarioDAO"%>
<%@page import="bolsaGogos.model.Usuario"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Remoção Usuarios</title>
    </head>
    <body>
             
        <header>
            <a style="color:blue" href="index.jsp">Home</a>
        </header>
        
        <h1 style="text-align:center"> Bolsa de Valores de Gogos</h1>
        
        <h2>Remoção dos usuários adicionados</h2>
        <%
            UsuarioDAO acessoUser = new UsuarioDAO();
             
            if (acessoUser.removeTodosUsuarios()){
        %> 
        
        <br><p> Todos os usuários não administradores foram removidos com sucesso!!</p><br>
        
        <%
            } else{

        %> 
        
        <p> Falha na remoção... </p>
        
        <%
            }
        %> 
        
        <h2>Lista dos usuários pertencentes ao banco:</h2>
        
        <%
            Usuario cobaia;
            List<Usuario> lista = acessoUser.getListaUsuarios();
                     
            
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