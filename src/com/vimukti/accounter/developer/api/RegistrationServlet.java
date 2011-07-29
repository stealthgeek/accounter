package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Developer;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

public class RegistrationServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			req.getRequestDispatcher("/sits/login.jsp?dest=/api/registration")
					.forward(req, resp);
			return;
		}
		String emailId = (String) session.getAttribute("emailId");
		if (emailId == null) {
			req.getRequestDispatcher("/sits/login.jsp?dest=/api/registration")
					.forward(req, resp);
			return;
		}
		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = getClient(emailId);
			if (client == null) {
				req.getRequestDispatcher(
						"/sits/login.jsp?dest=/api/registration").forward(req,
						resp);
				return;
			}

			Developer developer = getDeveloper(client);
			if (developer != null) {
				sendApiInfoPage(developer, req, resp);
				return;
			}
			Set<ServerCompany> companies = client.getCompanies();
			List<String> companyList = new ArrayList<String>();
			for (ServerCompany company : companies) {
				companyList.add(company.getCompanyName());
			}
			req.setAttribute("companyList", companyList);
			req.getRequestDispatcher("/api/registration.jsp")
					.forward(req, resp);
		} catch (Exception e) {
		} finally {
			if (hibernateSession != null) {
				hibernateSession.close();
			}
		}
	}

	private void sendApiInfoPage(Developer developer, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("developer", developer);
		req.getRequestDispatcher("/api/apiinfo.jsp").forward(req, resp);
	}

	private Developer getDeveloper(Client client) {
		Session session = HibernateUtil.getCurrentSession();
		return (Developer) session.getNamedQuery("get.developer.by.client")
				.setParameter(0, client).uniqueResult();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			req.setAttribute("error", "Session has expired");
			req.getRequestDispatcher("/sits/error.jsp").forward(req, resp);
		}
		String emailId = (String) session.getAttribute("emailId");
		if (emailId == null) {
			req.setAttribute("error", "Session has expired");
			req.getRequestDispatcher("/sits/error.jsp").forward(req, resp);
		}
		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = getClient(emailId);
			if (client == null) {
				req.setAttribute("error", "Session has expired");
				req.getRequestDispatcher("/sits/error.jsp").forward(req, resp);
			}
			if (!client.isActive()) {
				req.setAttribute("error", "User Not Activated");
				req.getRequestDispatcher("/sits/error.jsp").forward(req, resp);
			}
			Developer developer = getDeveloper(client);
			if (developer != null) {
				sendApiInfoPage(developer, req, resp);
				return;
			}
			String applicationName = req.getParameter("applicationName");
			String description = req.getParameter("description");
			String integrationUrl = req.getParameter("integrationUrl");
			String applicationType = req.getParameter("applicationType");
			String applicationUse = req.getParameter("applicationUse");
			String developerEmailId = req.getParameter("developerEmailId");
			String contact = req.getParameter("contact");

			if (!isValidInputs(NAME, applicationName, description,
					applicationType, applicationUse)
					|| !isValidInputs(MAIL_ID, developerEmailId)
					|| !isValidInputs(PHONE_NO, contact)) {
				// Set<ServerCompany> companies = client.getCompanies();
				// List<String> companyList = new ArrayList<String>();
				// for (ServerCompany company : companies) {
				// companyList.add(company.getCompanyName());
				// }
				// req.setAttribute("companyList", companyList);
				req.setAttribute("error", "Invalid inputs");
				req.getRequestDispatcher("/api/registration.jsp").forward(req,
						resp);
				return;
			}
			String apiKey = SecureUtils.createID(8);
			String secretKey = SecureUtils.createID(16);

			developer = new Developer();
			developer.setApiKey(apiKey);
			developer.setApplicationName(applicationName);
			developer.setApplicationType(applicationType);
			developer.setClient(client);
			developer.setContact(contact);
			developer.setDescription(description);
			developer.setIntegrationUrl(integrationUrl);
			developer.setSecretKey(secretKey);
			saveEntry(developer);
			sendApiInfoPage(developer, req, resp);

		} catch (Exception e) {
		} finally {
			if (hibernateSession != null) {
				hibernateSession.close();
			}
		}
	}
}
