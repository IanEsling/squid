<div>
    <fieldset>
        <legend>New Game</legend>
        <g:renderErrors bean="${game}"/>
        <g:form url="[controller:'squid', action:'newGame']">
            Rows: <div class='value ${hasErrors(bean: game, field: 'rows', 'errors')}'>
            <input type="text" id="rows" name="rows" value="${fieldValue(bean: game, field: 'rows')}">
        </div>
            Columns: <div class='value ${hasErrors(bean: game, field: 'columns', 'errors')}'>
            <input type="text" id="columns" name="columns" value="${fieldValue(bean: game, field: 'columns')}">
            </div>
            Player A: <div class='value ${hasErrors(bean: game, field: 'playerA', 'errors')}'>
            <input type="text" id="playerA" name="playerA" value="${fieldValue(bean: game, field: 'playerA')}">
            </div>
            Player B: <div class='value ${hasErrors(bean: game, field: 'playerB', 'errors')}'>
            <input type="text" id="playerB" name="playerB" value="${fieldValue(bean: game, field: 'playerB')}">
            </div>

            <g:actionSubmit method="post" value="New Game"/>
        </g:form>
    </fieldset>
</div>