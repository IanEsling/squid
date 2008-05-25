<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Squid</title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'squid.css')}"/>
</head>
<body>
<h1>Welcome To Squid</h1>
<g:render template="/squid/playersTemplate" model="[game:game]"/>
<g:render template="/squid/gameboardTemplate" model="[game:game]"/>

<div>
    <g:form url="[action:'newGame', controller:'squid']">
        <g:actionSubmit value="New Game"/>
    </g:form>
</div>

</body>
</html>