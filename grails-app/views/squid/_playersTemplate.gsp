<span class="players">
    <g:if test="${game != null}">
        current game is: number ${game?.id}
        players: ${game?.playerA} v ${game?.playerB}
        board size: ${game.rows} x ${game.columns}
    </g:if>
</span>