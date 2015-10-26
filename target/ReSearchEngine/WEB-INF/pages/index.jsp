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
                <li class="active"><a href="#">Home</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Result <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="experimental">Experimental</a></li>
                        <li><a href="interactive">Interactive</a></li>
                    </ul>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>

<div class="container">
    <div class="starter-template">

        <core:if test="${message != null}">
            <div class="alert alert-warning alert-dismissable" role="alert">${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </core:if>

        <h1 align="center">Indexing</h1>

        <form:form action="/" method="POST" enctype="multipart/form-data">
            <div class="row">
                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docLocation">Document location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="docLocation" />
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryLocation">Query location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="queryLocation" />
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="rjLocation">Relevance Judgement location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="rjLocation" />
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="swLocation">Stop Word location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="swLocation" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <h2 align="center">Document</h2>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docTF">TF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docTF">
                                <form:option value="none">No TF</form:option>
                                <form:option value="raw">Raw TF</form:option>
                                <form:option value="binary">Binary TF</form:option>
                                <form:option value="augmented">Augmented TF</form:option>
                                <form:option value="logarithmic">Logarithmic TF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docIDF">IDF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docIDF" >
                                <form:option value="none">No IDF</form:option>
                                <form:option value="use">Use IDF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docNormalization">Normalization</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docNormalization" >
                                <form:option value="none">No Normalization</form:option>
                                <form:option value="use">Use Normalization</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docStemming">Stemming</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docStemming" >
                                <form:option value="none">No Stemming</form:option>
                                <form:option value="use">Use Stemming</form:option>
                            </form:select>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <h2 align="center">Query</h2>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryTF">TF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryTF">
                                <form:option value="none">No TF</form:option>
                                <form:option value="raw">Raw TF</form:option>
                                <form:option value="binary">Binary TF</form:option>
                                <form:option value="augmented">Augmented TF</form:option>
                                <form:option value="logarithmic">Logarithmic TF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryIDF">IDF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryIDF" >
                                <form:option value="none">No IDF</form:option>
                                <form:option value="use">Use IDF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryNormalization">Normalization</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryNormalization" >
                                <form:option value="none">No Normalization</form:option>
                                <form:option value="use">Use Normalization</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryStemming">Stemming</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryStemming" >
                                <form:option value="none">No Stemming</form:option>
                                <form:option value="use">Use Stemming</form:option>
                            </form:select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12" align="center">
                    <button class="btn btn-danger" type="submit">Indexing</button>
                </div>
            </div>
        </form:form>
    </div>
</div><!-- /.container -->

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="<c:url value="/res/js/bootstrap.min.js" />"></script>
</body>
</html>
