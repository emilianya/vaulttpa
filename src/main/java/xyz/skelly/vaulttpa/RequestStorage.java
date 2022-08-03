package xyz.skelly.vaulttpa;


import jdk.jfr.internal.LogLevel;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class RequestStorage {
	static ArrayList<TpRequest> tpRequests = new ArrayList<TpRequest>();

	public static TpRequest getRequest(UUID player) {
		for (TpRequest i : tpRequests) {
			if (i.sentTo == player) {
				tpRequests.remove(i);
				return i;
			}
		}
		return null;
	}

	public static void createRequest(TpRequest request) {
		for (TpRequest i : tpRequests) {
			if (i.sentTo == request.sentTo) {
				tpRequests.remove(i);
			}
		}
		tpRequests.add(request);
	}
}
