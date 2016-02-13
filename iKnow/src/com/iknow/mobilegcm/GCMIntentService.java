package com.iknow.mobilegcm;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.androidexample.mobilegcm.R;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	
	private Controller aController = null;
	SharedPreferences settings;
	SharedPreferences.Editor edit;
	boolean state = false;
	//private static int i = 0;
    public GCMIntentService() {
    	// Call extended class Constructor GCMBaseIntentService
    	super(Config.GOOGLE_SENDER_ID);
    	
		
        
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
    	
    	
    	//Get Global Controller Class object (see application tag in AndroidManifest.xml)
    	if(aController == null)
           aController = (Controller) getApplicationContext();
    	
    	Log.i(TAG, "---------- onRegistered -------------");
        Log.i(TAG, "Device registered: regId = " + registrationId);
  //      aController.displayRegistrationMessageOnScreen(context, "Your device registred with GCM");
        Log.d("NAME", MainActivity.name);
        
        aController.register(context, MainActivity.name, MainActivity.email, registrationId,MainActivity.imei, MainActivity.interest1, MainActivity.interest2, MainActivity.interest3);
        
        DBAdapter.addDeviceData(MainActivity.name, MainActivity.email, registrationId, MainActivity.imei);
    
        
        
    }

    /**
     * Method called on device unregistred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	Log.i(TAG, "---------- onUnregistered -------------");
        Log.i(TAG, "Device unregistered");
        aController.displayRegistrationMessageOnScreen(context, getString(R.string.gcm_unregistered));
        aController.unregister(context, registrationId,MainActivity.imei);
    }

    /**
     * Method called on Receiving a new message from GCM server
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
    	Log.i(TAG, "---------- onMessage -------------");
        String message = intent.getExtras().getString("message");
        
        Log.i("GCM","message : "+message);
     //   String temp = message;
        String[] StringAll;
		StringAll = message.split("\\^");
        
		String user = "";
		String title = "";
		String imei  = "";
		String question = "";
		String qid = "";
		String answer = "";
		String qtime="";
		int StringLength = StringAll.length;
		if (StringLength == 5) {

			user   = StringAll[0];
		//	imei    = StringAll[1];
			title = StringAll[1];
			question = StringAll[2];
			qid = StringAll[3];
			
			
			
	//	title = "333";
	//	question = "111";
		 // Call broadcast defined on ShowMessage.java to show message on ShowMessage.java screen
         aController.displayMessageOnScreen(context, user,title,question,imei,qid);
         // generate notification to notify user
         settings = this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
 		edit = settings.edit();
         state = settings.getBoolean("notification", true);
         if(state)
         generateQuestionNotification(context, user,title, question,qid);
		}
		else if(StringLength==6)
		{
			
			
			
			user = StringAll[0];
			question = StringAll[1];
			answer = StringAll[2];
			title = StringAll[3];
			qid = StringAll[4];
			qtime = StringAll[5];
			settings = this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
			edit = settings.edit();
			state = settings.getBoolean("notification", true);
			if(state)
			 generateAnswerNotification(context, user, question, answer, title, qid, qtime);
			
		}
		else
		{
			
		}
      //   UserData userdata = new UserData(1,imei,id,question);  // need to modify!
      //   DBAdapter.addUserData(userdata);
         
       
        
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
    	Log.i(TAG, "---------- onDeletedMessages -------------");
        String message = getString(R.string.gcm_deleted, total);
        
        String title = "DELETED";
        // aController.displayMessageOnScreen(context, message);
        
        // generate notification to notify user
     //   generateNotification(context,"",title,message,"","");
    } 

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
    	Log.i(TAG, "---------- onError -------------");
        Log.i(TAG, "Received error: " + errorId);
        
        aController.displayRegistrationMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
    	Log.i(TAG, "---------- onRecoverableError -------------");
    	
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayRegistrationMessageOnScreen(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    
    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private static void generateQuestionNotification(Context context,String user, String title, String question, String qid) {
        int icon = R.drawable.iknownotification;
        long when = System.currentTimeMillis();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
        Date date = new Date(when);
       // System.out.println(formatter.format(date));
        
        
		NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, question, when);

        Intent notificationIntent = new Intent(context, DisplayAnswer.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        notificationIntent.putExtra("quser", user);
        notificationIntent.putExtra("qtitle", title);
        notificationIntent.putExtra("question", question);
      //  notificationIntent.putExtra("imei", imei);
        notificationIntent.putExtra("qid", qid);
        notificationIntent.putExtra("qtime", formatter.format(date));
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title, question, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);   
       // i++;
        
        
    }
    
    
    private static void generateAnswerNotification(Context context,String quser, String question, String answer, String title, String qid, String qtime) {
        int icon = R.drawable.iknownotification;
        long when = System.currentTimeMillis();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
        Date date = new Date(when);
       // System.out.println(formatter.format(date));
        
        
		NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, "iKnow", when);

        Intent notificationIntent = new Intent(context, DisplayAnswer.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        notificationIntent.putExtra("quser", quser);
        notificationIntent.putExtra("qtitle", title);
        notificationIntent.putExtra("question", question);
    //    notificationIntent.putExtra("imei", imei);
        notificationIntent.putExtra("qid", qid);
        notificationIntent.putExtra("qtime", qtime);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, "有新的回答噢！", answer, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);   
       // i++;
        
        
    }

}
