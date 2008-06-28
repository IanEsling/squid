<span class="summary">
    <g:if test="${gameState?.game}">
        <g:if test="${!gameState.gameOver}">
            <div class="summaryGameNumber">current game is number ${gameState.game.id}, turn number ${gameState.turnNumber}</div>
            <div class="summaryBoardSize">board size: ${gameState.game.rows} x ${gameState.game.columns}</div>
            <g:each status="i" in="${gameState.playerStates}" var="p">
            <div class="summaryPlayer${i}">player : ${p.playerName} - <g:if test="${p.status.equals('waiting')}">Orders Received</g:if>
                <g:else><a href="/squid/squid/move/${p.playerName}">Issue Orders</a></g:else></div>
                </g:each>
        </g:if>
        <g:else>
            <div class="gameOver">Game Over<br>
                ${gameState.declareWinner()}
            </div>
        </g:else>
    </g:if>
</span>