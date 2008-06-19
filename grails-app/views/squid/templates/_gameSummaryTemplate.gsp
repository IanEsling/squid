<span class="summary">
    <g:if test="${game}">
        <g:if test="${!gameState.gameOver}">
            <div class="summaryGameNumber">current game is number ${game.id}, turn number ${gameState.turnNumber}</div>
            <div class="summaryBoardSize">board size: ${game.rows} x ${game.columns}</div>
            <g:each in="${game.players}" var="p">
            <div class="summaryPlayerA">player : ${p.name} - <g:if test="${gameState.player(p.name).get('status').equals('waiting')}">Orders Received</g:if>
                <g:else><a href="/squid/squid/move/${p.name}">Issue Orders</a></g:else></div>
                </g:each>
        </g:if>
        <g:else>
            <div class="gameOver">Game Over<br>
                <g:if test="${gameState.winner=='Draw'}">
                    The game is a draw.
                </g:if>
                <g:elseif test="${gameState.winner=='PlayerA'}">
                    The winner is ${game.playerA}
                </g:elseif>
                <g:elseif test="${gameState.winner=='PlayerB'}">
                    The winner is ${game.playerB}
                </g:elseif>
            </div>
        </g:else>
    </g:if>
</span>