<g:if test="${game}">
    <table class="gameboard">
        <g:each in="${(1..game.rows)}" var="r">
            <tr>
                <g:each in="${(1..game.columns)}" var="c">
                    <g:set var="cellClass" value="normalGameboardCell"/>
                    <g:if test="${r==gameState.playerARow && c==gameState.playerAColumn}">
                        <g:set var="cellClass" value="playerAPosition"/>
                    </g:if>
                    <g:elseif test="${r==gameState.playerBRow && c==gameState.playerBColumn}">
                        <g:set var="cellClass" value="playerBPosition"/>
                    </g:elseif>
                    <g:elseif test="${r==gameState.playerAShotRow && c==gameState.playerAShotColumn}">
                        <g:set var="cellClass" value="playerAShot"/>
                    </g:elseif>
                    <g:elseif test="${c==gameState.playerBShotColumn && r==gameState.playerBShotRow}">
                        <g:set var="cellClass" value="playerBShot"/>
                    </g:elseif>

                    <g:if test="${((player == 'A') && (r >= gameState.playerARow-game.ROWS_PLAYER_CAN_MOVE) && (r <= gameState.playerARow+game.ROWS_PLAYER_CAN_MOVE) && (c >= gameState.playerAColumn-game.COLUMNS_PLAYER_CAN_MOVE) && c <= (gameState.playerAColumn+game.COLUMNS_PLAYER_CAN_MOVE)) || ((player == 'B') && (r >= gameState.playerBRow-game.ROWS_PLAYER_CAN_MOVE) && (r <= gameState.playerBRow+game.ROWS_PLAYER_CAN_MOVE) && (c >= gameState.playerBColumn-game.COLUMNS_PLAYER_CAN_MOVE) && c <= (gameState.playerBColumn+game.COLUMNS_PLAYER_CAN_MOVE))}">
                        <td class="${cellClass}"
                            onclick="moveTo('${r}', '${c}')"
                            onmouseout="this.className = '${cellClass}'"
                            onmouseover="this.className = 'cellHover';this.style.cursor = 'pointer'">
                    </g:if>
                    <g:else>
                        <td class="${cellClass}">
                    </g:else>
                    ${r}-${c}
                    </td>
                </g:each>
            </tr>
        </g:each>
    </table>
</g:if>

<script type="text/javascript">
    function moveTo(row, column)
    {
        document.getElementById("row").value = row;
        document.getElementById("column").value = column;
        document.getElementById("player").value = "${player}";
    }
</script>