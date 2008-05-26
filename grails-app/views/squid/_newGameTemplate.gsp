<div>
    <fieldset>
        <legend>New Game</legend>
        <g:form url="[controller:'squid', action:'newGame']">
            Rows: <input type="text" id="rows" name="rows">
            Columns: <input type="text" id="columns" name="columns">
            Player A: <input type="text" id="playerA" name="playerA">
            Player B: <input type="text" id="playerB" name="playerB">

            <g:actionSubmit method="post" value="New Game"/>
        </g:form>
    </fieldset>
</div>