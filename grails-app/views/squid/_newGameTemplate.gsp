<div>
    <fieldset>
        <legend>New Game</legend>
        <g:form url="[controller:'squid', action:'newGame']">
            <label for="rows">Rows:</label><input type="text" id="rows" name="rows">
            <label for="columns">Columns:</label><input type="text" id="columns" name="columns">
            <label for="playerA">Player A Name:</label><input type="text" id="playerA" name="playerA">
            <label for="playerB">Player B Name:</label><input type="text" id="playerB" name="playerB">

            <g:actionSubmit method="post" value="New Game"/>
        </g:form>
    </fieldset>
</div>