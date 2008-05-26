<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Squid</title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'squid.css')}"/>
</head>
<body>
<h1 align="center">Welcome To Squid</h1>

<div>
    <g:form url="[controller:'squid', action:'newGame']">
        <g:actionSubmit value="New Game"/>
    </g:form>
</div>

<h2><g:render template="/squid/playersTemplate" model="[game:game]"/></h2>

<g:render template="/squid/gameboardTemplate" model="[game:game]"/>

</body>
</html>