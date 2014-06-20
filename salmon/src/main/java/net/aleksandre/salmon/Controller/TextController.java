package net.aleksandre.salmon.Controller;

import java.util.List;
import java.util.Properties;

import net.aleksandre.salmon.model.Text;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class TextController {
	private static SessionFactory sessionFactory = null;
	private static ServiceRegistry serviceRegistry = null;
	private static Session session = null;

	static {
		Configuration configuration = new Configuration();
		configuration.configure();

		Properties properties = configuration.getProperties();

		serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
	}

	private void initTable() {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Text text1 = new Text("Sample text 1");
			Text text2 = new Text("საცდელი ტექსტი");
			session.save(text1);
			session.save(text2);
			session.flush();
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
		}
	}

	public TextController() {
		// initTable();
	}

	@SuppressWarnings("unchecked")
	public List<Text> getTexts() {
		List<Text> texts = null;
		try {
			texts = session.createQuery("from Text").list();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return texts;
	}

	public void saveText(Text text) {
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(text);
			session.flush();
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
		}
	}
}
