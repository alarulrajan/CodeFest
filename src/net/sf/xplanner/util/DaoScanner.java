package net.sf.xplanner.util;

import java.util.Map;
import java.util.Set;

import net.sf.xplanner.dao.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

public class DaoScanner {
	private ApplicationContext applicationContext;

	public void init() {
		final Map<String, Dao> beansOfType = this.applicationContext
				.getBeansOfType(Dao.class);
		final Set<String> keySet = beansOfType.keySet();
		for (final String key : keySet) {
			System.out.println(key + "=" + beansOfType.get(key));
		}
	}

	@Autowired
	@Required
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
