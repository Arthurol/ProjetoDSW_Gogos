<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina">
    
    <h3 class="page-header"><s:text name="prompt.edit.info"/></h3>

    <p><s:text name="instructions.update.info" /> </p>        

    <div class="container-fluid" id="noWell">  

                <div class="infoPerfil">
                    <p> <s:text name="prompt.name" /> &nbsp; <s:property value="%{#request.user.nome}" />
                    <p> <s:text name="prompt.cpf" /> &nbsp; <s:property value="%{#request.user.cpf}" />
                    <p> <s:text name="prompt.telephone" /> &nbsp; <s:property value="%{#request.user.telefone}" />
                    <br>
                </div>

                <s:form action="atualiza" namespace="/login" method="post" theme="bootstrap" cssClass="form-horizontal"> 

                        <s:hidden name="id" value="%{#request.user.id}"/>

                        <s:textfield key="form.label.name" name="nome" value="%{#request.user.nome}" />		

                        <s:textfield type="number" key="form.label.cpf" name="cpf" value="%{#request.user.cpf}" />	

                        <s:textfield type="number" key="form.label.telephone" name="telefone" value="%{#request.user.telefone}"  />	

                    <div class="formActions">
                        <s:submit cssClass="btn btn-primary" key="prompt.edit.info" />  
                        <a href="<s:url action="index" namespace="/login" />" class="btn btn-primary"><s:text name="prompt.cancel" /></a>
                    </div>
                </s:form> 

    </div>
</div>
                    
<%@include file="/footer.jsp"%>