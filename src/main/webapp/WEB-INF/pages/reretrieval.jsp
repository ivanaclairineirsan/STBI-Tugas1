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
                        <li><a href="<core:url value="experimental"/>">Experimental</a></li>
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
        <h2 align="center"><b>Result</b> - Second Retrieval</h2>

        <core:if test="${docs != null}">
            <div class="row">
                <div class="col-md-1" align="center"><b>Ranking</b></div>
                <div class="col-md-1" align="center"><b>Doc #</b></div>
                <div class="col-md-10"><b>Doc Title</b></div>
            </div>
            <core:set var="z" value="${1}"/>
            <core:forEach var="i" items="${docs[0].rankedDocuments.keySet()}">
                <core:forEach var="j" items="${docs[0].rankedDocuments.get(i)}">
                    <div class="row">
                        <div class="col-md-1" align="center">${z}</div>
                        <div class="col-md-1" align="center">${j[4]}</div>
                        <div class="col-md-10">${j[0]}</div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 col-md-offset-2">
                            <table class="table">
                                <tr>
                                    <th>Term</th>
                                    <th>Old Weight</th>
                                </tr>
                                <core:forEach var="k" items="${oldTerms.keySet()}">
                                    <tr>
                                        <td class="col-md-6">${k}</td>
                                        <td class="col-md-6">${oldTerms.get(k)}</td>
                                    </tr>
                                </core:forEach>
                            </table>
                        </div>
                        <div class="col-md-4">
                            <table class="table">
                                <tr>
                                    <th>Term</th>
                                    <th>New Weight</th>
                                </tr>
                                <core:forEach var="k" items="${docs[0].weightedTerms.keySet()}">
                                    <tr>
                                        <td class="col-md-6">${k}</td>
                                        <td class="col-md-6">${docs[0].weightedTerms.get(k)}</td>
                                    </tr>
                                </core:forEach>
                            </table>
                        </div>
                        <div class="col-md-2"></div>
                    </div>
                    <core:set var="z" value="${z+1}"/>
                </core:forEach>
            </core:forEach>
        </core:if>
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
