<span class="summary">
    <g:if test="${game != null}">
        <div class="summaryGameNumber">current game is number ${game?.id}, turn number ${game?.turnNumber}</div>
        <div class="summaryPlayerA">player A: ${game?.playerA} - <g:if test="${game?.playerAStatus?.equals('waiting')}">Orders Received</g:if>
        <g:else><a href="/squid/squid/playerA">Make Move</a></g:else></div>
        <div class="summaryPlayerB">player B: ${game?.playerB} - <g:if test="${game?.playerBStatus?.equals('waiting')}">Orders Received</g:if>
        <g:else><a href="/squid/squid/playerB">Make Move</a></g:else></div>
        <div class="summaryBoardSize">board size: ${game.rows} x ${game.columns}</div>
    </g:if>
</span>