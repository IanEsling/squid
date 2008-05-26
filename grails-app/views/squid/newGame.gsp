<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Squid</title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'squid.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
</head>
<body>
<h1 align="center">Welcome To Squid</h1>

<div width="100%">

    <div class="boardContainer">
        <g:render template="/squid/gameboardTemplate" model="[game:game]"/>
    </div>


    <div class="playersContainer">
        <g:render template="/squid/newGameTemplate" mode="[game:game]"/>

        <h2><g:render template="/squid/playersTemplate" model="[game:game]"/></h2>
    </div>


</div>
</body>
</html>