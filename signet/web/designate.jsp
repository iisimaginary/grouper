<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--
  $Id: designate.jsp,v 1.1 2005-09-09 20:49:46 acohen Exp $
  $Date: 2005-09-09 20:49:46 $
  
  Copyright 2004 Internet2 and Stanford University.  All Rights Reserved.
  Licensed under the Signet License, Version 1,
  see doc/license.txt in this distribution.
-->

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <meta name="robots" content="noindex, nofollow" />
  <title>
    Signet
  </title>
  <link href="styles/signet.css" rel="stylesheet" type="text/css" />
  <script language="JavaScript" type="text/javascript" src="scripts/signet.js"></script>
</head>

<body>

  <script type="text/javascript">

    var subjectSelected            = false;

    function showSubjectSearchResult(divId)
    {
      // Fill the specified DIV with the result of the person search.
      loadXMLDoc
        ('personForProxySearch.jsp?searchString='
         + document.getElementById('subjectSearchString').value,
         divId);
       
      // Make that DIV visible.
      var divToShow = document.getElementById(divId);
      divToShow.style.display = 'block';
    }
    
    function subsystemSelected(subsystemSelectId, subsystemPromptValue)
    {
      var theForm = document.form1;
      var subsystemSelect = document.getElementById(subsystemSelectId);
      if (subsystemSelect.value == subsystemPromptValue)
      {
        // Choosing "please choose a subsystem" doesn't really count as a
        // selection.
        return false;
      }
      else
      {
        return true;
      }
    }
    
    function setContinueButtonStatus(subsystemSelectId, subsystemPromptValue)
    {
      if (subjectSelected && subsystemSelected(subsystemSelectId, subsystemPromptValue))
      {
        document.form1.continueButton.disabled=false;
      }
      else
      {
        document.form1.continueButton.disabled=true;
      }
    }
    
    function selectSubject(subsystemSelectId, subsystemPromptValue)
    {
      var selectSubjectCompositeId
        = document.getElementById("<%=Constants.SUBJECT_SELECTLIST_ID%>").value;
          
      var subjectNameDivId = "SUBJECT_NAME:" + selectSubjectCompositeId;
      var subjectName = document.getElementById(subjectNameDivId).innerHTML;
          
      var subjectDescriptionDivId = "SUBJECT_DESCRIPTION:" + selectSubjectCompositeId;
      var subjectDescription = document.getElementById(subjectDescriptionDivId).innerHTML;
          
      var subjectWarningDivId = "SUBJECT_WARNING:" + selectSubjectCompositeId;
      var subjectWarning
        = document.getElementById(subjectWarningDivId).innerHTML;
        
      var subjectNameElement = document.getElementById("subjectName");
      var subjectDescriptionElement = document.getElementById("subjectDescription");
      var subjectWarningElement = document.getElementById("subjectWarning");
        
      subjectNameElement.firstChild.nodeValue=subjectName;
      subjectDescriptionElement.firstChild.nodeValue=subjectDescription;
      subjectWarningElement.firstChild.nodeValue=subjectWarning;
      
      subjectSelected = true;
      setContinueButtonStatus(subsystemSelectId, subsystemPromptValue);
    }
    
    // return TRUE if the form should be submitted, FALSE otherwise.
    function submitOrSearch()
    {
      if (cursorInPersonSearch())
      {
        if (!personSearchStrIsEmpty('subjectSearchString'))
        {
          subjectSelected=false;
          document.getElementById('SubjectResultDiv').style.display
            ='display:none;';
          document.getElementById('subjectName').firstChild.nodeValue
            ='';
          document.getElementById('subjectDescription').firstChild.nodeValue
            ='';
        
          // Someday, we'll start displaying these warnings, and then we'll have
          // to start erasing them, as well.
          // document.getElementById('subjectWarning').style.firstChild.nodeValue
          //   ='';

          setContinueButtonStatus
            ('<%=Constants.SUBSYSTEM_SELECTNAME%>',
             '<%=Constants.SUBSYSTEM_PROMPTVALUE%>');
         
          performPersonSearch
            ('personForProxySearch.jsp',
             'subjectSearchString',
             'SubjectResultDiv');
        }
         
         return false;
      }
      else
      {
        return true;
      }
         
//      return checkForCursorInPersonSearch
//        ('personForProxySearch.jsp',
//         'subjectSearchString',
//         'SubjectResultDiv');
    }
    
  </script>

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%@ page import="java.util.Set" %>

<%@ page import="edu.internet2.middleware.signet.Signet" %>
<%@ page import="edu.internet2.middleware.signet.PrivilegedSubject" %>
<%@ page import="edu.internet2.middleware.signet.Proxy" %>

<%@ page import="edu.internet2.middleware.signet.ui.Common" %>
<%@ page import="edu.internet2.middleware.signet.ui.Constants" %>

<% 
  Signet signet
     = (Signet)
         (request.getSession().getAttribute("signet"));

  PrivilegedSubject loggedInPrivilegedSubject
    = (edu.internet2.middleware.signet.PrivilegedSubject)
        (request.getSession().getAttribute("loggedInPrivilegedSubject"));
         
  Set grantableSubsystems = loggedInPrivilegedSubject.getGrantableSubsystems();
         
  // If the session contains a "currentProxy" attribute, then we're
  // editing an existing Proxy. Otherwise, we're attempting to create a
  // new one.
  Proxy currentProxy
    = (Proxy)(request.getSession().getAttribute("currentProxy"));
%>

  <tiles:insert page="/tiles/header.jsp" flush="true" />

  <div id="Navbar">
    <span class="logout">
      <a href="NotYetImplemented.do">
        <%= loggedInPrivilegedSubject.getName() %>: Logout
      </a>
    </span> <!-- logout -->
    <span class="select">
      <a href="Start.do">
        Home
      </a>
      &gt; Designated Driver
    </span> <!-- select -->
  </div> <!-- Navbar -->

  <form
    name="form1"
          method="post"
          action="ConfirmProxy.do" 
          onSubmit="return submitOrSearch();"> <!-- TRUE if submit -->

    <div id="Layout">
      <div id="Content">
      
        <div class="table1"> 
        
          <div class="section">
            <h2>
              Select privilege type(s)
            </h2>
            <p>
              Instructions go here.
            </p>
            <div style="margin-left: 25px;">
              <%=Common.subsystemSelectionSingle
                   (Constants.SUBSYSTEM_SELECTNAME,
                    Constants.SUBSYSTEM_PROMPTVALUE,
                    "Instructions go here.",
                    "setContinueButtonStatus('" + Constants.SUBSYSTEM_SELECTNAME + "', '" + Constants.SUBSYSTEM_PROMPTVALUE+ "');",
                    grantableSubsystems)%>
            </div>
          </div> <!-- section -->
      
          <div class="section">		
            <h2>
              Find subject
            </h2>
            <div style="margin-left: 25px;">
              <input
                name="subjectSearchString"
                type="text"
                id="subjectSearchString"
                class="long"
                maxlength="500"
                onFocus="personSearchFieldHasFocus=true;"
                onBlur="personSearchFieldHasFocus=false;" />

              <input
                name="subjectSearchbutton"
                type="submit"
                class="button1"
                value="Search"
                onclick="personSearchButtonHasFocus=true;"
                onfocus="personSearchButtonHasFocus=true;"
                onblur="personSearchButtonHasFocus=false;" />
              
            <div id="SubjectResultDiv" style="display:none;">
              <!-- The contents of this DIV will be inserted by JavaScript. -->
            </div>

	
     
            <div id="subjectDetails" style="float: left; padding-left: 10px; width: 400px;">
              <span class="category" id="subjectName">
                <!-- subject name gets inserted by Javascript at subject-selection time -->
              </span> <!-- subjectName -->
              <br />			  
              <span class="dropback" id="subjectDescription">
                <!-- subject description gets inserted by Javascript at subject-selection time -->
              </span> <!-- subjectDescription -->
              <br />
              <span class="status" id="subjectWarning">
                <!-- subject warning gets inserted by Javascript at subject-selection time -->
              </span>
            </div>  <!-- subjectDetails -->
          </div> <!-- section -->
        </div>  <!-- table1 -->
		 
        <div class="section">
          <h2>Set conditions</h2>
	      <table>
	      
            <tr>
              <%=Common.dateSelection
                (request,
                 Constants.EFFECTIVE_DATE_PREFIX,
                 "Designation will take effect:",
                 "immediately",
                 "on",
                 currentProxy == null
                   ? null
                   : currentProxy.getEffectiveDate())%>
            </tr>
            
            <tr>
              <%=Common.dateSelection
                (request,
                 Constants.EXPIRATION_DATE_PREFIX,
                 "...and will remain in effect:",
                 "until I revoke this designation",
                 "on",
                 currentProxy == null
                   ? null
                   : currentProxy.getExpirationDate())%>
            </tr>
          </table>
          <legend></legend>
        </div> <!-- section -->
        
        <div class="section" style="padding-top: 5px;">
        
          <h2>Complete this designation </h2>	
          <input
            name="continueButton"
            disabled="true"
            type="submit"
            class="button-def"
            onclick="personSearchButtonHasFocus=false;"
            onfocus="personSearchButtonHasFocus=false;"
            value="<%=(currentProxy==null?"Complete designation":"Save changes")%>" 
							
          <br />
            <a href="Start.do">
              <img src="images/arrow_left.gif" />
              CANCEL and return to your subject view
            </a>
          </div>
        </div>
      </div>
  
      <div id="Sidebar">      
        <div class="helpbox">
          <div class="ocbox" id="cbhelp">
            <a href="javascript:closeBox('boxhelp', 'obhelp', 'cbhelp');">-</a>
          </div>
          <div class="ocbox" id="obhelp" style="display: none;">
            <a href="javascript:openBox('boxhelp', 'obhelp', 'cbhelp');">+</a>
          </div>
          <h2>
            help
          </h2>
          <div class="actionbox">
            [an error occurred while processing this directive]
          </div>
        </div>
      </div>
  
      <tiles:insert page="/tiles/footer.jsp" flush="true" />
	
    </div>	
  </form>
</body>
</html>