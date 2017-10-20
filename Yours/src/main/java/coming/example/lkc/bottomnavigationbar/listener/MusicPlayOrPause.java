package coming.example.lkc.bottomnavigationbar.listener;

import coming.example.lkc.bottomnavigationbar.dao.SingList;

/**
 * Created by lkc on 2017/9/27.
 */

public interface MusicPlayOrPause {
    void Play();

    void Pause();

    void AutoNext();

    void SwitchBackground();

    void isLooping(boolean looping);

    void Progress(int progress, int lefttext, int righttext);

    void OnError();
}
