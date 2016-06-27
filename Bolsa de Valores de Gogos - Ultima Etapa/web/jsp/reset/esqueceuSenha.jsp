<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina" style="margin-left: 30px;">

    <h3 class="page-header"><s:text name="title.reset.password" /></h3>

    <p><s:text name="instructions.token.request" /></p>

    <div class="container-fluid" id="customizado">  
                <div class="well">
                    
                        <s:form action="envioToken" namespace="/login" method="post" theme="bootstrap" cssClass="form-horizontal">    

                                <s:textfield key="form.label.email" name="email" />	

                                <div class="formActions">
                                    <s:submit cssClass="btn btn-primary" key="form.receive.token" />
                                    <a href="<s:url action="index" namespace="/login" />" class="btn btn-primary" ><s:text name="prompt.cancel" /></a>
                                </div>

                        </s:form>
                   
                </div>
    </div>
    
</div>

<%@include file="/footer.jsp"%>