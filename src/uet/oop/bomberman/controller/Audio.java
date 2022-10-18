package uet.oop.bomberman.controller;

public class Audio {
    public enum AudioType {
        LOBBY, PLAYING, EXPLODING, EAT_ITEM, KILL_ENEMY, LOSE, WIN, CHOOSE, DIE
    }

    AudioMaster[] audiosList = new AudioMaster[5];

    public Audio() {
        audiosList[AudioType.LOBBY.ordinal()] = new AudioMaster("res/audio/Title Screen.wav");
        audiosList[AudioType.PLAYING.ordinal()] = new AudioMaster("res/audio/playing.wav");
        audiosList[AudioType.EXPLODING.ordinal()] = new AudioMaster("res/audio/exploding.wav");
        audiosList[AudioType.EAT_ITEM.ordinal()] = new AudioMaster("res/audio/eatItem.wav");
    }

    public void playParallel(AudioType audioName, int time) {
        AudioMaster audio = audiosList[audioName.ordinal()].copyAudio();
        audio.play(time);
    }


    public void playAlone(AudioType audioName, int time) {
        for (int i = 0; i < audiosList.length; i++) {
            if (i != audioName.ordinal()) {
                if (audiosList[i] != null) {
                    audiosList[i].stop();
                }
            }
        }
        audiosList[audioName.ordinal()].play(time);
    }
}
