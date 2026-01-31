package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import javax.sound.sampled.FloatControl;

public class Sound {

    Clip clip;
    URL[] soundUrl= new URL[50];
    float originalVolume = 1.0f;
    float currentVolume = originalVolume;

    public Sound() {
        // Ui Sound effects
        soundUrl[0] = getClass().getResource("/sounds/ui/ui_game_pause.wav");
        soundUrl[1] = getClass().getResource("/sounds/ui/ui_game_pausemenu_quit.wav");
        soundUrl[2] = getClass().getResource("/sounds/ui/ui_game_popup_in.wav");
        soundUrl[3] = getClass().getResource("/sounds/ui/ui_main_button_continue.wav");
        soundUrl[4] = getClass().getResource("/sounds/ui/ui_main_button_newgame.wav");
        soundUrl[5] = getClass().getResource("/sounds/ui/ui_main_roll_down.wav");
        soundUrl[6] = getClass().getResource("/sounds/ui/ui_main_select.wav");
        soundUrl[7] = getClass().getResource("/sounds/ui/ui_game_unpause.wav");
        soundUrl[22] =  getClass().getResource("/sounds/ui/ui_game_popup_out.wav");
        soundUrl[23] =  getClass().getResource("/sounds/ui/ui_main_button_options.wav");
        soundUrl[24] =  getClass().getResource("/sounds/ui/ui_main_nav_backtotitle_A.wav");
        soundUrl[25] =  getClass().getResource("/sounds/ui/ui_main_rollover.wav");

        // Player Sounds effects
        soundUrl[8] = getClass().getResource("/sounds/player/en_shared_hit_02.wav");
        soundUrl[9] = getClass().getResource("/sounds/player/pl_gen_corpseshockwave.wav");
        soundUrl[10] = getClass().getResource("/sounds/player/pl_gen_death.wav");
        soundUrl[11] = getClass().getResource("/sounds/player/pl_gen_dodge_roll_B.wav");
        soundUrl[12] = getClass().getResource("/sounds/player/pl_gen_dodge_roll_F.wav");
        soundUrl[13] = getClass().getResource("/sounds/player/pl_gen_dodge_sidehop_L.wav");
        soundUrl[14] = getClass().getResource("/sounds/player/pl_gen_dodge_sidehop_R.wav");
        soundUrl[15] = getClass().getResource("/sounds/player/pl_gen_hurt_minor.wav");
        soundUrl[16] = getClass().getResource("/sounds/player/pl_gen_shield_block_01.wav");
        soundUrl[17] = getClass().getResource("/sounds/player/pl_itm_wep_sword_com1_01.wav");
        soundUrl[29] = getClass().getResource("/sounds/player/pl_gen_spell_heal.wav");
        soundUrl[30] = getClass().getResource("/sounds/player/pl_gen_shield_up_01.wav");
        soundUrl[31] = getClass().getResource("/sounds/player/pl_gen_shield_down_01.wav");
        soundUrl[32] = getClass().getResource("/sounds/player/pl_itm_wep_techbow_impact_01.wav");
        soundUrl[33] = getClass().getResource("/sounds/player/pl_itm_wep_techbow_shoot_01.wav");


        // Monsters Sounds effects
        soundUrl[26] = getClass().getResource("/sounds/monsters/en_blob_attack_vo_01.wav");
        soundUrl[27] = getClass().getResource("/sounds/monsters/en_blob_death_01.wav");
        soundUrl[28] = getClass().getResource("/sounds/monsters/en_blob_hop_01.wav");

        // Musics
        soundUrl[18] = getClass().getResource("/sounds/musics/Tunic - Main Menu Music Title Screen.wav");
        soundUrl[19] = getClass().getResource("/sounds/musics/TUNIC (Original Soundtrack) - 02 Memories of Memories  Lifeformed × Janice Kwan.wav");
        soundUrl[20] = getClass().getResource("/sounds/musics/TUNIC (Original Soundtrack) - 24 Forget to Forget  Lifeformed × Janice Kwan.wav");
        soundUrl[21] = getClass().getResource("/sounds/musics/TUNIC (Original Soundtrack) - 49 Remember to Remember  Lifeformed × Janice Kwan.wav");


    }

    public void setVolume(float volumeScale) {

        try {
            if (clip != null) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                if (volumeScale < 0.01f) {
                    gainControl.setValue(-80.0f);
                } else {

                    float volumeInDb = (float) (20.0f * Math.log10(volumeScale));

                    if (volumeInDb > 6.0f) volumeInDb = 6.0f;

                    gainControl.setValue(volumeInDb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  setFile(int index){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[index]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(currentVolume);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateVolume(float volume){
        currentVolume = volume;
        setVolume(currentVolume);
    }

    public void play(){
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}
