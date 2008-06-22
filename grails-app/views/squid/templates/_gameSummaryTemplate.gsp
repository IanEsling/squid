<span class="summary">
    <g:if test="${game}">
        <g:if test="${!gameState.gameOver}">
            <div class="summaryGameNumber">current game is number ${game.id}, turn number ${gameState.turnNumber}</div>
            <div class="summaryBoardSize">board size: ${game.rows} x ${game.columns}</div>
            <g:each status="i" in="${gameState.players}" var="p">
            <div class="summaryPlayer${i}">player : ${p.name} - <g:if test="${p.status().equals('waiting')}">Orders Received</g:if>
                <g:else><a href="/squid/squid/move/${p.name}">Issue Orders</a></g:else></div>
                </g:each>
        </g:if>
        <g:else>
            <div class="gameOver">Game Over<br>
                ${gameState.declareWinner()}
            </div>
        </g:else>
    </g:if>
</span>