package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mortbay.util.UrlEncoded;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.utils.HibernateUtil;

public class CreateCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/CreateCompany.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		HttpSession session = request.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			return;
		}
		String status = (String) session.getAttribute("COM_STATUS");
		if (status != null) {
			response.sendRedirect("/companystatus");
			return;
		}
		doCreateCompany(request, response, emailID);

	}

	private void doCreateCompany(HttpServletRequest request,
			HttpServletResponse response, String emailID) throws IOException {
		final ServerCompany serverCompany = getCompany(request);
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		final Client client = getClient(emailID);
		Transaction transaction = session.beginTransaction();
		try {
			session.save(serverCompany);
			client.getCompanies().add(serverCompany);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		if (!validation(request, serverCompany)) {
			request.setAttribute("errormessage",
					"Company creation failed, please try with different company name.");
			dispatch(request, response, view);
		}

		final String urlString = getUrlString(serverCompany, emailID, client);
		final HttpSession httpSession = request.getSession(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					httpSession.setAttribute("COM_STATUS", "Creating");
					URL url = new URL(urlString.toString());
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						httpSession.setAttribute("COM_STATUS", "Success");
					} else {
						rollback(serverCompany, client);
						httpSession.setAttribute("COM_STATUS", "Fail");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
		response.sendRedirect("/companystatus");
		return;
	}

	/**
	 * @param serverCompany
	 * @param client
	 */
	private void rollback(ServerCompany serverCompany, Client client) {
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.getNamedQuery("delete.Client.Companies")
					.setParameter("clientID", client.getID());
			query.executeUpdate();
			query = session.getNamedQuery("delete.ServerCompany.by.Id")
					.setParameter("id", serverCompany.getID());
			query.executeUpdate();
			client.getCompanies().remove(serverCompany);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
	}

	/**
	 * @return
	 */
	private String getUrlString(ServerCompany serverCompany, String emailID,
			Client client) {
		StringBuffer buffer = new StringBuffer(
				"http://localhost:8890/initialzeCompany?");

		buffer.append(PARAM_SERVER_COMPANY_ID);
		buffer.append('=');
		buffer.append(new UrlEncoded(String.valueOf(serverCompany.getID()))
				.encode());

		buffer.append('&');
		buffer.append(PARA_COMPANY_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded(serverCompany.getCompanyName()).encode());

		buffer.append('&');
		buffer.append(PARAM_COMPANY_TYPE);
		buffer.append('=');
		buffer.append(new UrlEncoded(String.valueOf(serverCompany
				.getCompanyType())).encode());

		buffer.append('&');
		buffer.append(EMAIL_ID);
		buffer.append('=');
		buffer.append(new UrlEncoded(emailID).encode());

		buffer.append('&');
		buffer.append(PARAM_FIRST_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded(client.getFirstName()).encode());

		buffer.append('&');
		buffer.append(PARAM_LAST_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded((client.getLastName())).encode());

		// buffer.append('&');
		// buffer.append(PARAM_COUNTRY);
		// buffer.append('=');
		// buffer.append(client.getCountry());
		//
		// buffer.append('&');
		// buffer.append(PARAM_PH_NO);
		// buffer.append('=');
		// buffer.append(client.getPhoneNumber());

		return buffer.toString();

	}

	private ServerCompany getCompany(HttpServletRequest request) {

		String companyId = request.getParameter("name");
		String companyType = request.getParameter("companyType");

		ServerCompany company = new ServerCompany();
		if (companyType != null) {
			int type = Integer.parseInt(companyType);
			if (type == Company.ACCOUNTING_TYPE_UK
					|| type == Company.ACCOUNTING_TYPE_US) {
				company.setCompanyType(type);
			} else {
				company.setCompanyType(Company.ACCOUNTING_TYPE_INDIA);
			}
		}
		if (companyId != null) {
			company.setCompanyName(companyId.toLowerCase());
		}

		return company;
	}

	private boolean validation(HttpServletRequest request, ServerCompany company) {
		Boolean flag = true;
		String message = "";
		// if (AccounterService.isCompanyExits(company.getCompanyName())) {
		// if (!message.isEmpty())
		// message = message + ", Company with this name is already exist";
		// else
		// message = "Company with this name is already exist";
		// flag = false;
		// }

		if (!(isValidCompanyName(company.getCompanyName()))) {
			if (!message.isEmpty())
				message = message + ", Invalid Company ID";
			else
				message = "Invalid Company ID";
			flag = false;

		}

		request.setAttribute("errormessage", message);
		return flag;
	}

	private boolean isValidCompanyName(String companyId) {
		return companyId.matches("^[a-z][a-z0-9]{5,}$");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session != null) {
			String status = (String) session.getAttribute("COM_STATUS");
			if (status != null) {
				response.sendRedirect("/companystatus");
				return;
			}
		}
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		dispatch(request, response, view);
	}
}
