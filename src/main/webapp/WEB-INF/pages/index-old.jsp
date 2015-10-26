<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: christangga
  Date: 14-Oct-15
  Time: 7:25 PM
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
  <title>ReSearchEngine - Homepage</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<form:form action="/" method="POST">
  <table>
    <tr>
      <td>Document Location</td>
      <td><form:input path="docLocation" /></td>
    </tr>
    <tr>
      <td>Query Location</td>
      <td><form:input path="queryLocation" /></td>
    </tr>
    <tr>
      <td>Relevance Judgement Location</td>
      <td><form:input path="rjLocation" /></td>
    </tr>
    <tr>
      <td>Stop Word Location</td>
      <td><form:input path="swLocation" /></td>
    </tr>
    <tr>
      <td>TF</td>
      <td><form:select path="docTF">
        <form:option value="None">No TF</form:option>
        <form:option value="Raw">Raw TF</form:option>
        <form:option value="Binary">Binary TF</form:option>
        <form:option value="Log">Log TF</form:option>
        <form:option value="Augmented">Augmented TF</form:option>
      </form:select></td>
      <td><form:errors path="docTF" /></td>
    </tr>
    <tr>
      <td>IDF</td>
      <td><form:select path="docIDF" >
        <form:option value="None">No IDF</form:option>
        <form:option value="Use">Use IDF</form:option>
      </form:select></td>
      <td><form:errors path="docIDF" /></td>
    </tr>
    <tr>
      <td>Normalization</td>
      <td><form:select path="docNorm" >
        <form:option value="None">No Normalization</form:option>
        <form:option value="Use">Use Normalization</form:option>
      </form:select></td>
      <td><form:errors path="docNorm" /></td>
    </tr>
    <tr>
      <td>TF</td>
      <td><form:select path="queryTF" >
        <form:option value="None">No TF</form:option>
        <form:option value="Raw">Raw TF</form:option>
        <form:option value="Binary">Binary TF</form:option>
        <form:option value="Log">Log TF</form:option>
        <form:option value="Augmented">Augmented TF</form:option>
      </form:select></td>
      <td><form:errors path="queryTF" /></td>
    </tr>
    <tr>
      <td>IDF</td>
      <td><form:select path="queryIDF" >
        <form:option value="None">No IDF</form:option>
        <form:option value="Use">Use IDF</form:option>
      </form:select></td>
      <td><form:errors path="queryIDF" /></td>
    </tr>
    <tr>
      <td>Normalization</td>
      <td><form:select path="queryNorm" >
        <form:option value="None">No Normalization</form:option>
        <form:option value="Use">Use Normalization</form:option>
      </form:select></td>
      <td><form:errors path="queryNorm" /></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Submit" />
      </td>
    </tr>
  </table>
</form:form>
</body>
</html>
