package Interfaces;

import Models.Message;

public interface Publisher {
	void addObserver(Subscriber o);
	void removeObserver(Subscriber o);
	void notifyObservers(Message m);
}
