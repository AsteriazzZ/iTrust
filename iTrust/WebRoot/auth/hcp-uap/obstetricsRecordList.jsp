<%--
  Created by IntelliJ IDEA.
  User: Drew
  Date: 10/28/18
  Time: 2:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - Obstetrics Record List";
%>

<%@include file="/header.jsp"%>
<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricsRecordList.jsp");
        return;
    }

    response.sendRedirect("/iTrust/auth/hcp-uap/obstetricsPatientEligibilityCheck.jsp");


//    /* If the patient id doesn't check out, then kick 'em out to the exception handler */
//    EditPatientAction action = new EditPatientAction(prodDAO,
//            loggedInMID.longValue(), pidString);

%>
