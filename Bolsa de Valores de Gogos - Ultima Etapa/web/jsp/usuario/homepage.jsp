<%@include file="/template.jsp"%>

        <div class="col-md-9" id="conteudoPagina">
                <h3 class="page-header"><s:text name="prompt.homepage" /></h3>
                
                <s:if test ="%{#request.extratoDinheiro != null}" >
                    <s:text name="%{#request.extratoDinheiro}" />
                </s:if>
                
                <s:if test ="%{#request.extratoPersonagens != null}" >
                    <s:text name="%{#request.extratoPersonagens}" />
                </s:if>
                
                
        </div>                
            
<%@include file="/footer.jsp"%>