<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="bolsaGogos.model.DAO.LancamentoDinheiroDAO"%>
<%@page import="bolsaGogos.model.DAO.UsuarioDAO"%>
<%@page import="bolsaGogos.model.Usuario"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Impressão do extrato</title>
    </head>
    <body>
        
        <header>
            <a style="color:blue" href="index.jsp">Home</a>
        </header>
        
        <h1 style="text-align:center"> Bolsa de Valores de Gogos</h1>
        
        <br><h2>Impressão do Extrato:</h2>
            
        <%  
            LancamentoDinheiroDAO lancamento = new LancamentoDinheiroDAO();
            String impressao = "";
            int i = 0;
            for (i = 0; i < 300 ; i++){
                
                
                impressao = lancamento.consultaExtrato(i);

                if(impressao != ""){
                    
                %> 

                <h3>Impressão do extrato do usuário de id <%=i%>: </h3>
                <p><%=impressao%><br> 
                    
                <%
                    
                }
            }
                %>
              
   </body>
</html>      