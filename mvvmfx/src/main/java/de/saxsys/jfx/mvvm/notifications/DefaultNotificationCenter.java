/*******************************************************************************
 * Copyright 2013 Alexander Casall
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.saxsys.jfx.mvvm.notifications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link NotificationCenter}.
 * 
 * @author sialcasa
 * 
 */
class DefaultNotificationCenter implements NotificationCenter {
	
	DefaultNotificationCenter() {
	}
	
	private final Map<String, List<NotificationObserver>> observersForName = new HashMap<String, List<NotificationObserver>>();
	
	@Override
	public void addObserverForName(String name, NotificationObserver observer) {
		List<NotificationObserver> observers = this.observersForName.get(name);
		if (observers == null) {
			this.observersForName.put(name, new ArrayList<NotificationObserver>());
		}
		observers = this.observersForName.get(name);
		observers.add(observer);
	}
	
	@Override
	public void removeObserverForName(String name, NotificationObserver observer) {
		List<NotificationObserver> observers = this.observersForName.get(name);
		observers.remove(observer);
		if (observers.size() == 0) {
			this.observersForName.remove(name);
		}
	}
	
	@Override
	public void removeObserver(NotificationObserver observer) {
		Iterator<String> iterator = this.observersForName.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Iterator<NotificationObserver> iterator2 = this.observersForName.get(key).iterator();
			while (iterator2.hasNext()) {
				NotificationObserver actualObserver = iterator2.next();
				if (actualObserver == observer) {
					this.observersForName.remove(key);
					break;
				}
			}
		}
	}
	
	@Override
	public void postNotification(String name, Object... objects) {
		Collection<NotificationObserver> notificationReceivers = observersForName.get(name);
		if (notificationReceivers != null) {
			for (NotificationObserver observer : notificationReceivers) {
				observer.receivedNotification(name, objects);
			}
		}
	}
	
}
