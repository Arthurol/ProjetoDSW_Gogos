<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina">

    <h3 class="page-header"><s:text name="Adição de Personagens" /></h3>

    <div class="container-fluid"> 
        <div class="well">
                
                <s:form action="adicionaPersonagem" id="escolhaPersonagem" namespace="/login" theme="bootstrap" cssClass="form-horizontal">

                    <s:select label="Escolha um Personagem" 
                                headerKey="-1" headerValue="Escolha um Personagem"
                                list="%{#request.nomePersonagens}" 
                                name="personagem" />
                    <div style="padding-left: 50px;">
                        <label for="quantidade" ><s:text name="prompt.quantidade" /> </label>
                        <select id="1-20" form="escolhaPersonagem" name="quantidade" ></select>
                    </div>
                    <div style="float:right;">   
                        <s:submit cssClass="btn btn-primary" key="prompt.add.gogo"  />
                        <a href="<s:url action="index" namespace="/login" />" class="btn btn-primary"><s:text name="prompt.cancel" /></a> 
                    </div> 
                </s:form>
                    
        </div>
    </div>
</div>

<script type="text/javascript">
$(function(){
    var $select = $("#1-20");
    for (i=1;i<=20;i++){
        $select.append($('<option></option>').val(i).html(i))
    }
});
</script>

<%@include file="/footer.jsp"%>
