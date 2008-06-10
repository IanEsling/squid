<span class="summary">
    <g:if test="${game}">
        <g:if test="${!game.gameOver}">
            <div class="summaryGameNumber">current game is number ${game.id}, turn number ${game.turnNumber}</div>
            <div class="summaryPlayerA">player A: ${game.playerA} - <g:if test="${game.playerAStatus.equals('waiting')}">Orders Received</g:if>
                <g:else><g:link controller="squid" action="playerA">Issue Orders</g:link></g:else></div>
            <div class="summaryPlayerB">player B: ${game.playerB} - <g:if test="${game.playerBStatus.equals('waiting')}">Orders Received</g:if>
                <g:else><g:link controller="squid" action="playerB">Issue Orders</g:link></g:else></div>
            <div class="summaryBoardSize">board size: ${game.rows} x ${game.columns}</div>
        </g:if>
        <g:else>
            <div class="gameOver">Game Over<br>
                <g:if test="${game.winner=='Draw'}">
                    The game is a draw.
                </g:if>
                <g:elseif test="${game.winner=='PlayerA'}">
                    The winner is ${game.playerA}
                </g:elseif>
                <g:elseif test="${game.winner=='PlayerB'}">
                    The winner is ${game.playerB}
                </g:elseif>
            </div>
        </g:else>
    </g:if>
</span>