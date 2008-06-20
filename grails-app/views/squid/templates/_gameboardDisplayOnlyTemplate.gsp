<g:if test="${game}">
    <table class="gameboard">
        <g:each in="${(1..game.rows)}" var="r">
            <tr>
                <g:each in="${(1..game.columns)}" var="c">
                    <g:set var="cellClass" value="normalGameboardCell"/>
                    <g:if test="${gameState.anyoneThere(r,c)}">
                        <g:set var="cellClass" value="player${gameState.playerHere(r,c)}Position"/>
                    </g:if>
                    <g:elseif test="${gameState.aShotHere(r,c)}">
                        <g:set var="cellClass" value="player${gameState.playerShotHere(r,c)}Shot"/>
                    </g:elseif>
                    <td class="${cellClass}">
                    ${r}-${c}
                    </td>
                </g:each>
            </tr>
        </g:each>
    </table>
</g:if>