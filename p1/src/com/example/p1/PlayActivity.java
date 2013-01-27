package com.example.p1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class PlayActivity extends Activity {
	// static final String MUSIC_DIR = "/DCIM/music/";
	static final String MUSIC_DIR = "/DCIM/Big Buddha/";

	Button playPauseButton;

	private MediaPlayer m_mediaPlayer;
	private MediaController mcontroller;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_audiobasics);
		playPauseButton = (Button) findViewById(R.id.play_pause);
		
		
		
		m_mediaPlayer = new MediaPlayer();
//		
	mcontroller = new MediaController(this);
	        VideoView videoView = (VideoView) findViewById(R.id.VideoView);
//
	        mcontroller.setAnchorView(videoView);
// 	        mcontroller.setMediaPlayer(videoView);
//	        mcontroller.show();

	        
	        
	        
	      //  VideoView videoView = (VideoView) findViewById(R.id.VideoView);
//	        MediaController mediaController = new MediaController(this);
//	        mediaController.setAnchorView(videoView);
//	// Set video link (mp4 format )
//	        Uri video = Uri.parse("mp4 video link");
//	        videoView.setMediaController(mediaController);
//	        videoView.setVideoURI(video);
//	        videoView.start();

	        
	        
	        
	        

//		String MusicDir = Environment.getExternalStorageDirectory()
//				.getAbsolutePath() + MUSIC_DIR;
//
//		Intent i = new Intent(this, ListFiles.class);
//		i.putExtra("directory", MusicDir);
//		startActivityForResult(i, 0);

		
		 AssetFileDescriptor fd= getFd() ;
		 
		try {		
		m_mediaPlayer.setDataSource( fd.getFileDescriptor());
		m_mediaPlayer.prepare();

		Toast.makeText(getApplicationContext(), "  OK.",
				Toast.LENGTH_SHORT).show();
	} catch (Exception e) {
		e.printStackTrace();
	}
	startMP();
		
		playPauseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (m_mediaPlayer.isPlaying()) {
					pauseMP();
				} else {
					startMP();
				}
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0 && resultCode == RESULT_OK) {
			String tmp = data.getExtras().getString("clickedFile");
			try {
				m_mediaPlayer.setDataSource(tmp);
				m_mediaPlayer.prepare();

				Toast.makeText(getApplicationContext(), "onActivityResult OK.",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			startMP();
		}
	}

	public void pauseMP() {
		playPauseButton.setText("Play");
		m_mediaPlayer.pause();
	}

	public void startMP() {
		m_mediaPlayer.start();
		playPauseButton.setText("Pause");
	}

	boolean needToResume = false;

	@Override
	protected void onPause() {
		if (m_mediaPlayer != null && m_mediaPlayer.isPlaying()) {
			needToResume = true;
			pauseMP();

		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (needToResume && m_mediaPlayer != null) {
			startMP();
		}
	}

	
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * the MediaController will hide after 3 seconds - tap the screen to
         * make it appear again
         */
     // seems to crash          mcontroller.show();
        return false;
    }
	 public boolean canPause() {
		    return true;
		  }

		  public boolean canSeekBackward() {
		    return true;
		  }

		  public boolean canSeekForward() {
		    return true;
		  }
	
	protected AssetFileDescriptor getFd() {
		AssetFileDescriptor fd=null;
		AssetManager am = getApplicationContext().getAssets();

		try {
			fd = am.openFd("Chipmunks - Happy Birthday to You!!!.mp4.mp3");

			return fd;
		} catch (Exception e) {
			e.printStackTrace();
		};
		return fd;
	};

//	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
//	int frequency = 11025; // Hz
//	int bufferSize = 50 * AudioTrack.getMinBufferSize(frequency,
//			AudioFormat.CHANNEL_OUT_MONO, audioEncoding);
//
//	public AudioRecord audioRecord = new AudioRecord(
//			MediaRecorder.AudioSource.MIC, frequency,
//			AudioFormat.CHANNEL_IN_MONO, audioEncoding, bufferSize);

}
