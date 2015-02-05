package app;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twitterImp.TweetSearch;
import twitterImp.TwitterPost;
import twitterImp.TwitterTrends;
import twitterImp.TwitterUserInfo;
import bean.Analytics;
import bean.Tweet;
import bean.UserInfo;


@RestController
public class AppController {
	
	
	
	TwitterPost post = new TwitterPost();
	TwitterUserInfo twtUserInfo = new TwitterUserInfo();
	TwitterTrends twtTrendsInfo = new TwitterTrends();
	TweetSearch twtSearch = new TweetSearch();
	Map<String, String> blacklistMap = new HashMap<String, String>();
	Map userInfoCacheMap = new HashMap();
	Map<String, Integer> apiCounter = new HashMap<String, Integer>();
	public static Analytics hitcount = new Analytics();
	
	public  AppController() throws IOException {
		// TODO Auto-generated constructor stub
		apiCounter.put("tweetPost", 0);
		apiCounter.put("trends", 0);
		apiCounter.put("tweetSearch", 0);
		apiCounter.put("userInfo", 0);
		apiCounter.put("failedHit", 0);

	}
	
		
    
    @RequestMapping(value="/tweet",method = RequestMethod.POST)
    public String tweet(@RequestBody Tweet tweetObj,HttpServletRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	System.out.println(request.getRemoteAddr());

        String temp = tweetObj.getTweetString();
        
        String tweetPstStatus=post.postTweet(temp);
        apiCounter.replace("tweetPost", apiCounter.get("tweetPost"), (int)apiCounter.get("tweetPost")+1);
        System.out.println("Temp+Count"+apiCounter.get("tweetPost")+temp); 
        return tweetPstStatus;
    }
    
    @RequestMapping(value="/userinfo/{user_name}",method = RequestMethod.GET)
    public UserInfo userInfo(@PathVariable("user_name") String userName,HttpServletRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	System.out.println("hello");

    	System.out.println("hello 2");
    	UserInfo userInfoStr = new UserInfo();
    	
    	try
	    	{
	        if(blacklistMap.containsKey(request.getRemoteAddr()))
	        {
	        	System.out.println("IP is a bocked IP"+request.getRemoteAddr()); 
	        }
	        else
	        {
	        	if(userInfoCacheMap.containsKey(userName))
	        	{
	        		userInfoStr=(UserInfo)userInfoCacheMap.get(userName);
	        		System.out.println("From Map!!");
	        	}
	        	else
	        	{
	        	System.out.println(request.getRemoteAddr());        	
	        	userInfoStr = twtUserInfo.getUserInfo(userName);
	            userInfoCacheMap.put(userName, userInfoStr);
	        	}
	        }  	 	
    	}
    	finally
    	{
    		System.out.println("Appcontrol finally");
	        apiCounter.replace("userInfo", apiCounter.get("userInfo"), (int)apiCounter.get("userInfo")+1);
	        return userInfoStr;
    	}
    }
    
    @RequestMapping(value="/trends/{place_id}",method = RequestMethod.GET)
    public List<String> userInfo(@PathVariable("place_id") int id,HttpServletRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	System.out.println(request.getRemoteAddr());

    	List<String> returnResult = twtTrendsInfo.getTrendsInfo(id);
        apiCounter.replace("trends", (int)apiCounter.get("trends"), (int)apiCounter.get("trends")+1);
        return returnResult;
    }
    
    @RequestMapping(value="/tweetsearch/{searchString}",method = RequestMethod.GET)
    public List<String> searchTweet(@PathVariable("searchString") String searchStr,HttpServletRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	System.out.println(request.getRemoteAddr());

    	List<String> tweetSearchStr = twtSearch.getTweets(searchStr); 
        apiCounter.replace("tweetSearch", apiCounter.get("tweetSearch"), (int)apiCounter.get("tweetSearch")+1);
        return tweetSearchStr;
    }
    
    @RequestMapping(value="/getanalytics",method = RequestMethod.GET)
    public Analytics getAnalytics(HttpServletRequest request)  
    {
    	System.out.println("Inside analytics"+request.getRemoteAddr());

        hitcount.setPostedCount((int)apiCounter.get("tweetPost"));
        hitcount.setTrendsCount((int)apiCounter.get("trends"));
        hitcount.setTweetSearchCount((int)apiCounter.get("tweetSearch"));
        hitcount.setUserInfoCount((int)apiCounter.get("userInfo"));
        
        return hitcount;
    }
    
    @RequestMapping(value="/blacklistme",method = RequestMethod.GET)
    public void blacklist(HttpServletRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	System.out.println("Inside blacklist"+request.getRemoteAddr());

    	blacklistMap.put(request.getRemoteAddr(),request.getRemoteAddr());
  
    }
    
    @RequestMapping(value="/whitelistme",method = RequestMethod.GET)
    public void whitelist(HttpServletRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	System.out.println("Inside blacklist"+request.getRemoteAddr());

    	blacklistMap.remove(request.getRemoteAddr());
  
    }
}
