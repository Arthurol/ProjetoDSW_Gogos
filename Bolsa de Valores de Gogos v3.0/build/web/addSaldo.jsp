<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="bolsaGogos.model.DAO.TransferenciaDAO"%>
<%@page import="bolsaGogos.model.Transferencia"%>
<%@page import="bolsaGogos.model.DAO.UsuarioDAO"%>
<%@page import="bolsaGogos.model.Usuario"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Adição de Saldo</title>
    </head>
    <body>
         
        <header>
            <a style="color:blue" href="index.jsp">Home</a>
        </header>
        
        <h1 style="text-align:center"> Bolsa de Valores de Gogos</h1>
        
        <br><h2>Credito de Dinheiro</h2>
            
        <%  
            Transferencia transf = new Transferencia();
            TransferenciaDAO transfDAO = new TransferenciaDAO();
            UsuarioDAO userDAO = new UsuarioDAO();
            
            
            int i = 0, j = 0;
            for(i = 1, j = 1; i <= 300 && j <= 3; i++){
   
                
                if(userDAO.getUsuario(i)!= null) {

                %> 
                    <p> Usuário <%=i%> encontrado!</p>

                <%
                    String conBanAg = Integer.toString(i);
                    transf.setIdUsuario(i);
                    transf.setValor(1000.00);
                    transf.setAgenciaOrigem(conBanAg);
                    transf.setBancoOrigem(conBanAg);
                    transf.setContaOrigem(conBanAg);

                    if(transfDAO.creditoDinheiro(transf) == true){
        %>          
        
                        <p>O credito de R$ 1000,00 para o usuário de id <%=i%> foi realizado com sucesso.</p><br>
        
            <%      
                    j++;
                    } 
                }
            }
                if (i > 302){
                %> 
                    <p>Não foi possível realizar a transferência, número de tentativas extrapoladas(<%=i%>).
        
                <%
                   
                }
            %> 
            
   
   </body>
</html>      