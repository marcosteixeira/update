<%@ include file="/base.jsp" %>
<body>
	<form id="form" action="<c:url value="/atualizacao/verificarAtualizacao/{contexto.contexto}/{contexto.ultimoScriptRodado}/{contexto.tagAtual}"/>"> </form>
	<button form="form" type="submit" class="btn btn-primary btn-large"> Verificar se há atualizações</button>
</body>
</html>