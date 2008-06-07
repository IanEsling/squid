<span class="summary">
    <g:if test="${game != null}">
        <g:if test="${game.gameOver == false}">
            <div class="summaryGameNumber">current game is number ${game?.id}, turn number ${game?.turnNumber}</div>
            <div class="summaryPlayerA">player A: ${game?.playerA} - <g:if test="${game?.playerAStatus?.equals('waiting')}">Orders Received</g:if>
                <g:else><a href="/squid/squid/playerA">Issue Orders</a></g:else></div>
            <div class="summaryPlayerB">player B: ${game?.playerB} - <g:if test="${game?.playerBStatus?.equals('waiting')}">Orders Received</g:if>
                <g:else><a href="/squid/squid/playerB">Issue Orders</a></g:else></div>
            <div class="summaryBoardSize">board size: ${game.rows} x ${game.columns}</div>
        </g:if>
        <g:else>
            <div class="gameOver">Game Over<br>
                <g:if test="${game.winner=='Draw'}">
                    The game is a draw.
                    </g:if>
                <g:else>
                    The winner is ${game.winner}
                </g:else>            
            </div>
        </g:else>
    </g:if>
</span>