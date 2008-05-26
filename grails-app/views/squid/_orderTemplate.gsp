<div>
    <fieldset>
        <legend>Issue Order</legend>
        <g:form url="[controller:'squid', action:'order']">
            MoveTo: <input size="2" type="text" id="row" name="row"/>-<input size="2" type="text" id="column" name="column"/>
            <input type="hidden" id="gameId" name="gameId" value="${game?.id}"/>
            <input type="hidden" id="player" name="player" value="A"/>
            <g:actionSubmit method="post" value="Submit Order"/>
        </g:form>
    </fieldset>
</div>