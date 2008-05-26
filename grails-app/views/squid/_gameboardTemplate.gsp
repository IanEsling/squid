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

                    <g:else>
                    <td onmouseout="this.className = 'gameboard'"
                            onmouseover="this.className = 'cellHover';
                            this.style.cursor = 'pointer'">
                        </g:else>
                        ${r}-${c}
                    </td>
                </g:each>
                </tr>
            </g:each>
        </table>
    </g:if>