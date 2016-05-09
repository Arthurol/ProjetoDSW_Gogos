<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <style>
        div1 {width:40%; float:left;}    
        div1 {width:40%; float:right;}     
        </style>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolsa de Valores de Gogos - Teste do BD</title>
    </head>
    <body>
        
        <header>
            <a style="color:blue" href="index.jsp">Home</a>
        </header>
        
        <h1 style="text-align:center"> Bolsa de Valores de Gogos</h1>
        
        <h2 style="text-align:center">Escolha uma operação:</h2>
        
        <div style="margin-left: 40%">
            <div style ="witdth:100px; float:left;">
                   <p>
                       <a href="listaUsuarios.jsp"><button> Obter Usuários</button></a>
                   </p>
                   
                   <p>
                       <a href="cadastro.jsp"><button> Adicionar Usuário</button></a>
                   </p>

                   <p>
                       <a href="remocao.jsp"><button> Remover Usuários</button></a>
                   </p>
            </div>  
            
            <div style ="witdth:100px; float:left; margin-left:10%">
                   <p>
                       <a href="addSaldo.jsp"><button>Adicionar Saldo</button></a>
                   </p>
                   
                   <p>
                       <a href="criarOferta.jsp"><button>Criar Oferta de Compra</button></a>
                   </p>

                   <p>
                       <a href="extrato.jsp"><button>Extrato(Dinheiro)</button></a>
                   </p>
            </div> 
        </div>  
        
    </body>
</html>
    