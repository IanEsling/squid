<span>
    <g:if test="${game != null}">
        <table class="gameboard">
            <g:each in="${(0..game.rows)}" var="r">
                <tr>
                <g:each in="${(0..game.columns)}" var="c">
                    <td onmouseout="this.className = 'gameboard'"
                            onmouseover="this.className = 'cellHover';
                            this.style.cursor = 'pointer'">
                        ${r}-${c}
                    </td>
                </g:each>
                </tr>
            </g:each>
        </table>
    </g:if>
</span>