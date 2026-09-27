package MelodyMesh;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private Clip clip;
    private long pausePosition;
    private FloatControl volumeControl;

    public boolean play(String filePath) {
        try {
            stop();
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Audio file not found: " + filePath);
                return false;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            pausePosition = 0;
            clip.start();

            return true;

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Only supported audio formats such as WAV can be played.");

        } catch (LineUnavailableException e) {
            System.out.println("Audio device is unavailable.");

        } catch (Exception e) {
            System.out.println("Playback error: " + e.getMessage());
        }

        return false;
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
            System.out.println("Audio paused.");
        } else {
            System.out.println("No audio is currently playing.");
        }
    }

    public void resume() {
        if (clip != null) {
            clip.setMicrosecondPosition(pausePosition);
            clip.start();
            System.out.println("Audio resumed.");
        } else {
            System.out.println("No audio loaded.");
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            pausePosition = 0;
        }
    }

    public void setVolume(float percent) {
        if (volumeControl == null) {
            System.out.println("Volume control unavailable.");
            return;
        }

        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();

        float value = min + (max - min) * percent / 100.0f;

        volumeControl.setValue(value);

        System.out.println("Volume set to " + percent + "%.");
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public long getDurationSeconds() {
        if (clip == null) {
            return 0;
        }

        return clip.getMicrosecondLength() / 1_000_000;
    }
}
