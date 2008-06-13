<g:if test="${game}">
    <table class="gameboard">
        <g:each in="${(1..game.rows)}" var="r">
            <tr>
                <g:each in="${(1..game.columns)}" var="c">
                    <g:if test="${r==gameState.playerARow && c==gameState.playerAColumn}">
                        <td class="playerAPosition">
                    </g:if>
                    <g:elseif test="${r==gameState.playerBRow && c==gameState.playerBColumn}">
                        <td class="playerBPosition">
                    </g:elseif>
                    <g:elseif test="${r==gameState.playerAShotRow && c==gameState.playerAShotColumn}">
                        <td class="playerAShot">
                    </g:elseif>
                    <g:elseif test="${r==gameState.playerBShotRow && c==gameState.playerBShotColumn}">
                        <td class="playerBShot">
                    </g:elseif>
                    <g:else>
                        <td class="normalGameboardCell">
                    </g:else>
                    ${r}-${c}
                    </td>
                </g:each>
            </tr>
        </g:each>
    </table>
</g:if>