package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import javax.sound.sampled.FloatControl;

public class Sound {

    Clip clip;
    URL[] soundUrl= new URL[60];
    float originalVolume = 1.0f;
    float currentVolume = originalVolume;

    public Sound() {
        // Ui Sound effects
        soundUrl[0] = setup("ui/ui_game_pause.wav");
        soundUrl[1] = setup("ui/ui_game_pausemenu_quit.wav");
        soundUrl[2] = setup("ui/ui_game_popup_in.wav");
        soundUrl[3] = setup("ui/ui_main_button_continue.wav");
        soundUrl[4] = setup("ui/ui_main_button_newgame.wav");
        soundUrl[5] = setup("ui/ui_main_roll_down.wav");
        soundUrl[6] = setup("ui/ui_main_select.wav");
        soundUrl[7] = setup("ui/ui_game_unpause.wav");
        soundUrl[22] =  setup("ui/ui_game_popup_out.wav");
        soundUrl[23] =  setup("ui/ui_main_button_options.wav");
        soundUrl[24] =  setup("ui/ui_main_nav_backtotitle_A.wav");
        soundUrl[25] =  setup("ui/ui_main_rollover.wav");
        soundUrl[34] = setup("ui/ui_inv_slide_in.wav");
        soundUrl[35] = setup("ui/ui_inv_slide_out.wav");
        soundUrl[36] = setup("ui/ui_inventory_rollover.wav");
        soundUrl[37] = setup("ui/ui_inventory_assign_invalid.wav");
        soundUrl[38] = setup("ui/ui_inventory_assign_left.wav");
        soundUrl[39] = setup("ui/ui_inventory_assign_right.wav");
        soundUrl[40] = setup("ui/ui_inventory_assign_top.wav");
        soundUrl[44] = setup("ui/ui_main_savefiles_panel_in.wav");
        soundUrl[45] = setup("ui/ui_main_savefiles_rollover.wav");
        soundUrl[46] = setup("ui/ui_main_savefiles_selectedfile_load.wav");
        soundUrl[47] = setup("ui/ui_main_savefiles_selectedfile_cancel.wav");
        soundUrl[48] = setup("ui/ui_main_savefiles_selectfile.wav");
        soundUrl[49] = setup("ui/ui_main_button_loadgame.wav");








        // Player Sounds effects
        soundUrl[8] = setup("player/en_shared_hit_02.wav");
        soundUrl[9] = setup("player/pl_gen_corpseshockwave.wav");
        soundUrl[10] = setup("player/pl_gen_death.wav");
        soundUrl[11] = setup("player/pl_gen_dodge_roll_B.wav");
        soundUrl[12] = setup("player/pl_gen_dodge_roll_F.wav");
        soundUrl[13] = setup("player/pl_gen_dodge_sidehop_L.wav");
        soundUrl[14] = setup("player/pl_gen_dodge_sidehop_R.wav");
        soundUrl[15] = setup("player/pl_gen_hurt_minor.wav");
        soundUrl[16] = setup("player/pl_gen_shield_block_01.wav");
        soundUrl[17] = setup("player/pl_itm_wep_sword_com1_01.wav");
        soundUrl[29] = setup("player/pl_gen_spell_heal.wav");
        soundUrl[30] = setup("player/pl_gen_shield_up_01.wav");
        soundUrl[31] = setup("player/pl_gen_shield_down_01.wav");
        soundUrl[32] = setup("player/pl_itm_wep_techbow_impact_01.wav");
        soundUrl[33] = setup("player/pl_itm_wep_techbow_shoot_01.wav");
        soundUrl[41] = setup("player/pl_itm_con_berryHP.wav");
        soundUrl[42] = setup("player/pl_itm_con_berryMP.wav");
        soundUrl[43] = setup("player/pl_itm_wep_techbow_shoot_ice_01.wav");



        // Monsters Sounds effects
        soundUrl[26] = setup("monsters/en_blob_attack_vo_01.wav");
        soundUrl[27] = setup("monsters/en_blob_death_01.wav");
        soundUrl[28] = setup("monsters/en_blob_hop_01.wav");

        // Musics
        soundUrl[18] = setup("musics/Tunic - Main Menu Music Title Screen.wav");
        soundUrl[19] = setup("musics/TUNIC (Original Soundtrack) - 02 Memories of Memories  Lifeformed × Janice Kwan.wav");
        soundUrl[20] = setup("musics/TUNIC (Original Soundtrack) - 24 Forget to Forget  Lifeformed × Janice Kwan.wav");
        soundUrl[21] = setup("musics/TUNIC (Original Soundtrack) - 49 Remember to Remember  Lifeformed × Janice Kwan.wav");


    }

    public URL setup(String fileName){
        return getClass().getResource("/sounds/"+fileName);

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
