<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina" style="margin-left: 30px;">

    <h3 class="page-header"><s:text name="title.reset.password" /></h3>

        <p><s:text name="instructions.reset.password" /></p>

    <div class="container-fluid" id="customizado" >  
                <div class="well">
                    <div class="reducedSize"> 

                        <s:form action="executaResetSenha" namespace="/login" method="post" theme="bootstrap" cssClass="form-horizontal">

                                <s:textfield type="hidden" name="email" value="%{#request.email}" />

                                <s:textfield type="hidden"  name="token" value="%{#request.token}" />

                                <s:textfield key="form.label.newPassword1" type="password" name="senhaNova1" size="32" />

                                <s:textfield key="form.label.newPassword2" type="password" name="senhaNova2" size="32" />

                            <div class="formActions">       
                                <s:submit cssClass="btn btn-primary"key="prompt.change.password" />
                                <a href="<s:url action="index" namespace="/login" />" class="btn btn-primary"><s:text name="prompt.cancel" /></a>
                            </div> 
                        </s:form>
                    </div>
                </div>
    </div>
</div>
        
<%@include file="/footer.jsp"%>

