<span class="summary">
    <g:if test="${game}">
        <g:if test="${!gameState.gameOver}">
            <div class="summaryGameNumber">current game is number ${game.id}, turn number ${gameState.turnNumber}</div>
            <div class="summaryPlayerA">player A: ${game.playerA} - <g:if test="${gameState.playerAStatus.equals('waiting')}">Orders Received</g:if>
                <g:else><a href="/squid/squid/move/A">Issue Orders</a></g:else></div>
            <div class="summaryPlayerB">player B: ${game.playerB} - <g:if test="${gameState.playerBStatus.equals('waiting')}">Orders Received</g:if>
                <g:else><a href="/squid/squid/move/B">Issue Orders</a></g:else></div>
            <div class="summaryBoardSize">board size: ${game.rows} x ${game.columns}</div>
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