package uet.oop.bomberman.controller.audio;

import javax.sound.sampled.*;
import java.io.File;

public class AudioMaster {
    private AudioInputStream audioInput;
    private Clip clip;
    private boolean isPlaying = false;
    private String audioSrc;

    public AudioMaster(String audioSrc) {
        File audioFile = new File(audioSrc);
        this.audioSrc = audioSrc;
        try {
            audioInput = AudioSystem.getAudioInputStream(audioFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            clip = AudioSystem.getClip();
            clip.open(audioInput);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void play(int time) {
        if(!isPlaying) {
            if(time > 0) clip.loop(time-1);
            else clip.loop(-1);
        }
        isPlaying = true;
    }

    public void stop() {
        if(isPlaying) {
            clip.stop();
        }
        isPlaying = false;
    }

    public AudioMaster copyAudio() {
        AudioMaster copy = new AudioMaster(audioSrc);
        return copy;
    }
}
