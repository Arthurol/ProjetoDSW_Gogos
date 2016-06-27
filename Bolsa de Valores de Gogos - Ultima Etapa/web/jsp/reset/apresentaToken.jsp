<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina">
    
    <h3 class="page-header"><s:text name="title.reset.password" /></h3>

    <p> <s:text name="prompt.token" /><br><s:property value="%{#request.token}"/> </p>

    <div class="container-fluid">  
        
            <div class="well">

                    <s:form action="verificaTokenSenha" namespace="/login" method="post" theme="bootstrap" cssClass="form-horizontal">

                           <s:textfield key="form.label.token" name="token" />
                           <s:textfield key="prompt.email" name="email" value="%{#request.email}"/>


                       <div class="formActions">
                           <s:submit cssClass="btn btn-primary" key="form.default.submit" />
                           <a href="<s:url action="index" namespace="/login" />" class="btn btn-primary"><s:text name="prompt.cancel" /></a>
                       </div>
                   </s:form>

            </div>
    </div>
    
</div>

<%@include file="/footer.jsp"%>