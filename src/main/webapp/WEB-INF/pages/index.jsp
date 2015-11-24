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
                <li class="active"><a href="#">Home</a></li>
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

        <core:if test="${message != null}">
            <div class="alert alert-warning alert-dismissable" role="alert">${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </core:if>

        <form:form action="/" method="POST" enctype="multipart/form-data">
            <h2 align="center"><b>ReSearch Engine</b> - Configuration</h2>

            <div class="row">
                <div class="col-md-12">
                    <h3 align="center">File Location</h3>
                </div>

                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docFile">Document location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="docFile"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryFile">Query location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="queryFile"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="rjFile">Relevance Judgement location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="rjFile"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="swFile">Stop Words location</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="file" path="swFile"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-4">
                    <div class="row">
                        <div class="col-md-12" align="center">
                            <h3>Document</h3>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docTF">TF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docTF">
                                <form:option value="raw">Raw TF</form:option>
                                <form:option value="binary">Binary TF</form:option>
                                <form:option value="augmented">Augmented TF</form:option>
                                <form:option value="logarithmic">Logarithmic TF</form:option>
                                <form:option value="none">No TF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docIDF">IDF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docIDF">
                                <form:option value="none">No IDF</form:option>
                                <form:option value="use">Use IDF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docStemming">Stemming</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docStemming">
                                <form:option value="none">No Stemming</form:option>
                                <form:option value="use">Use Stemming</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="docNormalization">Normalization</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="docNormalization">
                                <form:option value="none">No Normalization</form:option>
                                <form:option value="use">Use Normalization</form:option>
                            </form:select>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="row">
                        <div class="col-md-12" align="center">
                            <h3>Query</h3>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryTF">TF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryTF">
                                <form:option value="raw">Raw TF</form:option>
                                <form:option value="binary">Binary TF</form:option>
                                <form:option value="augmented">Augmented TF</form:option>
                                <form:option value="logarithmic">Logarithmic TF</form:option>
                                <form:option value="none">No TF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryIDF">IDF</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryIDF">
                                <form:option value="none">No IDF</form:option>
                                <form:option value="use">Use IDF</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryStemming">Stemming</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryStemming">
                                <form:option value="none">No Stemming</form:option>
                                <form:option value="use">Use Stemming</form:option>
                            </form:select>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="row">
                        <div class="col-md-12" align="center">
                            <h3>Relevance Feedback</h3>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="topS">Top S</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="text" path="topS" value="10"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="topN">Top N</label>
                        </div>
                        <div class="col-md-6">
                            <form:input type="text" path="topN" value="3"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="rfMethod">Method</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="rfMethod">
                                <form:option value="roccio">Roccio</form:option>
                                <form:option value="regular">Regular</form:option>
                                <form:option value="dec-hi">Dec-Hi</form:option>
                                <form:option value="pseudo">Pseudo</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="queryExpansion">Query Expansion</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="queryExpansion">
                                <form:option value="no">No</form:option>
                                <form:option value="yes">Yes</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" align="right">
                            <label for="secondRetrieval">2nd Retrieval</label>
                        </div>
                        <div class="col-md-6">
                            <form:select path="secondRetrieval">
                                <form:option value="same">Same As 1st Retrieval</form:option>
                                <form:option value="different">Different From 1st Retrieval</form:option>
                            </form:select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12" align="center">
                    <button class="btn btn-success" type="submit">Index It!</button>
                </div>
            </div>
        </form:form>
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
