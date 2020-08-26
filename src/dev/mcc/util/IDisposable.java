package dev.mcc.util;

import java.util.ArrayList;

public interface IDisposable {
	static final ArrayList<IDisposable> DISPOSABLES = new ArrayList<IDisposable>();
	public static void disposeOnExit(IDisposable disposable) {
		DISPOSABLES.add(disposable);
	}
	public static void disposeAll() {
		for(IDisposable disposable : DISPOSABLES) {
			disposable.dispose();
		}
		DISPOSABLES.clear();
	}
	public static void disposeNow(IDisposable disposable) {
		if(DISPOSABLES.contains(disposable)) {
			DISPOSABLES.remove(disposable);
		}
		disposable.dispose();
	}
	public void dispose();
}
