<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina">
    
    <h3 class="page-header"><s:text name="title.change.password" /></h3>

    <p> <s:text name="instructions.change.password" /></p>

    <div class="container-fluid"> 
        <div class="well" >

                    <s:form action="trocaSenha" namespace="/login" method="post" theme="bootstrap" cssClass="form-horizontal" >

                            <s:textfield key="form.label.email" name="email" size="32" />

                            <s:textfield key="form.label.oldPassword" type="password" name="senhaAntiga" size="32" />

                            <s:textfield key="form.label.newPassword1" type="password" name="senhaNova1" size="32" />

                            <s:textfield key="form.label.newPassword2" type="password" name="senhaNova2" size="32" />

                        <div class="formActionsPassword">  
                            <s:submit cssClass="btn btn-primary" key="prompt.change.password" />
                            <a href="<s:url action="index" namespace="/login"><s:param name="user" value="%{#request.user}" /> 
                                      </s:url>" class="btn btn-primary" ><s:text name="prompt.cancel" />
                           </a>
                        </div>

                </s:form>
           
       </div>
    </div>
 </div>         
    
<%@include file="/footer.jsp"%>
