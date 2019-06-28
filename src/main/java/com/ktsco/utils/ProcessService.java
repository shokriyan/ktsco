package com.ktsco.utils;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ProcessService extends Service<Void>{

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				Thread.sleep(4000);
				return null;
			}
			
		};
	}
	
	

}
