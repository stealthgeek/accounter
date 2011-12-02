package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.OperationContext;

public class UserManager extends Manager {
	public long inviteUser(OperationContext context) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = context.getData();
			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Operation Data Found Null...." + data);
			}
			User user = new User((ClientUser) data);
			String email = ((ClientUser) data).getEmail();
			Company company = getCompany(context.getCompanyId());
			User userByUserEmail = getUserByUserEmail(email, company);
			if (userByUserEmail != null) {
				if (userByUserEmail.isDeleted()) {
					userByUserEmail.setDeleted(false);
					userByUserEmail.setUserRole(user.getUserRole());
					userByUserEmail.setPermissions(user.getPermissions());
					userByUserEmail.setCanDoUserManagement(user
							.isCanDoUserManagement());
					user = userByUserEmail;
				}
			} else {

				company.addUser(user);
			}
			String userID = context.getUserEmail();

			createOrUpdateClient(company, userID, email, user,
					(ClientUser) data);
			User inviteduser = getUserByUserEmail(userID, company);
			Activity inviteuserActivity = new Activity(company, inviteduser,
					ActivityType.ADD, user);

			session.saveOrUpdate(user);
			session.save(inviteuserActivity);
			transaction.commit();
			ClientUser clientObject = new ClientConvertUtil().toClientObject(
					user, ClientUser.class);
			ChangeTracker.put(clientObject.toUserInfo());
			return user.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL,
						e.getMessage());
			}
		}
	}

	public long updateUser(OperationContext updateContext)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();
		try {
			IAccounterCore data = updateContext.getData();

			ClientUser clientUser = (ClientUser) data;

			User user = (User) session.get(User.class, clientUser.getID());

			String userID = updateContext.getUserEmail();

			Client updateClient = getClient(clientUser.getEmail());
			updateClient.setFirstName(clientUser.getFirstName());
			updateClient.setLastName(clientUser.getLastName());
			updateClient.setFullName(clientUser.getFullName());

			Company company = getCompany(updateContext.getCompanyId());
			User user1 = company.getUserByUserEmail(userID);
			new ServerConvertUtil().toServerObject(user,
					(IAccounterCore) clientUser, session);

			canEdit(user, data);
			session.flush();
			session.saveOrUpdate(updateClient);
			session.saveOrUpdate(user);
			Activity userUpdateActivity = new Activity(company, user1,
					ActivityType.EDIT, user);
			session.save(userUpdateActivity);
			hibernateTransaction.commit();
			ChangeTracker.put(clientUser.toUserInfo());
			return user.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}
		}

	}

	private User getUserByUserEmail(String email, Company company) {
		Session session = HibernateUtil.getCurrentSession();
		return (User) session.getNamedQuery("user.by.emailid")
				.setParameter("emailID", email)
				.setParameter("company", company).uniqueResult();

	}

	public PaginationList<ClientActivity> getUsersActivityLog(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			int startIndex, int length, long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Timestamp startTime = new Timestamp(startDate.getDateAsObject()
				.getTime());
		Timestamp endTime = new Timestamp(endDate.getDateAsObject().getTime());
		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);
		Query query;
		int count;
		if (startDate.getDate() == 0 || endDate.getDate() == 0) {
			query = session.getNamedQuery("list.Activity").setEntity("company",
					company);
			query.setFirstResult(startIndex);
			query.setMaxResults(length);
			count = ((BigInteger) session.getNamedQuery("getCountOfActivity")
					.setLong("companyId", companyId).uniqueResult()).intValue();
		} else {
			query = session.getNamedQuery("get.Activities.by.date");
			query.setParameter("fromDate", startTime);
			query.setParameter("endDate", endTime);
			query.setFirstResult(startIndex);
			query.setMaxResults(length);
			query.setEntity("company", company);
			count = ((BigInteger) session
					.getNamedQuery("getCountOfActivityBetweenDates")
					.setParameter("fromDate", startTime)
					.setParameter("endDate", endTime)
					.setLong("companyId", companyId).uniqueResult()).intValue();
		}
		List<Activity> activites = query.list();
		PaginationList<ClientActivity> clientActivities = new PaginationList<ClientActivity>();
		for (Activity activity : activites) {
			ClientActivity clientActivity;
			try {
				clientActivity = new ClientConvertUtil().toClientObject(
						activity, ClientActivity.class);
				clientActivities.add(clientActivity);
			} catch (AccounterException e) {
				e.printStackTrace();
			}

		}
		clientActivities.setTotalCount(count);
		return clientActivities;
	}

	public ArrayList<ClientUserInfo> getAllUsers(long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List<User> financeUsers = session.getNamedQuery("list.User")
				.setEntity("company", company).list();

		List<ClientUserInfo> clientUsers = new ArrayList<ClientUserInfo>();
		for (User user : financeUsers) {
			if (!user.isDeleted()) {
				ClientUser clientUser = new ClientConvertUtil().toClientObject(
						user, ClientUser.class);
				updateClientUser(clientUser, user.getClient());
				ClientUserInfo userInfo = clientUser.toUserInfo();
				clientUsers.add(userInfo);
			}
		}
		return new ArrayList<ClientUserInfo>(clientUsers);
	}

	public boolean changeMyPassword(String emailId, String oldPassword,
			String newPassword) throws DAOException {

		Session session = HibernateUtil.openSession();
		org.hibernate.Transaction tx = null;

		try {
			tx = session.beginTransaction();
			oldPassword = HexUtil.bytesToHex(Security.makeHash(emailId
					+ oldPassword));
			newPassword = HexUtil.bytesToHex(Security.makeHash(emailId
					+ newPassword));

			Query query = session.getNamedQuery("getEmailIdFromClient")
					.setParameter("emailId", emailId)
					.setParameter("password", oldPassword);
			String emailID = (String) query.uniqueResult();

			if (emailID == null)
				return false;

			query = session.getNamedQuery("updatePasswordForClient");
			query.setParameter("newPassword", newPassword);
			query.setParameter("emailId", emailId);
			query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}

	public void createAdminUser(ClientUser user, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		User admin = new User(user);
		admin.setActive(true);
		session.save(admin);
		Company company = getCompany(companyId);
		company.getUsersList().add(admin);
		session.saveOrUpdate(this);
		transaction.commit();
	}

	private void createOrUpdateClient(Company company, String senderEmailId,
			String emailId, User user, ClientUser clientUser) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		Client inviter = getClient(senderEmailId);

		Client invitedClient = getClient(emailId);
		boolean userExists = false;
		String randomString = HexUtil.getRandomString();
		if (invitedClient == null) {
			invitedClient = new Client();
			invitedClient.setActive(true);
			Set<User> users = new HashSet<User>();
			user.setClient(invitedClient);
			user.setCompany(company);
			users.add(user);
			invitedClient.setUsers(users);
			invitedClient.setCountry(inviter.getCountry());
			invitedClient.setEmailId(emailId);
			invitedClient.setFirstName(clientUser.getFirstName());
			invitedClient.setLastName(clientUser.getLastName());
			invitedClient.setFullName(clientUser.getFullName());
			invitedClient.setPassword(HexUtil.bytesToHex(Security
					.makeHash(emailId + randomString)));
			// invitedClient.setRequirePasswordReset(true);
		} else {
			userExists = true;
			Set<User> users = invitedClient.getUsers();
			boolean flag = false;
			for (User u : users) {
				if (company == u.getCompany()) {
					flag = true;
				}
			}
			if (!flag) {
				invitedClient.getUsers().add(user);
				user.setClient(invitedClient);
			}
		}
		user.setActive(userExists);
		session.setFlushMode(flushMode);
		session.save(invitedClient);
		if (userExists) {
			UsersMailSendar.sendMailToOtherCompanyUser(invitedClient,
					company.getTradingName(), inviter);
		} else {
			UsersMailSendar.sendMailToInvitedUser(invitedClient, randomString,
					company.getTradingName());
		}
	}

	public Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(BaseServlet.EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	public ArrayList<String> getAuditHistory(int objectType, long objectID,
			Long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		try {
			Query query = session.getNamedQuery("getAuditHistory")
					.setParameter("companyId", companyId)
					.setParameter("objectType", objectType)
					.setParameter("objectID", objectID);

			Iterator<String> iterator = query.list().iterator();

			ArrayList<String> jsonString = new ArrayList<String>();

			while (iterator.hasNext()) {
				jsonString.add(iterator.next().toString());
			}
			return jsonString;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

}
