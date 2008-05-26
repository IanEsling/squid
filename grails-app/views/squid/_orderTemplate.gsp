<div>
    <fieldset>

        <g:if test="${game?.playerAStatus?.equals('waiting')}">
            <legend>Issue Order for ${game?.playerB}</legend>
        </g:if>
        <g:else>
            <legend>Issue Order for ${game?.playerA}</legend>
        </g:else>
        <g:form url="[controller:'squid', action:'submitOrder']">
            MoveTo: <input size="2" type="text" id="row" name="row"/>-<input size="2" type="text" id="column" name="column"/>
            <input type="hidden" id="gameId" name="gameId" value="${game?.id}"/>
            <input type="hidden" id="player" name="player" value="<g:if test="${game?.playerAStatus?.equals('waiting')}">B</g:if><g:else>A</g:else>">
            <g:actionSubmit method="post" value="Submit Order"/>
        </g:form>
    </fieldset>
</div>