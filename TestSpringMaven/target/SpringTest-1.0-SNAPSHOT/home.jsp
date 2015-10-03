<%--
  Created by IntelliJ IDEA.
  User: Ivana Clairine
  Date: 10/4/2015
  Time: 12:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Untitled</title>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1">
  <meta name="generator" content="Web Page Maker">
  <style type="text/css">
    /*----------Text Styles----------*/
    .ws6 {font-size: 8px;}
    .ws7 {font-size: 9.3px;}
    .ws8 {font-size: 11px;}
    .ws9 {font-size: 12px;}
    .ws10 {font-size: 13px;}
    .ws11 {font-size: 15px;}
    .ws12 {font-size: 16px;}
    .ws14 {font-size: 19px;}
    .ws16 {font-size: 21px;}
    .ws18 {font-size: 24px;}
    .ws20 {font-size: 27px;}
    .ws22 {font-size: 29px;}
    .ws24 {font-size: 32px;}
    .ws26 {font-size: 35px;}
    .ws28 {font-size: 37px;}
    .ws36 {font-size: 48px;}
    .ws48 {font-size: 64px;}
    .ws72 {font-size: 96px;}
    .wpmd {font-size: 13px;font-family: Arial,Helvetica,Sans-Serif;font-style: normal;font-weight: normal;}
    /*----------Para Styles----------*/
    DIV,UL,OL /* Left */
    {
      margin-top: 0px;
      margin-bottom: 0px;
    }
  </style>

</head>
<body>
<form name="form1" style="margin:0px">
  <input name="Query" value="Query Path" type="file" style="position:absolute;width:210px;left:284px;top:196px;z-index:1">
  <input name="formtext2" value="Documents Path" type="file" style="position:absolute;width:210px;left:689px;top:193px;z-index:4">
  <div id="formradio1" style="position:absolute; left:229px; top:259px; z-index:8"><input type="radio" name="TF-IDF" value="Raw"> Raw TF </div>
  <div id="formradio2" style="position:absolute; left:229px; top:279px; z-index:9"><input type="radio" name="TF-IDF" value="Log"> Logarithmic TF </div>
  <div id="formradio3" style="position:absolute; left:229px; top:299px; z-index:10"><input type="radio" name="TF-IDF" value="Binary"> Binary TF</div>
  <div id="formradio4" style="position:absolute; left:229px; top:319px; z-index:11"><input type="radio" name="TF-IDF" value="Augmented"> Augmented TF </div>
  <div id="formradio5" style="position:absolute; left:229px; top:339px; z-index:12"><input type="radio" name="TF-IDF" value="IDF"> IDF </div>
  <div id="formradio6" style="position:absolute; left:636px; top:259px; z-index:14"><input type="radio" name="TF-IDF" value="Raw"> Raw TF </div>
  <div id="formradio7" style="position:absolute; left:636px; top:279px; z-index:15"><input type="radio" name="TF-IDF" value="Log"> Logarithmic TF </div>
  <div id="formradio8" style="position:absolute; left:636px; top:299px; z-index:16"><input type="radio" name="TF-IDF" value="Binary"> Binary TF </div>
  <div id="formradio9" style="position:absolute; left:636px; top:319px; z-index:17"><input type="radio" name="TF-IDF" value="Augmented"> Augmented TF </div>
  <div id="formradio10" style="position:absolute; left:636px; top:339px; z-index:18"><input type="radio" name="TF-IDF" value="IDF"> IDF </div>
  <div id="formradio11" style="position:absolute; left:229px; top:400px; z-index:20"><input type="radio" name="index" value="yes"> yes </div>
  <div id="formradio12" style="position:absolute; left:229px; top:420px; z-index:21"><input type="radio" name="index" value="no"> no </div>
  <div id="formradio13" style="position:absolute; left:637px; top:400px; z-index:23"><input type="radio" name="index" value="yes"> yes </div>
  <div id="formradio14" style="position:absolute; left:637px; top:420px; z-index:24"><input type="radio" name="index" value="no"> no </div>
  <input name="submit" type="submit" value="Search" style="position:absolute;left:547px;top:481px;z-index:25">
</form>

<div id="text1" style="position:absolute; overflow:hidden; left:354px; top:126px; width:81px; height:36px; z-index:2">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws18">Query </font></div>
  </div></div>

<div id="text2" style="position:absolute; overflow:hidden; left:734px; top:123px; width:131px; height:36px; z-index:3">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws18">Documents</font></div>
  </div></div>

<div id="text3" style="position:absolute; overflow:hidden; left:234px; top:197px; width:50px; height:22px; z-index:5">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws12">Path</font></div>
  </div></div>

<div id="text4" style="position:absolute; overflow:hidden; left:640px; top:194px; width:50px; height:22px; z-index:6">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws12">Path</font></div>
  </div></div>

<div id="text5" style="position:absolute; overflow:hidden; left:318px; top:234px; width:71px; height:22px; z-index:7">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws12">TF-IDF</font></div>
  </div></div>

<div id="text6" style="position:absolute; overflow:hidden; left:712px; top:235px; width:71px; height:22px; z-index:13">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws12">TF-IDF</font></div>
  </div></div>

<div id="text7" style="position:absolute; overflow:hidden; left:318px; top:377px; width:71px; height:22px; z-index:19">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws12">INDEX</font></div>
  </div></div>

<div id="text8" style="position:absolute; overflow:hidden; left:726px; top:377px; width:71px; height:22px; z-index:22">
  <div class="wpmd">
    <div><font face="Comic Sans MS" class="ws12">INDEX</font></div>
  </div></div>


</body>
</html>

