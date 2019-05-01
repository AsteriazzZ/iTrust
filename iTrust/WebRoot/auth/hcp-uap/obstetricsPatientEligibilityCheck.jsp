<%--
  Created by IntelliJ IDEA.
  User: Drew
  Date: 11/3/18
  Time: 12:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.controller.obstetricVisit.ObstetricVisitController"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - Obstetrics Patient Eligibility Check";
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

    String patientPidString = (String) session.getAttribute("pid");
    DAOFactory patientProdDAO = DAOFactory.getProductionInstance();
    PatientDAO patDAO = new PatientDAO(patientProdDAO);
    boolean eligible = patDAO.getPatientObstetricEligibility(Long.parseLong(patientPidString));

//    boolean eligible = false;

    if(eligible){
        //send them directly to the next page
        response.sendRedirect("/iTrust/auth/hcp-uap/viewObstetricsVisit.xhtml");
    }
    else{
        //if not eligible and current HCP is an OB/GYN, then we can approve the patient and then send them to the next page
        if(personnelForm.getSpecialty().equals("OB/GYN")){
            %>
            <h4>The patient is not eligible for obstetric care. Please try another patient or approve this patient for obstetrics care first: </h4>
            <input type="button" name="eligible" value="Click here to approve this patient for obstetrics care" onclick="javascript: location='obstetricsPatientEligibilityCheck.jsp?act=change'">
            <%
                String action=request.getParameter("act");
                if (action != null && action.compareTo("change") == 0)
                {
                    patDAO.editObstetricEligibility(true, Long.parseLong(patientPidString));
                    response.sendRedirect("/iTrust/auth/hcp-uap/viewObstetricsVisit.xhtml");
                }
        }
        //otherwise no access
        else{
            %>
            <h4>The patient is not eligible for obstetric care.</h4>
            <p>You are not an OB/GYN HCP and do not have the authority to approve them for obstetrics care </p>
            <p>Please refer this patient to an OB/GYN HCP for assistance </p>
            <%
        }

    }
        %>
