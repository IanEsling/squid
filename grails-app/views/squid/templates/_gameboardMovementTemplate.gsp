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

                    <g:if test="${gameState.playerCanMoveHere(r, c, player)}">
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