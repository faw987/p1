package com.example.p1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class DriveActivity extends Activity {
	static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION = 2;
	static final int CAPTURE_IMAGE = 3;
	protected static final String TAG = "DriveActivity";
	
	static final String TEMP_LOCAL_STORAGE_DIR = "/storage/emulated/legacy/Download/";

	private static Uri fileUri;
	private static Drive service;
	private GoogleAccountCredential credential;
	String accountName;
	private Context mContext;

	public static final String AUTHTOKENSCOPE_DRIVE = "oauth2:https://www.googleapis.com/auth/drive";
	
	String function;

	String fileidFromWrite;
	String token=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			function = extras.getString("FUNCTION");
			System.out.println(TAG + " -- function=" + function);
		}

		credential = GoogleAccountCredential.usingOAuth2(this,
				DriveScopes.DRIVE);
		startActivityForResult(credential.newChooseAccountIntent(),
				REQUEST_ACCOUNT_PICKER);

	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {

		System.out.println(TAG
				+ "  >>>>>>>>>>    onActivityResult (requestCode,resultCode)= "
				+ requestCode + "," + resultCode);

		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER: {
			System.out.println(TAG + "  >>>>>>>>>>   REQUEST_ACCOUNT_PICKER ");

			if (resultCode == RESULT_OK && data != null
					&& data.getExtras() != null) {
				accountName = data
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

				System.out.println("      accountName = " + accountName);

				if (accountName != null) {
					credential.setSelectedAccountName(accountName);
					service = getDriveService(credential);
					// startCameraIntent();
					if ("W".equals(function))
						writeFileToDrive("f1.txt");
					if ("R".equals(function))
						readFileFromDrive("f1.txt");
					if ("P".equals(function))
						postFileToWeb("f1.html");
					if ("L".equals(function))
						listFileToDrive();
				}
			}
			break;
		}
		case REQUEST_AUTHORIZATION:{
			System.out.println(TAG + "  >>>>>>>>>>   REQUEST_AUTHORIZATION ");

			if (resultCode == Activity.RESULT_OK) {
				saveFileToDrive();
			} else {
				startActivityForResult(credential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
			break;}
		case CAPTURE_IMAGE:
			System.out.println(TAG + "  >>>>>>>>>>   CAPTURE_IMAGE ");

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

	private void readFileFromDrive(final String ftitle) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (token==null){
					try {
						token = credential.getToken();
					} catch (Exception e) {
						e.printStackTrace();
						token=null;
					}
					;}
				System.out.println("      token = " + token);

				java.io.File folder = new java.io.File("/papad");

				List<File> files = retrieveFilterQFiles(service, 
						"mimeType = 'text/plain' and trashed = false" + 
								" and title = '" + ftitle + "'");

				File f1 = files.get(0); 									// HACK HACK HACK
				String id = f1.getId();
				String url = f1.getDownloadUrl();

				System.out.println(" readFileFromDrive      id = " + id);
				System.out.println(" readFileFromDrive      url = " + url);

				File gf = new File();
				gf.setTitle(ftitle);
				gf.setId(id);
				gf.setDownloadUrl(url);
				// gf.setMimeType("text/plain");
				// gf.setOriginalFilename("f1.txt");
 				System.out.println(" readFileFromDrive      gf = " + gf);

				try {
					java.io.File f = downloadGFileToJFolder(service, token, gf,
							folder);
					Utilities.printFile(f);
				} catch (Exception e) {
					e.printStackTrace();
				}
				;
			}
		});
		t.start();
	}

	private void writeFileToDrive(final String ftitle) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (token==null){
					try {
						token = credential.getToken();
					} catch (Exception e) {
						e.printStackTrace();
					}
					;}
				System.out.println("      token = " + token);

				try {
				//	java.io.File myFile = new java.io.File(TEMP_LOCAL_STORAGE_DIR + "f1.txt");  
					java.io.File myFile = new java.io.File(TEMP_LOCAL_STORAGE_DIR + ftitle);  
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(
							fOut);
					String plansjson = Utilities.plansToJSON().toString(5);
					String tasksjson = Utilities.tasksToJSON().toString(5);

					myOutWriter.append(plansjson);
					myOutWriter.append(tasksjson);
					myOutWriter.close();
					fOut.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				;

				// File's metadata.
				File body = new File();
				//body.setTitle("f1.txt");
				body.setTitle(ftitle);
				body.setDescription("");
				body.setMimeType("text/plain");

				java.io.File fileContent = new java.io.File(TEMP_LOCAL_STORAGE_DIR + ftitle);
				FileContent mediaContent = new FileContent("text/plain",
						fileContent);

				System.out.println("fileContent =/" + fileContent + "/");
				System.out.println("mediaContent =/" + mediaContent + "/");

				try {
					File file = service.files().insert(body, mediaContent)
							.execute();

					// Uncomment the following line to print the File ID.
					System.out.println("File ID: %s" + file.getId());
 					System.out.println("File " + file);

					// return file;
				} catch (UserRecoverableAuthIOException e) {
					System.out
					.println("An UserRecoverableAuthIOException occured: "
							+ e);

					startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);

				} catch (IOException e) {
					System.out.println("An error occured: " + e);
					System.out.println("    error  cause: " + e.getCause());
					e.printStackTrace();
					// return null;
				}
			}
		});
		t.start();
	}

	private void listFileToDrive() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				  String fileList="";

				if (token==null){
					try {
						token = credential.getToken();
					} catch (Exception e) {
						e.printStackTrace();
					}
					;}
				System.out.println("      token = " + token);
				
			    Looper.prepare(); 

				try {
					FileList file = service.files().list().execute();
					int i=0;
					List<File> files = retrieveAllFiles(service);
					Log.i(TAG, "size is " + files.size());
					for (File file1 : files) {
						Log.i(TAG, "title = " + file1.getTitle());
						
						Log.i(TAG, "id = " + file1.getId());
						i++;
						fileList += i + ": " + file1.getTitle() + "  " + file1.getId();
						
						// System.out.println("      file1 = " + file1 );
						
						
//						JSONObject jsonObj = new JSONObject(file1);
//
//						System.out.println("\n\n\n");
//
//						try {
//							System.out.println("      jsonObj = "
//									+ jsonObj.toString(3));
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						;
						

					}

					// HACK - not so easy .... makeToast(fileList);
					
					System.out.println("File " + file);

					// return file;
				} catch (UserRecoverableAuthIOException e) {
					System.out
					.println("An UserRecoverableAuthIOException occured: "
							+ e);

					startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);

				} catch (IOException e) {
					System.out.println("An error occured: " + e);
					System.out.println("    error  cause: " + e.getCause());
					e.printStackTrace();
					// return null;
				}
			}
			// HACK - not so easy .... public void onStart( ) {
			// HACK - not so easy ....     Looper.prepare(); 
			// HACK - not so easy .... }
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
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).build();
	}

	// new GsonFactory(),
	// "481231366281.apps.googleusercontent.com","0s-U40-bN5XPx3azley8aMv7",credential).build();
	// try {
	// // GoogleAccountCredential credential =
	// // GoogleAccountCredential.usingOAuth2(mContext, DriveScopes.DRIVE_FILE);
	// credential.setSelectedAccountName(accountName);
	// // Trying to get a token right away to see if we are authorized
	// credential.getToken();
	// service = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
	// new GsonFactory(), credential).build();
	// } catch (Exception e) {
	// Log.e(TAG, "Failed to get token");
	// // If the Exception is User Recoverable, we display a notification that
	// will trigger the
	// // intent to fix the issue.
	// if (e instanceof UserRecoverableAuthException) {
	// UserRecoverableAuthException exception = (UserRecoverableAuthException)
	// e;
	// NotificationManager notificationManager = (NotificationManager) mContext
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Intent authorizationIntent = exception.getIntent();
	// authorizationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
	// Intent.FLAG_FROM_BACKGROUND);
	// PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
	// authorizationIntent, 0);
	// Notification notification = new Notification.Builder(mContext)
	// .setSmallIcon(android.R.drawable.ic_dialog_alert)
	// .setTicker("Permission requested")
	// .setContentTitle("Permission requested")
	// .setContentText("for account " + accountName)
	// .setContentIntent(pendingIntent).setAutoCancel(true).build();
	// notificationManager.notify(0, notification);
	// } else {
	// e.printStackTrace();
	// }
	// }
	// return service;

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	// private java.io.File downloadGFileToJFolder(Drive drive, String token,
	// File gFile, java.io.File jFolder) throws IOException {
	private java.io.File downloadGFileToJFolder(Drive drive, String token,
			File gFile, java.io.File jFolder) throws IOException {

		System.out.println(" downloadGFileToJFolder            gFile=" + gFile);

		//
		// if (gFile.getDownloadUrl() != null && gFile.getDownloadUrl().length()
		// > 0 ) {
		// if (jFolder == null) {
		// jFolder = Environment.getExternalStorageDirectory();
		// jFolder.mkdirs();
		// }
		try {

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(gFile.getDownloadUrl());

			System.out.println("   downloadGFileToJFolder   get=" + get);

			get.setHeader("Authorization", "Bearer " + token);
			HttpResponse response = client.execute(get);
			System.out.println("   downloadGFileToJFolder   response="
					+ response);

			InputStream inputStream = response.getEntity().getContent();
			System.out.println("   downloadGFileToJFolder   inputStream="
					+ inputStream);

			jFolder.mkdirs();
			// java.io.File jFile = new
			// java.io.File(Environment.getExternalStorageDirectory() + "/" +
			// jFolder.getAbsolutePath() + "/" + getGFileName(gFile)); //
			// getGFileName() is my own method... it just grabs originalFilename
			// if it exists or title if it doesn't.

			System.out.println("   downloadGFileToJFolder   Environment.getExternalStorageDirectory()=" + Environment.getExternalStorageDirectory());

			java.io.File jFile = new java.io.File(TEMP_LOCAL_STORAGE_DIR + "f1.txt");
			System.out.println("   downloadGFileToJFolder   jFile=" + jFile);

			FileOutputStream fileStream = new FileOutputStream(jFile);
			System.out.println("   downloadGFileToJFolder   fileStream="
					+ fileStream);

			byte buffer[] = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				fileStream.write(buffer, 0, length);
			}
			fileStream.close();
			inputStream.close();
			return jFile;
		} catch (IOException e) {
			// Handle IOExceptions here...
			System.out
			.println("      ======================================================= EXCEPTION");
			e.printStackTrace();
			return null;
		}
		// } else {
		// // Handle the case where the file on Google Drive has no length here.
		// return null;
		// }
	}

	// getGFileName() is my own method... it just grabs originalFilename if it
	// exists or title if it doesn't.

	private String getGFileName(File gFile) {
		return gFile.getTitle(); // HACK
	}

	/**
	 * Retrieve a list of all File resources.
	 * 
	 * @param service
	 *            Drive API service instance.
	 * @return List of File resources.
	 */
	private static List<File> retrieveAllFiles(Drive service ) {
		return retrieveFilterQFiles(  service, "");
	}
	/**
	 * Retrieve a list of filtered File resources.
	 * 
	 * @param service
	 *            Drive API service instance.
	 * @param q
	 *            query string.
	 * @return List of File resources.
	 */
	private static List<File> retrieveFilterQFiles(Drive service, String q) {
		List<File> result = new ArrayList<File>();
		Files.List request = null;
		try {
			request = service.files().list().setQ(q);
			//				.setQ("mimeType = 'text/plain' and trashed = false" + q);
		} catch (IOException e) {
			Log.e(TAG, "", e);
			e.printStackTrace();
			return result;
		}

		do {
			try {
				FileList files = request.execute();

				result.addAll(files.getItems());
				request.setPageToken(files.getNextPageToken());
			} catch (IOException e) {
				Log.e(TAG, "", e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);
		return result;
	}
	
	
	private void postFileToWeb(final String ftitle) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (token==null){
					try {
						token = credential.getToken();
					} catch (Exception e) {
						e.printStackTrace();
					}
					;}
				System.out.println("      token = " + token);
				

				try {
				//	java.io.File myFile = new java.io.File(TEMP_LOCAL_STORAGE_DIR + "f1.txt");  
					java.io.File myFile = new java.io.File(TEMP_LOCAL_STORAGE_DIR + ftitle);  
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(
							fOut);
//					String plansjson = Utilities.plansToJSON().toString(5);
//					String tasksjson = Utilities.tasksToJSON().toString(5);
//
//					myOutWriter.append(plansjson);
//					myOutWriter.append(tasksjson);
					
					myOutWriter.append("<!doctype html>\n<html lang=\"en\">\n<head>");
					myOutWriter.append("<title>TEST</title>\n</head>\n<body>");
					myOutWriter.append("<h2>PAPA information here.</h2>");
					myOutWriter.append("</body>");

					 
					myOutWriter.close();
					fOut.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				;

				// File's metadata.
				File body = new File();
				//body.setTitle("f1.txt");
				body.setTitle(ftitle);
				body.setDescription("");
				body.setMimeType("text/plain");

				java.io.File fileContent = new java.io.File(TEMP_LOCAL_STORAGE_DIR + ftitle);
				FileContent mediaContent = new FileContent("text/plain",
						fileContent);

				System.out.println("fileContent =/" + fileContent + "/");
				System.out.println("mediaContent =/" + mediaContent + "/");

				try {
					File file = service.files().insert(body, mediaContent)
							.execute();

					// Uncomment the following line to print the File ID.
					System.out.println("File ID: %s" + file.getId());
 					System.out.println("File " + file);

					// return file;
				} catch (UserRecoverableAuthIOException e) {
					System.out
					.println("An UserRecoverableAuthIOException occured: "
							+ e);

					startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);

				} catch (IOException e) {
					System.out.println("An error occured: " + e);
					System.out.println("    error  cause: " + e.getCause());
					e.printStackTrace();
					// return null;
				}
			}
			
			public void onStart( ) {
			    Looper.prepare(); 
			}
			
		});
		t.start();
	}
	// HACK - not so easy .... public void makeToast(String message) {	
	// HACK - not so easy .... 	Toast toast = Toast.makeText( getApplicationContext(), message, Toast.LENGTH_LONG);
	// HACK - not so easy .... 	toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset() / 2);
	// HACK - not so easy .... 	toast.show();
	// HACK - not so easy .... }
}
