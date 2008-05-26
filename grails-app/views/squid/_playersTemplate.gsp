<span class="players">
    <g:if test="${game != null}">
        current game is: number ${game?.id}<br>
        players: ${game?.playerA} v ${game?.playerB}<br>
        board size: ${game.rows} x ${game.columns}<br>
    </g:if>
</span>