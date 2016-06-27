<%@include file="/template.jsp"%>

<div class="col-md-9" id="conteudoPagina">

    <h3 class="page-header"><s:text name="Criação de Oferta" /></h3>

    <div class="container-fluid"> 
        
        <p style="float: right"><s:text name="saldo">Saldo: R$ <s:property value="%{#request.saldo}" /></s:text></p>
        
        <div class="well" >

                    <s:form action="processaOferta" id="criacaoOferta" namespace="/login" theme="bootstrap" cssClass="form-horizontal">

                        <s:select label="Escolha um Personagem" 
                                    headerKey="-1" headerValue="Escolha um Personagem"
                                    list="%{#request.nomePersonagens}" 
                                    name="personagem" />
                        
                     <div style="padding-left: 50px;">
                        <label for="quantidade" ><s:text name="prompt.quantidade" /> </label>
                        <select id="1-20" form="criacaoOferta" name="quantidade" ></select>
                     </div>
                        <s:radio name="tipoOferta" list="{'Oferta de Compra', 'Oferta de Venda'}" /> 
                    <div style="float:right;">   
                        <label for="preco"><s:text name="prompt.offer.value" /></label>
                        <input type="number" min="1.00" step="1.00" max="1000" value="0.00" name="preco"/>

                        <s:submit cssClass="btn btn-primary" key="prompt.create.offer" />
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
        $select.append($('<option></option>').val(i).html(i));
    }
});

$('input[type="number"]').change(function() {
    var min = Globalize.parseFloat($(this).attr("min"));
    var max = Globalize.parseFloat($(this).attr("max"));
    var value = Globalize.parseFloat($(this).val());
    if(value < min)
        value = min;
    if(value > max)
        value = max;
    
    console.log(value);
    $(this).val(value);
});

</script>

<%@include file="/footer.jsp"%>