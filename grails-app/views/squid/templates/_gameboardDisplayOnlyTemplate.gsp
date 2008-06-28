<g:if test="${gameState.game}">
    <table class="gameboard">
        <g:each in="${(1..gameState.game.rows)}" var="r">
            <tr>
                <g:each in="${(1..gameState.game.columns)}" var="c">
                    <g:set var="cellClass" value="normalGameboardCell"/>
                    <g:if test="${gameState.anyoneThere(r,c)}">
                        <g:set var="cellClass" value="player${gameState.whichPlayerHere(r,c)}Position"/>
                    </g:if>
                    <g:elseif test="${gameState.aShotHere(r,c)}">
                        <g:set var="cellClass" value="player${gameState.whichPlayerShotHere(r,c)}Shot"/>
                    </g:elseif>
                    <td class="${cellClass}">
                    ${r}-${c}
                    </td>
                </g:each>
            </tr>
        </g:each>
    </table>
</g:if>