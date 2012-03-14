package com.vimukti.accounter.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class SubscryptionTool extends Thread {
	Logger log = Logger.getLogger(SubscryptionTool.class);

	public SubscryptionTool() {
		super("Subsciprion Tool");
	}

	public void run() {
		log.info("Started checking of subscriptions");
		ServerLocal.set(Locale.ENGLISH);
		Session session = HibernateUtil.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			List<Client> clients = getClients();
			for (Client c : clients) {
				ClientSubscription subscription = c.getClientSubscription();
				if (subscription.isTracePeriodExpired()) {
					doExpireSubscription(c);
					continue;
				}
				if (subscription.isExpired()) {
					Date tracePeriodDate = subscription.getGracePeriodDate();
					if (tracePeriodDate == null) {
						subscription.setSubscription(Subscription
								.getInstance(Subscription.FREE_CLIENT));
						subscription.setPremiumType(0);
						subscription.setGracePeriodDate(getTracePeriodDate());
					}
				}
			}
			transaction.commit();
			log.info("Completed checking of subscriptions");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	public static Date getTracePeriodDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, ServerConfiguration.getTracePeriod());
		return c.getTime();
	}

	private void doExpireSubscription(Client c) throws AccounterException {
		ClientSubscription subscription = c.getClientSubscription();
		subscription.setGracePeriodDate(null);
		Set<String> members = subscription.getMembers();
		int premiumType = subscription.getPremiumType();
		Set<String> deletedMembers = getDeletedMembers(members, c.getEmailId(),
				premiumType);
		deleteUsers(deletedMembers, c);
		members.removeAll(deletedMembers);
		subscription.setMembers(members);

		HibernateUtil.getCurrentSession().saveOrUpdate(
				c.getClientSubscription());
		log.info("Complted..:" + c.getEmailId());
		// try {
		// UsersMailSendar.sendMailToSubscriptionExpiredUser(c);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public static Set<String> getDeletedMembers(Set<String> members,
			String emailId, int premiumType) {
		Set<String> deletedMems = new HashSet<String>();

		List<String> newMembers = new ArrayList<String>(members);
		newMembers.remove(emailId);
		int noOfUsers = 0;
		switch (premiumType) {
		case 1:
			noOfUsers = 1;
			break;
		case 2:
			noOfUsers = 2;
			break;
		case 3:
			noOfUsers = 5;
			break;
		default:
			break;
		}
		noOfUsers = newMembers.size() - noOfUsers + 1;
		for (int i = 0; i < noOfUsers && i < newMembers.size(); i++) {
			deletedMems.add(newMembers.get(i));
		}
		return deletedMems;

	}

	private void deleteUsers(Set<String> members, Client client)
			throws AccounterException {
		for (String s : members) {
			log.info("deleteUser:" + s);
			deleteUser(client, s);
		}
	}

	public static void deleteUser(Client client, String emailId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getUserIds.invited.by.client");

		((SQLQuery) query).addEntity(User.class);

		List<User> users = (List<User>) query.setParameter("emailId", emailId)
				.setParameter("clientId", client.getID()).list();
		for (User user : users) {
			user.setDeleted(true);
			session.saveOrUpdate(user);
			Activity activity = new Activity(user.getCompany(), user
					.getCompany().getCreatedBy(), ActivityType.DELETE, user);
			session.save(activity);
		}
	}

	private List<Client> getClients() {
		Session session = HibernateUtil.getCurrentSession();
		List list = session.getNamedQuery("get.all.clients").list();
		return list;
	}

}
