package com.rmpksoft.jrlesson.helloworld;

import java.util.Collection;
import java.util.Vector;

public class TestBeanFactory {

	public static Collection<CustomerBean> generateCollection() {
		Vector<CustomerBean> collection = new Vector<>();
		collection.add(new CustomerBean("Ted", 20));
		collection.add(new CustomerBean("Jack", 34));
		collection.add(new CustomerBean("Bob", 56));
		collection.add(new CustomerBean("Alice", 12));
		collection.add(new CustomerBean("Robin", 22));
		collection.add(new CustomerBean("Peter", 28));

		return collection;
	}

}
