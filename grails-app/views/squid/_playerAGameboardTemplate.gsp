<g:if test="${game}">
    <table class="gameboard">
        <g:each in="${(1..game.rows)}" var="r">
            <tr>
                <g:each in="${(1..game.columns)}" var="c">
                    <g:if test="${r==game.playerRow('A') && c==game.playerColumn('A')}">
                        <td class="playerAPosition">
                    </g:if>
                    <g:else>
                        <g:set var="cellClass" value="normalGameboardCell"/>
                        <g:if test="${r==game.playerRow('B') && c==game.playerColumn('B')}">
                            <g:set var="cellClass" value="playerBPosition"/>
                        </g:if>
                        <g:elseif test="${game.shotLandedInRow('A', r) && game.shotLandedInColumn('A', c)}">
                            <g:set var="cellClass" value="playerAShot"/>
                        </g:elseif>
                        <g:elseif test="${game.shotLandedInRow('B', r) && game.shotLandedInColumn('B', c)}">
                            <g:set var="cellClass" value="playerBShot"/>
                        </g:elseif>

                        <g:if test="${game.playerCanMoveHere('A', r, c)}">
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
        document.getElementById("player").value = "A";
    }
</script>