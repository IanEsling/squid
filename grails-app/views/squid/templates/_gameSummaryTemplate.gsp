<span class="summary">
    <g:if test="${gameState.setTestGame}">
        <g:if test="${!gameState.gameOver}">
            <div class="summaryGameNumber">current game is number ${gameState.gameId}, turn number ${gameState.turnNumber}</div>
            <div class="summaryBoardSize">board size: ${gameState.setTestGame.rows} x ${gameState.setTestGame.columns}</div>
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