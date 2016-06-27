<%@include file="/template.jsp"%>

    <div class="col-md-9" id="conteudoPagina">

        <h3 class="page-header"><s:text name="Exibição de Ofertas" /></h3>

            <s:if test ="%{#request.listaOfertas != null}" >
                                
                                <s:iterator value="%{#request.listaOfertas}">
                                    <s:set var="statOff"><s:property value="statusOferta"/></s:set>
                                    
                                    <b><s:text name="Oferta de "/><s:property value="tipoOferta"/>
                                        <s:text name="id: "/><s:property value="id"/>
                                    <s:text name=", Status: "/><s:property value="statusOferta"/></b>
                                    
                                    <s:if test ="%{#statOff == #request.aberta}" ><a href="
                                                                <s:url action="removeOferta" namespace="/login">
                                                                    <s:param name="idOfertaRm" value="id"/></s:url>">
                                                                        &nbsp; Remover Oferta</a></s:if><br>
                                    
                                    <s:text name="Id do Personagem: "/> <s:property value="idPersonagem"/>     
                                    <s:text name=", Quantidade: "/> <s:property value="quantidade"/>  
                                    <s:text name=", Preço da Oferta: RS "/><s:property value="precoUnitario"/><s:text name="0"/> <br>
                                    <s:text name="Registrada em: "/><s:property value="data"/> <br><br>
                                    
                                </s:iterator>
                </s:if>
    </div>
        
<%@include file="/footer.jsp"%>