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
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<core:url value="/"/>">ReSearch Engine</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="<core:url value="/"/>">Home</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">Result <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li class="active"><a href="#">Experimental</a></li>
                        <li><a href="<core:url value="interactive"/>">Interactive</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</nav>

<div class="container">
    <div class="starter-template">
        <h1 align="center"><b>Result</b> - Experimental</h1>

        <core:forEach var="n" begin="1" end="${docs.size()}">
            <div class="row">
                <div class="col-md-12" align="center">
                    <span>Query #${n}</span>
                </div>

                <div class="col-md-9">
                    <table class="table">
                        <tr>
                            <th>Ranking</th>
                            <th>Doc #</th>
                            <th>Doc Title</th>
                            <th>Similarity</th>
                        </tr>
                        <core:set var="z" value="${1}"/>
                        <core:forEach var="i" items="${docs[n-1].rankedDocuments.keySet()}">
                            <core:forEach var="j" items="${docs[n-1].rankedDocuments.get(i)}">
                                <tr>
                                    <td class="col-md-1">${z}</td>
                                    <td class="col-md-1">${j[4]}</td>
                                    <td class="col-md-8">${j[0]}</td>
                                    <td class="col-md-2">${j[5]}</td>
                                </tr>
                                <core:set var="z" value="${z+1}"/>
                            </core:forEach>
                        </core:forEach>
                    </table>
                </div>

                <div class="col-md-3">
                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryPrecision">Precision</label>
                        </div>
                        <div class="col-md-6">
                            <div id="queryPrecision">${docs[n-1].recallPrecision[1]}</div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryRecall">Recall</label>
                        </div>
                        <div class="col-md-6">
                            <div id="queryRecall">${docs[n-1].recallPrecision[0]}</div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryNIAP">Non-Interpolated Average Precision</label>
                        </div>
                        <div class="col-md-6">
                            <div id="queryNIAP">${docs[n-1].NIAP}</div>
                        </div>
                    </div>
                </div>
            </div>
        </core:forEach>
    </div>
</div>
<!-- /.container -->

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<c:url value="/res/js/jquery-2.1.4.min.js" />"></script>
<script src="<c:url value="/res/js/bootstrap.min.js" />"></script>
</body>
</html>
