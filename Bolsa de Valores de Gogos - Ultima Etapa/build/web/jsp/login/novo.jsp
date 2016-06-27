<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina" style="margin-left: 30px;">

    <h3 class="page-header"><s:text name="title.account.creation" /></h3>

    <p> <s:text name="instruction.create.account" /></p>

    <div class="container-fluid" id="noWell">  
            <s:form action="salva" namespace="/login" method="post" theme="bootstrap" cssClass="form-horizontal"  >  

                    <s:hidden name="id" value="%{#request.user.id}"/>

                    <s:textfield key="form.label.name" name="nome" value="%{#request.user.nome}" />

                    <s:textfield key="form.label.email" name="email" value="%{#request.user.email}" />

                    <s:textfield type="number" key="form.label.cpf" name="cpf" value="%{#request.user.cpf}" />	

                    <s:textfield type="number" key="form.label.telephone" name="telefone" value="%{#request.user.telefone}" />	

                    <s:textfield  key="form.label.password" name="senhaNova1" type="password" style="width: 178px" />	

                    <s:textfield key="form.label.passwordConfirm" name="senhaNova2" type="password" style="width: 178px" />		

                <div class="formActions">
                    <s:submit key="form.default.submit" cssClass="btn btn-primary" />   
                    <a href="<s:url action="index" namespace="/login" />" class="btn btn-primary" ><s:text name="prompt.cancel" /></a>
                </div>

            </s:form> 
    </div>
</div>  
    
        
            

<%@include file="/footer.jsp"%>