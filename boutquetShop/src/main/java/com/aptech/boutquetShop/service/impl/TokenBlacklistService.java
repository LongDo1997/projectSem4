package com.aptech.boutquetShop.service.impl;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
	// Stores token as Key, Expiration Time as Value
	private final Map<String, Date> blacklist = new ConcurrentHashMap<>();

	public void blacklistToken(String token, Date expirationDate) {
		blacklist.put(token, expirationDate);
	}

	public boolean isBlacklisted(String token) {
		Date expiration = blacklist.get(token);
		if (expiration == null) return false;

		// If the token's natural expiration time has already passed, clean it up
//		if (expiration.before(new Date())) {
//			blacklist.remove(token);
//			return false;
//		}
		return true;
	}
}
