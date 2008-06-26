<g:form url="[controller:'squid', action:'submitOrder']">
    <input size="2" readonly="true" type="text" id="row" name="row"/>-<input size="2" readonly="true" type="text" id="column" name="column"/><br>
    <input type="radio" name="turnType" value="Move" checked="checked" />Move To<br>
    <input type="radio" name="turnType" value="Fire" />Fire At<br>

    <input type="hidden" id="gameId" name="gameId" value="${gameState?.game?.id}"/>
    <input type="hidden" id="player" name="player"/>
    <g:actionSubmit method="post" value="Submit Order"/>
</g:form>
