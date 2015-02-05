package util;

import java.util.HashMap;
import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

public class TwitterUtil {
	
	 static String consumerKeyStr = "Enter Consumer Key"; //YHviJbUgGGALfjHBeuTXh2ov4
	 static String consumerSecretStr = "Enter Consumer Secret Key"; // 5Tda9FiZ0bo0qof166IlvfmWfPqAULm9UyC2qG1XCokLvdWzKb
	 static String accessTokenStr ="Enter Access Token"; //2759246442-x0MD8Ac9egYmkgUAqZ4j12RSDSuHUE93xPZzZ47 
	 static String accessTokenSecretStr = "Enter Token Secret"; //Na0Z5lqchzqlmnPm2abnzVCBANuYljwEFNyMHLCr2Onnk
	 public Map<String, Integer> hitMiss = new HashMap<String, Integer>();

	protected OAuthConsumer oAuthConsumer;

	protected TwitterUtil() {

		oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKeyStr,
				consumerSecretStr);
		oAuthConsumer.setTokenWithSecret(accessTokenStr, accessTokenSecretStr);
		hitMiss.put("failedHit", 0);
	}
}
