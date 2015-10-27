<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="icon" href="<c:url value="/res/assets/favicon.ico" />">

  <title>ReSearch Engine</title>

  <!-- Bootstrap core CSS -->
  <link href="<c:url value="/res/css/bootstrap.min.css" />" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="<c:url value="/res/css/starter-template.css" />" rel="stylesheet">

  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/">ReSearch Engine</a>
    </div>
    <div id="navbar" class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li><a href="/">Home</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Result <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="experimental">Experimental</a></li>
            <li class="active"><a href="#">Interactive</a></li>
          </ul>
        </li>
      </ul>
    </div><!--/.nav-collapse -->
  </div>
</nav>

<div class="container">
  <div class="starter-template">
    <h1 align="center">Interactive Result</h1>

    <div class="row">
      <form:form action="/interactive" method="POST" enctype="multipart/form-data">
        <div class="col-md-12">
          <div class="input-group">
            <form:input type="text" class="form-control" path="query" placeholder="Search" />
            <span class="input-group-btn">
              <button class="btn btn-danger" type="submit">
                <span class="glyphicon glyphicon-search"></span>
              </button>
            </span>
          </div>
        </div>
      </form:form>

      <core:if test="${docs != null}">
        <div class="col-md-12">
          <table class="table">
            <tr>
              <th>Ranking</th>
              <th>Document #</th>
              <th>Document Title</th>
            </tr>
            <core:forEach var="i" begin="0" end="${docs[0].rankedDocuments.size()-1}">
              <tr>
                <td class="col-md-2">${i+1}</td>
                <td class="col-md-2">${docs[0].rankedDocuments[i][0]}</td>
                <td class="col-md-8">${docs[0].rankedDocuments[i][4]}</td>
              </tr>
            </core:forEach>
          </table>
        </div>
      </core:if>

    </div>
  </div>
</div><!-- /.container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="<c:url value="/res/js/bootstrap.min.js" />"></script>
</body>
</html>
