package com.zapcom.api_gateway.JwtAuthenticatonFilter;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	public String secret_key = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
	// write the method to extract the username from the token by dividing the token
	// into parts and extracting the username from the payload

	public String extractusername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		try {

			final Claims claims = extractAllClaims(token);
			return claimsResolver.apply(claims);
		} catch (Exception e) {
			System.err.println("Error extracting claims: " + e.getMessage());
			throw e;
		}

	}

	public Claims extractAllClaims(String token) {

		try {

			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getsigningkey()).build().parseClaimsJws(token);

			return claimsJws.getBody();
		} catch (Exception e) {
			System.err.println("Error extracting all claims: " + e.getMessage());
			throw e;
		}
	}

	private Key getsigningkey() {
		byte[] keybytes = Decoders.BASE64.decode(secret_key);
		return Keys.hmacShaKeyFor(keybytes);
	}
	// write the method to generate the token by taking the userdetails as arguments
	// and setting the claims and signing the token
	// for example

	public String geneartejwttoken(UserDetails userdetails) {
		return generatetoken(new HashMap<String, Object>(), userdetails);
	}

	public String generatetoken(Map<String, Object> extraclaims, UserDetails userdetails) {

		return Jwts.builder().setClaims(extraclaims).setSubject(userdetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5))
				.signWith(getsigningkey(), SignatureAlgorithm.HS256).compact();
	}

	// write the method to check if the token is valid by checking the username and
	// the expiration date
	// for example
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = userDetails.getUsername();
		return username.equalsIgnoreCase(extractusername(token)) && !isTokenexpired(token);
	}

	private boolean isTokenexpired(String token) {

		return extractexpiratrion(token).before(new Date());
	}

	public Date extractexpiratrion(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
