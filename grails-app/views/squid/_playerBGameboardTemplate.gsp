<g:if test="${game != null}">
    <table class="gameboard">
        <g:each in="${(1..game.rows)}" var="r">
            <tr>
                <g:each in="${(1..game.columns)}" var="c">
                    <g:if test="${r==gameState.playerRow('B', game) && c==gameState.playerColumn('B', game)}">
                        <td class="playerBPosition">
                    </g:if>
                    <g:else>
                        <g:set var="cellClass" value="normalGameboardCell"/>
                        <g:if test="${r==gameState.playerRow('A', game) && c==gameState.playerColumn('A', game)}">
                            <g:set var="cellClass" value="playerAPosition"/>
                        </g:if>
                        <g:elseif test="${gameState.shotLandedInRow('A', r, game) && gameState.shotLandedInColumn('A', c, game)}">
                            <g:set var="cellClass" value="playerAShot"/>
                        </g:elseif>
                        <g:elseif test="${gameState.shotLandedInRow('B', r, game) && gameState.shotLandedInColumn('B', c, game)}">
                            <g:set var="cellClass" value="playerBShot"/>
                        </g:elseif>

                        <g:if test="${game.playerCanMoveHere('B', r, c)}">
                            <td class="${cellClass}"
                            onclick="moveTo('${r}', '${c}')"
                            onmouseout="this.className = '${cellClass}'"
                            onmouseover="this.className = 'cellHover';this.style.cursor = 'pointer'">
                        </g:if>
                        <g:else>
                            <td class="${cellClass}">
                        </g:else>
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
        document.getElementById("player").value = "B";
    }
</script>