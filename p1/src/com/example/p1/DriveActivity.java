package com.example.p1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class DriveActivity extends Activity {
	static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION = 2;
	static final int CAPTURE_IMAGE = 3;
	protected static final String TAG = "DriveActivity";

	private static Uri fileUri;
	private static Drive service;
	private GoogleAccountCredential credential;
	String accountName;
	private Context mContext;

	public static final String AUTHTOKENSCOPE_DRIVE = "oauth2:https://www.googleapis.com/auth/drive";



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		credential = GoogleAccountCredential.usingOAuth2(this,
				DriveScopes.DRIVE);
		startActivityForResult(credential.newChooseAccountIntent(),
				REQUEST_ACCOUNT_PICKER);


	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {

		System.out.println("  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>    onActivityResult (requestCode,resultCode)= "
				+ requestCode + "," + resultCode);

		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == RESULT_OK && data != null
			&& data.getExtras() != null) {
				accountName = data
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

				System.out.println("      accountName = " + accountName);
				if (accountName != null) {
					credential.setSelectedAccountName(accountName);
					service = getDriveService(credential);
					// startCameraIntent();
					writeFileToDrive();
				}
			}
			break;
		case REQUEST_AUTHORIZATION:
			if (resultCode == Activity.RESULT_OK) {
				saveFileToDrive();
			} else {
				startActivityForResult(credential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
			break;
		case CAPTURE_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				saveFileToDrive();
			}
		}
	}

	private void startCameraIntent() {
		String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getPath();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
		.format(new Date());
		fileUri = Uri.fromFile(new java.io.File(mediaStorageDir
				+ java.io.File.separator + "IMG_" + timeStamp + ".jpg"));

		System.out.println("      startCameraIntent mediaStorageDir= "
				+ mediaStorageDir);
		System.out.println("      startCameraIntent fileUri= " + fileUri);

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(cameraIntent, CAPTURE_IMAGE);
	}




	private void writeFileToDrive() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {


				//	File myFile = new File("/sdcard/mysdfile.txt"); // /storage/emulated/legacy/Download/f1.txt
				try {
					java.io.File myFile = new java.io.File("/storage/emulated/legacy/Download/f1.txt"); // /storage/emulated/legacy/Download/f1.txt

					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = 
							new OutputStreamWriter(fOut);
					String plansjson = Utilities.plansToJSON().toString(5);
					String tasksjson = Utilities.tasksToJSON().toString(5);

					myOutWriter.append(plansjson);
					myOutWriter.append(tasksjson);
					myOutWriter.close();
					fOut.close();
				} catch (Exception e){e.printStackTrace();};
				
				// File's metadata.
				File body = new File();
				body.setTitle("f1.txt");
				body.setDescription("");
				body.setMimeType("text/plain");

				java.io.File fileContent = new java.io.File("/storage/emulated/legacy/Download/f1.txt");
				FileContent mediaContent = new FileContent("text/plain", fileContent);

				System.out.println("fileContent =/" + fileContent + "/");
				System.out.println("mediaContent =/" + mediaContent + "/");

				try {
					File file = service.files().insert(body, mediaContent).execute();

					// Uncomment the following line to print the File ID.
					System.out.println("File ID: %s" + file.getId());

					//  return file;
				} catch (UserRecoverableAuthIOException e) {
					System.out.println("An UserRecoverableAuthIOException occured: " + e);

					startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);

				} catch (IOException e) {
					System.out.println("An error occured: " + e);
					System.out.println("    error  cause: " + e.getCause());
					//    return null;
				}
			}
		});
		t.start();
	}

	private void saveFileToDrive() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// File's binary content
					System.out.println("saveFileToDrive1");

					java.io.File fileContent = new java.io.File(fileUri
							.getPath());
					System.out.println("saveFileToDrive2");

					FileContent mediaContent = new FileContent("image/jpeg",
							fileContent);

					System.out.println("      saveFileToDrive  ");

					// File's metadata.
					File body = new File();
					body.setTitle(fileContent.getName());
					body.setMimeType("image/jpeg");

					File file = service.files().insert(body, mediaContent)
							.execute();
					if (file != null) {
						showToast("Photo uploaded: " + file.getTitle());
						startCameraIntent();
					}
				} catch (UserRecoverableAuthIOException e) {
					startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
		.build();
	}
	//			new GsonFactory(), "481231366281.apps.googleusercontent.com","0s-U40-bN5XPx3azley8aMv7",credential).build();
	//		try {
	////	        GoogleAccountCredential credential =
	////	            GoogleAccountCredential.usingOAuth2(mContext, DriveScopes.DRIVE_FILE);
	//	        credential.setSelectedAccountName(accountName);
	//	        // Trying to get a token right away to see if we are authorized
	//	        credential.getToken();
	//	       service = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
	//	                        new GsonFactory(), credential).build();
	//	                        } catch (Exception e) {
	//	                                Log.e(TAG, "Failed to get token");
	//	                                // If the Exception is User Recoverable, we display a notification that will trigger the
	//	                                // intent to fix the issue.
	//	                                if (e instanceof UserRecoverableAuthException) {
	//	                                        UserRecoverableAuthException exception = (UserRecoverableAuthException) e;
	//	                                        NotificationManager notificationManager = (NotificationManager) mContext
	//	                                            .getSystemService(Context.NOTIFICATION_SERVICE);
	//	                                        Intent authorizationIntent = exception.getIntent();
	//	                                        authorizationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
	//	                                            Intent.FLAG_FROM_BACKGROUND);
	//	                                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
	//	                                            authorizationIntent, 0);
	//	                                        Notification notification = new Notification.Builder(mContext)
	//	                                            .setSmallIcon(android.R.drawable.ic_dialog_alert)
	//	                                            .setTicker("Permission requested")
	//	                                            .setContentTitle("Permission requested")
	//	                                            .setContentText("for account " + accountName)
	//	                                            .setContentIntent(pendingIntent).setAutoCancel(true).build();
	//	                                        notificationManager.notify(0, notification);
	//	                                } else {
	//	                                        e.printStackTrace();
	//	                                }
	//	                        }
	//		return service;


	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
