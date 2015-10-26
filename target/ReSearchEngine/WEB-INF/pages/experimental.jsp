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
            <li class="active"><a href="#">Experimental</a></li>
            <li><a href="interactive">Interactive</a></li>
          </ul>
        </li>
      </ul>
    </div><!--/.nav-collapse -->
  </div>
</nav>

<div class="container">
  <div class="starter-template">
    <h1 align="center">Experimental Result</h1>

    <div class="row">
      <div class="col-md-6" align="right">
        <label for="docPrecision">Precision</label>
      </div>
      <div class="col-md-6">
        <div id="docPrecision">1.000</div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6" align="right">
        <label for="docRecall">Recall</label>
      </div>
      <div class="col-md-6">
        <div id="docRecall">1.000</div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6" align="right">
        <label for="docNIAP">Non-Interpolted Average Precision</label>
      </div>
      <div class="col-md-6">
        <div id="docNIAP">1.000</div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12" align="center">
        <span>Query #1</span>
      </div>

      <div class="col-md-9">
        <table class="table">
          <tr>
            <th>Ranking</th>
            <th>Document #</th>
            <th>Document Title</th>
          </tr>
          <tr>
            <td class="col-md-2">1</td>
            <td class="col-md-2">23</td>
            <td class="col-md-8">Query expansion using heterogeneous thesauri</td>
          </tr>
        </table>
      </div>

      <div class="col-md-3">
        <div class="row">
          <div class="col-md-6" align="right">
            <label for="queryPrecision">Precision</label>
          </div>
          <div class="col-md-6">
            <div id="queryPrecision">1.000</div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-6" align="right">
            <label for="queryRecall">Recall</label>
          </div>
          <div class="col-md-6">
            <div id="queryRecall">1.000</div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-6" align="right">
            <label for="queryNIAP">Non-Interpolted Average Precision</label>
          </div>
          <div class="col-md-6">
            <div id="queryNIAP">1.000</div>
          </div>
        </div>
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
