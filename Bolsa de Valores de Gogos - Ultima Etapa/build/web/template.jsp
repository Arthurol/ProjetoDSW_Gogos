<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sb" uri="/struts-bootstrap-tags" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@taglib uri="/WEB-INF/crud.tld" prefix="crud"%>

<%
    response.setHeader ("Cache-Control", "no-cache");
    response.setHeader ("Pragma", "no-cache");
    response.setDateHeader ("Expires", 0); 
%>


<html lang="en">
<head>
        <title><s:text name="app.title"/></title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="Sistema construído com o framework STRUTS">
	<meta name="author" content="Arthur Lopes">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilo.css" />
        <!-- Bootstrap -->
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.min.css.map" />
</head>

<body>
        <!-- jQuery -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/globalize.js"></script>
        
    <nav class="navbar navbar-inverse navbar-fixed-top">
       <div class="container-fluid">
           <div class="navbar-header">
               <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-controls="navbar">
                   <span class="sr-only">Toggle navigation</span>
                   <span class="icon-bar"></span>
                   <span class="icon-bar"></span>
                   <span class="icon-bar"></span>
               </button>
               <a class="navbar-brand" href="<s:url action="index" namespace="/login" />"><s:text name="app.title" /></a>
           </div>

           <div id="navbar" class="navbar-collapse collapse">
               <ul class="nav navbar-nav navbar-left">

                    <s:if test ="%{#session.user != null}" >
                        <li><a style="color:white"> <s:text name="header.greeting" /> <s:property value="%{#session.user.nome}" />! </a></li>
                        
                        <li><a href="<s:url action="logout" namespace="/login" />"><s:text name="prompt.logout" /></a></li>
                    </s:if>
                    <s:else>
                        <li><a href="<s:url action="index" namespace="/login" />"><s:text name="prompt.login" /></a></li>
                    </s:else>
               </ul>
           </div>
       </div>
   </nav>       
    
    <div id="main" class="container-fluid">          
           
        <div class="container-fluid">

                <div class="navbar-collapse collapse">
                        
                             
                        
                        <ul class="nav">
                        </ul>
                </div>

        </div>
    
        <div class="container-fluid">
                <div class="row-fluid">
                        <div id="pnSidebar">
                        </div>

                        <div id="pnCentral">
                            
                            <s:if test ="%{#request.alerta != null}" > 
                                
                                <div class="alert alert-danger" id="alertaErro">
                                    <p style="font-size: 18px">
                                        <s:property value="%{#request.alerta}"></s:property>
                                    </p>
                                </div>
                                    
                            </s:if>
                            
                            <s:if test ="%{#request.alerta2 != null}" > 
                                
                                <div class="alert alert-info" id="alertaErro">
                                    <p style="font-size: 18px">
                                        <s:property value="%{#request.alerta2}"></s:property>
                                    </p>
                                </div>
                                    
                            </s:if>

                                <div class="row-fluid notice" id="pnAviso">

                                </div>

                                <div class="row-fluid" id="pnContent">
                                </div>
                        </div>
                </div>
        </div>
    </div>
            
        <div class="container-fluid" ">
            <div class="row" id="conteudo">
                
                <s:if test ="%{#session.user != null}" >    
                   <div class="col-md-3" style="margin-top: 30px;">
                        <div class="well sidebar-nav" style="width:100%;" >
                            
                            <ul class="nav nav-list" style="text-align: center; font-size: 14px; text-transform: uppercase;">
                               
                                    <li> <a href="<s:url action="index" namespace="/login" />"><s:text name="prompt.home" /></a> </li> <br>
                
                                    <li> <a href="<s:url action="preparaEdicaoUsuario" namespace="/login" />">
                                            <s:text name="prompt.edit.info" /> </a> </li> <br>
    
                                    <li><a href="<s:url action="preparaTrocaSenha" namespace="/login" />">    
                                              <s:text name="prompt.change.password" /> </a> <br>     
                                              
                                    <li> <a href="<s:url action="preparaAdicaoPersonagem" namespace="/login" />">
                                        <s:text name="prompt.add.gogo" /> </a> </li> <br>

                                    <li> <a href="<s:url action="preparaCriacaoOferta" namespace="/login" />">
                                        <s:text name="prompt.add.offer" /> </a> </li> <br>

                                    <li> <a href="<s:url action="imprimeExtratoDinheiro" namespace="/login" />">
                                        <s:text name="prompt.check.moneyExtract" /> </a> </li> <br>

                                    <li> <a href="<s:url action="imprimeExtratoPersonagens" namespace="/login" />">
                                            <s:text name="prompt.check.gogoExtract" /> </a> </li> <br>
                                    
                                    <li> <a href="<s:url action="exibeOfertas" namespace="/login" />">
                                            <s:text name="prompt.check.allOffers" /> </a> </li> <br>
                            </ul>
                        </div>
                   </div>
                </s:if>
                
            </div>
        </div>
        