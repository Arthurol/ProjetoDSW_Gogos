<%@page import="java.util.ArrayList"%>
<%@page import="bolsaGogos.model.Oferta"%>
<%@page import="bolsaGogos.model.DAO.OfertaDAO"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        
        <br><h2>Inserção de oferta de compra no banco</h2>
            
        <%  
            OfertaDAO ofertaCom = new OfertaDAO();
            UsuarioDAO userDAO = new UsuarioDAO();
            
            int i = 0, j = 0;
            for(i = 1, j = 1; i <= 300 && j <= 3; i++){
                
                if(userDAO.getUsuario(i)!= null) {

                    int idPersonagem = (int)(Math.random() * 63);
                    int idUsuario = i;
                    int qtde = (int)(Math.random() * 11) + 1;
                    double preco = (int)(Math.random() * 110) + 1;

                    Oferta compra = new Oferta(idPersonagem, idUsuario, qtde, preco);

                    if(ofertaCom.insereOfertaCompra(compra) == 0){

            %> 
                        <h3>Oferta de compra inserida com sucesso!!</h3>
                        <p>Registrada oferta para o personagem de id <%=idPersonagem%>, id do usuário: <%=idUsuario%>, quantidade: <%=qtde%> e preço: <%=preco%>  </p><br> 
                        
            <%
                    j++;
                    }else{
 
            %>
             
                        <h3>A oferta não pôde ser inserida.</h3>
                        <br><br>
        <%
                    }
                }
            }
            %>  
         
            <h2>Recuperação de todos as ofertas  do banco</h2>
        <%
            Oferta cobaia = new Oferta();
            List<Oferta> lista = new ArrayList<Oferta>();
            lista = ofertaCom.getListaOfertas();
            
            if (lista!=null){
            
                for(int g = 0; g < lista.size(); g++){
                    cobaia = lista.get(g);
            %>
                <p>Oferta <%=cobaia.getId()%>:</p>  

                <p>Id Usuário:<%=cobaia.getIdUsuario()%>, Id Personagem: <%=cobaia.getIdPersonagem()%>, Quantidade: <%=cobaia.getQuantidade()%>, Preço Unitário: <%=cobaia.getPrecoUnitario()%>.</p>
                <br>
        <%
                }
            }else{
            %>
            
            <p> Lista vazia!</p>
            <%
            }
            %>
              
   </body>
</html>      