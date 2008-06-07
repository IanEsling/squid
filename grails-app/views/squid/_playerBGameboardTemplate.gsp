<g:if test="${game != null}">
    <table class="gameboard">
        <g:each in="${(1..game.rows)}" var="r">
            <tr>
                <g:each in="${(1..game.columns)}" var="c">
                    <g:if test="${r==game.playerRow('A') && c==game.playerColumn('A')}">
                        <td class="playerAPosition">
                    </g:if>
                    <g:elseif test="${r==game.playerRow('B') && c==game.playerColumn('B')}">
                        <td class="playerBPosition">
                    </g:elseif>

                    <g:elseif test="${(r==game.playerRow('B') && (game.playerColumn('B')-1..game.playerColumn('B')+1).contains(c))}">
                        <td onclick="moveTo('${r}', '${c}')" onmouseout="this.className = 'gameboard'"
                            onmouseover="this.className = 'cellHover';
                            this.style.cursor = 'pointer'">
                    </g:elseif>
                    <g:elseif test="${(c==game.playerColumn('B') && (game.playerRow('B')-1..game.playerRow('B')+1).contains(r))}">
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
    }
</script>