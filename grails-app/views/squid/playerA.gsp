<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Squid</title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'squid.css')}"/>
</head>
<body>
<h1 align="center">Welcome To Squid</h1>

<div width="100%">
    <h2><g:render template="/squid/gameSummaryTemplate"/></h2>

    <div class="boardContainer">
        <g:render template="/squid/playerAGameboardTemplate"/>
    </div>

    <div class="playersContainer">
        <g:render template="/squid/playerAOrderTemplate"/>
    </div>

</div>
</body>
</html>