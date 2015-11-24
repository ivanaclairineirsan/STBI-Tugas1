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
                        <li class="active"><a href="#">Interactive</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</nav>

<div class="container">
    <div class="starter-template">
        <h2 align="center"><b>Result</b> - Interactive</h2>

        <core:if test="${queryString == null}">
            <form:form action="/interactive" method="POST" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-12">
                        <div class="input-group">
                            <form:input type="text" class="form-control" path="query" placeholder="Search"/>
                        <span class="input-group-btn">
                          <button class="btn btn-danger" type="submit">
                              <span class="glyphicon glyphicon-search"></span>
                          </button>
                        </span>
                        </div>
                    </div>
                </div>
            </form:form>
        </core:if>

        <core:if test="${docs != null}">
            <div class="row">
                <div class="col-md-12">
                    Search Results for <b>${queryString}</b>:
                </div>

                <div class="col-md-12">
                    <table class="table table-hover">
                        <tr>
                            <th>Ranking</th>
                            <th>Document No</th>
                            <th>Document Title</th>
                            <th>Relevance</th>
                        </tr>
                        <core:set var="z" value="${1}"/>
                        <core:forEach var="i" items="${docs[0].rankedDocuments.keySet()}">
                            <core:forEach var="j" items="${docs[0].rankedDocuments.get(i)}">
                                <tr class="documentTable" id="table-${j[4]}">
                                    <td class="col-md-1" align="center">${z}</td>
                                    <td class="col-md-2" align="center">${j[4]}</td>
                                    <td class="col-md-8">${j[0]}</td>
                                    <td class="col-md-1" align="center"><input class="checkboxDocument"
                                                                               id="checkbox-${j[4]}"
                                                                               type="checkbox"/></td>
                                </tr>
                                <core:set var="z" value="${z+1}"/>
                            </core:forEach>
                        </core:forEach>
                    </table>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12" align="center">
                    <form:form action="/reretrieval" method="POST" enctype="multipart/form-data">
                        <form:input type="hidden" path="relevant" id="relevantDoc"/>
                        <form:input type="hidden" path="notRelevant" id="notRelevantDoc"/>
                        <button class="btn btn-success" type="submit" id="reRetrieve">Re-retrieve!</button>
                    </form:form>
                </div>
            </div>
        </core:if>
    </div>
</div>
<!-- /.container -->

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<c:url value="/res/js/jquery-2.1.4.min.js" />"></script>
<script src="<c:url value="/res/js/bootstrap.min.js" />"></script>
<script type="text/javascript">
    var relevant = [];
    var notRelevant = [];
    $(document).ready(function () {
        $(".documentTable, .checkboxDocument").on("click", function (e) {
            e.stopImmediatePropagation();

            var id = parseInt(this.id.replace("table-", "").replace("checkbox-", ""));

            if ($("#checkbox-" + id).is(':checked')) {
                $("#checkbox-" + id).prop("checked", false);
                index = relevant.indexOf(id);
                if (index > -1) {
                    relevant.splice(index, 1);
                }
                if (notRelevant.indexOf(id) < 0) {
                    notRelevant.push(id);
                }
            } else {
                $("#checkbox-" + id).prop("checked", true);
                if (relevant.indexOf(id) < 0) {
                    relevant.push(id);
                }
                var index = notRelevant.indexOf(id);
                if (index > -1) {
                    notRelevant.splice(index, 1);
                }
            }

            console.log("id ", id);
            console.log("relevant ", relevant);
            console.log("notRelevant ", notRelevant);
        });

        $("#reRetrieve").on("click", function () {
            $("#relevantDoc").val(relevant);
            $("#notRelevantDoc").val(notRelevant);
        });
    });
</script>
</body>
</html>
