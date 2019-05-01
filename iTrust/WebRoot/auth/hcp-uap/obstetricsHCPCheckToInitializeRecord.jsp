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
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - Obstetrics HCP Check";
%>

<%@include file="/header.jsp"%>
<%
    /* Require a Personnel ID first */
    String pidString= "" + loggedInMID.longValue();

    /* A bad personnel ID gets you exiled to the exception handler */
    EditPersonnelAction personnelEditor = new EditPersonnelAction(prodDAO,loggedInMID.longValue(), pidString);
    long pid  = personnelEditor.getPid();

    PersonnelBean personnelForm;
    personnelForm = prodDAO.getPersonnelDAO().getPersonnel(pid);

    //Let them through to the next page because they have access
    if(personnelForm.getSpecialty().equals("OB/GYN")){
        response.sendRedirect("/iTrust/auth/hcp-uap/obstetricsPatientInitialization.xhtml");
    }
    //tell them they don't have access and allow them to go back with a button/link
    else{
        %>
            <h4>You are not an OB/GYN HCP. You do not have access. </h4>
            <a href="viewObstetricsVisit.xhtml">Click here to return</a>
        <%
    }
        %>
