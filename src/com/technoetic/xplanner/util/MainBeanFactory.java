package com.technoetic.xplanner.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

//FIXME: Remove. (already removed a lot of bad stuff.)
@Deprecated
public class MainBeanFactory {
	public static BeanFactory factory = MainBeanFactory.createDefaultFactory();
	public static final String DEFAULT_BEANS_REGISTRY_PATH = "/spring-beans.xml";
	protected static final Logger LOG = LogUtil.getLogger();

	private static XmlBeanFactory createDefaultFactory() {
		try {
			return new XmlBeanFactory(new ClassPathResource(
					MainBeanFactory.DEFAULT_BEANS_REGISTRY_PATH));
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void reset() {
		MainBeanFactory.factory = MainBeanFactory.createDefaultFactory();
	}

	public static void setBean(final String name, final Object singleton) {
		((ConfigurableBeanFactory) MainBeanFactory.factory).registerSingleton(
				name, singleton);
	}

	public static void initBeanProperties(final Object bean) {
		((AutowireCapableBeanFactory) MainBeanFactory.factory)
				.autowireBeanProperties(bean,
						AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
	}

}
