<g:if test="${game != null}">
    <table class="gameboard">
        <g:each in="${(1..game.rows)}" var="r">
            <tr>
                <g:each in="${(1..game.columns)}" var="c">
                    <g:if test="${r==game.playerRow('A') && c==game.playerColumn('A')}">
                        <td class="playerAPosition" onclick="moveTo('${r}', '${c}')"
                            onmouseout="this.className = 'playerAPosition'"
                            onmouseover="this.className = 'cellHover';
                            this.style.cursor = 'pointer'">
                    </g:if>
                    <g:elseif test="${r==game.playerRow('B') && c==game.playerColumn('B')}">
                        <td class="playerBPosition">
                    </g:elseif>

                    <g:elseif test="${game.playerCanMoveHere('B', r, c)}">
                        <td onclick="moveTo('${r}', '${c}')" onmouseout="this.className = 'gameboard'"
                            onmouseover="this.className = 'cellHover';
                            this.style.cursor = 'pointer'">
                    </g:elseif>
                    <g:else>
                        <td>
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