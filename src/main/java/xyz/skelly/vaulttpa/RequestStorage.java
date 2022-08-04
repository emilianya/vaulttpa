package xyz.skelly.vaulttpa;


import jdk.jfr.internal.LogLevel;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class RequestStorage {
	static ArrayList<TpRequest> tpRequests = new ArrayList<TpRequest>();

	public static TpRequest getRequest(UUID player) {
		TpRequest req = null;
		for (TpRequest i : tpRequests) {
			if (i.sentTo == player) {
				req = i;
				break;
			}
		}
		tpRequests.remove(req);
		return req;
	}

	public static void createRequest(TpRequest request) {
		tpRequests.removeIf(req -> req.sentTo == request.sentTo);
		tpRequests.add(request);
	}
}