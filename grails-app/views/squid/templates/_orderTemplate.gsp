<g:form name="orders">
    <input size="2" readonly="true" type="text" id="row" name="row"/>-<input size="2" readonly="true" type="text" id="column" name="column"/><br>
    <input type="radio" id="move" name="turnType" value="Move" onclick="makeMove()" ${turnType=='Move'?"checked='checked'":""} />Move To<br>
    <input type="radio" id="fire" name="turnType" value="Fire" onclick="makeFire()" ${turnType=='Fire'?"checked='checked'":""} />Fire At<br>

    <input type="hidden" id="gameId" name="gameId" value="${gameState?.game?.id}"/>
    <input type="hidden" id="player" name="player" value="${player}"/>
    <input type="submit" name="Submit Order" onclick="submitOrder()"/>
</g:form>

<script type="text/javascript">
    function makeMove()
    {
        document.getElementById("move").checked=true;
        clearRowAndColumn();
    }

    function makeFire()
    {
        document.getElementById("fire").checked=true;
        clearRowAndColumn();
    }

    function clearRowAndColumn()
    {
        document.getElementById('row').value = "";
        document.getElementById('column').value = "";
        document.forms.orders.action = "/squid/squid/move/${player}"
        document.forms.orders.submit();
    }

    function submitOrder()
    {
//        if (document.getElementById('row')=="" || document.getElementById('column')=="") return;
        document.forms.orders.action = "/squid/squid/submitOrder"
        document.forms.orders.submit();
    }
</script>
