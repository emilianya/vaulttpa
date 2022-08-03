package xyz.skelly.vaulttpa;

import java.util.UUID;

public class TpRequest {
	public UUID from;
	public UUID to;
	public UUID sentTo;
	public UUID sentBy;
	public TpRequest(UUID afrom, UUID ato, UUID asentBy, UUID asentTo) {
		from = afrom;
		to = ato;
		sentBy = asentBy;
		sentTo = asentTo;
	}
}
