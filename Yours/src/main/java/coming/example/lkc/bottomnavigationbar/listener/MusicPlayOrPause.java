package coming.example.lkc.bottomnavigationbar.listener;

/**
 * Created by lkc on 2017/9/27.
 */

public interface MusicPlayOrPause {
    void Play();

    void Pause();

    void AutoNext();

    void Progress(int progress, int lefttext, int righttext);
}
