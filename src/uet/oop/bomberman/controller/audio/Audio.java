package uet.oop.bomberman.controller.audio;

public class Audio {
    public enum AudioType {
        LOBBY, PLAYING, EXPLODING, PICKUP, KILL_ENEMY, LOSE, WIN, CHOOSE, DIE
    }

    AudioMaster[] audiosList = new AudioMaster[11];
    private boolean muted = false;

    public Audio() {
        audiosList[AudioType.LOBBY.ordinal()] = new AudioMaster("res/audio/lobby.wav");
        audiosList[AudioType.PLAYING.ordinal()] = new AudioMaster("res/audio/playing.wav");
        audiosList[AudioType.EXPLODING.ordinal()] = new AudioMaster("res/audio/exploding.wav");
        audiosList[AudioType.PICKUP.ordinal()] = new AudioMaster("res/audio/pick_up.wav");
        audiosList[AudioType.KILL_ENEMY.ordinal()] = new AudioMaster("res/audio/monster_die.wav");
        audiosList[AudioType.CHOOSE.ordinal()] = new AudioMaster("res/audio/choose.wav");
        audiosList[AudioType.DIE.ordinal()] = new AudioMaster("res/audio/die.wav");
        audiosList[AudioType.WIN.ordinal()] = new AudioMaster("res/audio/win.wav");
        audiosList[AudioType.LOSE.ordinal()] = new AudioMaster("res/audio/lose.mid");
    }

    public boolean isMuted() {
        return muted;
    }

    public void setAudioOption(boolean muted) {
        this.muted = muted;
        if (muted) {
            for (AudioMaster audio: audiosList) {
                if (audio != null) {
                    audio.stop();
                }
            }
        }
    }

    public void playOnBackground(AudioType audioName, int time) {
        if (muted) return;
        AudioMaster audio = audiosList[audioName.ordinal()].copyAudio();
        audio.play(time);
    }


    public void playAlone(AudioType audioName, int time) {
        if (muted) return;
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
