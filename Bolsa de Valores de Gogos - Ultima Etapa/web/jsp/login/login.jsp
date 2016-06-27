<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina" style="margin-left: 30px;">
    
    <h3 class="page-header"> <s:text name="prompt.login"/></h3>
 
    <div class="container-fluid" id="customizado">
        <div class="well" >
            <div class="formLogin">
                
                <s:form action="log" namespace="/login" method="log" theme="bootstrap" cssClass="form-horizontal">   
                    
                                <s:textfield key="prompt.email" name="email" />
                            
                                <s:textfield key="prompt.password" name="senhaLogin" type="password" /> 
                            
                            <div class="loginActions">
                                <s:submit class="btn btn-primary" value="Login" />
                                <a href="<s:url action="preparaEnvioToken" namespace="/login"/>" class="btn"><s:text name="prompt.forgot.password"/></a>
                                <a href="<s:url action="novo" namespace="/login" />" class="btn"><s:text name="prompt.create.account" /></a>
                            </div>
                   
                </s:form>

            </div>
        </div>
    </div>

    <div class="container-fluid">
        
            <h3>Sistema CRUD com login</h3>

            <p>Bem-vindo ao sistema CRUD com login desenvolvido usando o framework STRUTS.</p>

            <p>Desenvolvedores do sistema CRUD com login</p>
 
    </div>
    
</div>
    
<%@include file="/footer.jsp"%>